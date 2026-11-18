package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.SkillCategory
import com.example.network.AccountProfileDto
import com.example.network.AccountRepository
import com.example.ui.components.AboutAppDialog
import com.example.ui.components.KenyanGreen
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.BiasharaViewModel
import kotlinx.coroutines.launch

enum class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME("home", "Home", Icons.Filled.Home, Icons.Outlined.Home),
    EXPLORE("explore", "Explore", Icons.Filled.Explore, Icons.Outlined.Explore),
    SAVED("saved", "Saved", Icons.Filled.Bookmark, Icons.Outlined.BookmarkBorder),
    PROFILE("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

private enum class AppEntryState {
    CHECKING_SESSION,
    AUTHENTICATION,
    ONBOARDING,
    MAIN
}

private enum class SecondaryScreen {
    MATCHER,
    TRENDS,
    STORIES
}

class MainActivity : ComponentActivity() {

    private val viewModel: BiasharaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BiasharaTheme {

                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                val accountRepository = remember {
                    AccountRepository(applicationContext)
                }

                var entryState by remember {
                    mutableStateOf(AppEntryState.CHECKING_SESSION)
                }

                var accountProfile by remember {
                    mutableStateOf<AccountProfileDto?>(null)
                }
                var savedSkillIds by remember {
                    mutableStateOf<Set<String>>(emptySet())
                }

                var isLoadingSavedSkills by remember {
                    mutableStateOf(false)
                }

                var currentScreen by remember {
                    mutableStateOf(Screen.HOME)
                }

                // Secondary screens remain outside the four bottom navigation tabs.
                var secondaryScreen by remember {
                    mutableStateOf<SecondaryScreen?>(null)
                }

                // The secondary screen to return to when Android Back is pressed.
                var secondaryParent by remember {
                    mutableStateOf<SecondaryScreen?>(null)
                }

                // The original main tab from which secondary navigation started.
                var secondaryReturnScreen by remember {
                    mutableStateOf(Screen.HOME)
                }

                var isSearching by remember {
                    mutableStateOf(false)
                }

                var selectedCategoryFilter by remember {
                    mutableStateOf<SkillCategory?>(null)
                }

                var showCalculatorScreen by remember {
                    mutableStateOf(false)
                }

                val snackbarHostState = remember {
                    SnackbarHostState()
                }

                val coroutineScope = rememberCoroutineScope()

                // -------------------------------------------------
                // 1. RESTORE ACCOUNT SESSION
                // -------------------------------------------------

                LaunchedEffect(accountRepository) {

                    val restoredAccount =
                        accountRepository.restoreSession()

                    if (restoredAccount != null) {

                        accountProfile = restoredAccount

                        entryState =
                            if (uiState.isOnboardingCompleted) {
                                AppEntryState.MAIN
                            } else {
                                AppEntryState.ONBOARDING
                            }

                    } else {

                        accountProfile = null
                        entryState = AppEntryState.AUTHENTICATION
                    }
                }

                // -------------------------------------------------
                // RESTORE SAVED STARTING CAPITAL
                // -------------------------------------------------

                LaunchedEffect(accountProfile) {

                    accountProfile?.startingCapitalKes?.let { savedCapital ->

                        viewModel.setExactCapital(savedCapital)
                    }
                }

                // -------------------------------------------------
                // LOAD SAVED USER SKILL SELECTIONS
                // -------------------------------------------------

                LaunchedEffect(accountProfile, entryState) {

                    if (
                        entryState == AppEntryState.ONBOARDING &&
                        accountProfile != null
                    ) {

                        isLoadingSavedSkills = true

                        try {

                            val selection = accountRepository.getMySkillSelection()

                            savedSkillIds = selection.skillIds.toSet()

                        } catch (e: Exception) {

                            savedSkillIds = emptySet()

                        } finally {

                            isLoadingSavedSkills = false
                        }

                    } else {

                        savedSkillIds = emptySet()
                        isLoadingSavedSkills = false
                    }
                }

                // -------------------------------------------------
                // 2. HANDLE ACCOUNT LOGIN
                // -------------------------------------------------

                fun onAccountAuthenticated(profile: AccountProfileDto) {

                    accountProfile = profile

                    currentScreen = Screen.HOME
                    isSearching = false
                    showCalculatorScreen = false

                    secondaryScreen = null
                    secondaryParent = null
                    secondaryReturnScreen = Screen.HOME

                    entryState =
                        if (uiState.isOnboardingCompleted) {
                            AppEntryState.MAIN
                        } else {
                            AppEntryState.ONBOARDING
                        }
                }

                // -------------------------------------------------
                // 3. HANDLE LOGOUT
                // -------------------------------------------------

                fun logoutAccount() {

                    coroutineScope.launch {

                        accountRepository.logout()

                        accountProfile = null

                        currentScreen = Screen.HOME
                        isSearching = false
                        showCalculatorScreen = false

                        secondaryScreen = null
                        secondaryParent = null
                        secondaryReturnScreen = Screen.HOME

                        entryState = AppEntryState.AUTHENTICATION
                    }
                }

                // -------------------------------------------------
                // 4. SHOW NOTIFICATIONS
                // -------------------------------------------------

                LaunchedEffect(uiState.userNotification) {

                    uiState.userNotification?.let { msg ->

                        coroutineScope.launch {

                            snackbarHostState.showSnackbar(msg)

                            viewModel.clearNotification()
                        }
                    }
                }

                // -------------------------------------------------
                // 5. AUTHENTICATION GATE
                // -------------------------------------------------

                when (entryState) {

                    AppEntryState.CHECKING_SESSION -> {

                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = Color.White
                        ) {

                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {

                                CircularProgressIndicator(
                                    color = KenyanGreen
                                )
                            }
                        }
                    }

                    AppEntryState.AUTHENTICATION -> {

                        AccountAccessScreen(
                            repository = accountRepository,
                            onAuthenticated = { profile ->
                                onAccountAuthenticated(profile)
                            }
                        )
                    }

                    AppEntryState.ONBOARDING -> {

                        var isSavingOnboarding by remember {
                            mutableStateOf(false)
                        }

                        if (isLoadingSavedSkills) {

                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = Color.White
                            ) {

                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {

                                    CircularProgressIndicator(
                                        color = KenyanGreen
                                    )
                                }
                            }

                        } else {

                            OnboardingFlow(

                                availableSkills = uiState.allSkills,

                                // Restore skills previously saved in PostgreSQL.
                                initialSelectedSkillIds = savedSkillIds,

                                // Restore starting capital previously saved in DataBase.
                                initialSelectedCapitalKes =
                                    accountProfile?.startingCapitalKes ?: 25000,

                                onFinishOnboarding = {
                                    name,
                                    email,
                                    selectedSkills,
                                    capital ->

                                    if (!isSavingOnboarding) {

                                        isSavingOnboarding = true

                                        coroutineScope.launch {

                                            try {

                                                // SAVE CAPITAL TO AUTHENTICATED USER RECORD.
                                                accountProfile = accountRepository.saveStartingCapital(capital)
                                                
                                                // SAVE SKILLS TO AUTHENTICATED USER RECORD.
                                                accountRepository.saveMySkillSelection(
                                                    skillIds = selectedSkills.toList(),
                                                    customSkills = emptyList()
                                                )

                                                // COMPLETE ONBOARDING AFTER SUCCESSFUL SAVE.
                                                viewModel.completeOnboarding(
                                                    name = name,
                                                    email = email,
                                                    selectedSkillIds = selectedSkills,
                                                    capitalKes = capital
                                                )

                                                entryState = AppEntryState.MAIN

                                            } catch (e: Exception) {

                                                snackbarHostState.showSnackbar(
                                                    "Could not save your selections. Please try again."
                                                )

                                            } finally {

                                                isSavingOnboarding = false
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }

                    AppEntryState.MAIN -> {

                        // Defensive protection against an absent profile.
                        val authenticatedProfile = accountProfile

                        if (authenticatedProfile == null) {

                            entryState = AppEntryState.AUTHENTICATION

                        } else {

                            // -------------------------------------------------
                            // 6. EXISTING MAIN APPLICATION
                            // -------------------------------------------------

                            val hasActiveOverlay =
                                isSearching ||
                                uiState.selectedDetailIdea != null ||
                                showCalculatorScreen ||
                                secondaryScreen != null ||
                                uiState.showAboutDialog ||
                                uiState.showCompareSheet ||
                                uiState.showJourneySheet ||
                                uiState.showPlanDraftSheet

                            BackHandler(
                                enabled = hasActiveOverlay ||
                                    currentScreen != Screen.HOME
                            ) {

                                when {

                                    uiState.selectedDetailIdea != null ->
                                        viewModel.selectDetailIdea(null)

                                    isSearching ->
                                        isSearching = false

                                    showCalculatorScreen ->
                                        showCalculatorScreen = false

                                    uiState.showAboutDialog ->
                                        viewModel.closeAboutDialog()

                                    uiState.showCompareSheet ->
                                        viewModel.closeCompareSheet()

                                    uiState.showJourneySheet ->
                                        viewModel.closeJourneySheet()

                                    uiState.showPlanDraftSheet ->
                                        viewModel.closeBusinessPlanDraft()

                                    secondaryParent != null -> {
                                        secondaryScreen = secondaryParent
                                        secondaryParent = null
                                    }

                                    secondaryScreen != null -> {
                                        secondaryScreen = null
                                        secondaryParent = null
                                        currentScreen = secondaryReturnScreen
                                    }

                                    currentScreen != Screen.HOME ->
                                        currentScreen = Screen.HOME
                                }
                            }

                            Scaffold(
                                modifier = Modifier.fillMaxSize(),

                                snackbarHost = {
                                    SnackbarHost(snackbarHostState)
                                },

                                bottomBar = {

                                    NavigationBar(
                                        containerColor = Color.White,
                                        tonalElevation = 6.dp,
                                        windowInsets = WindowInsets.navigationBars,
                                        modifier = Modifier.testTag(
                                            "bottom_nav_bar"
                                        )
                                    ) {

                                        Screen.values().forEach { screen ->

                                            val isSelected =
                                                currentScreen == screen &&
                                                !isSearching

                                            NavigationBarItem(

                                                selected = isSelected,

                                                onClick = {

                                                    isSearching = false

                                                    secondaryScreen = null
                                                    secondaryParent = null

                                                    currentScreen = screen
                                                },

                                                icon = {

                                                    Icon(
                                                        imageVector =
                                                            if (isSelected) {
                                                                screen.selectedIcon
                                                            } else {
                                                                screen.unselectedIcon
                                                            },

                                                        contentDescription =
                                                            screen.title
                                                    )
                                                },

                                                label = {

                                                    Text(
                                                        text = screen.title,

                                                        fontWeight =
                                                            if (isSelected) {
                                                                FontWeight.Bold
                                                            } else {
                                                                FontWeight.Normal
                                                            }
                                                    )
                                                },

                                                colors =
                                                    NavigationBarItemDefaults.colors(

                                                        selectedIconColor =
                                                            KenyanGreen,

                                                        selectedTextColor =
                                                            KenyanGreen,

                                                        indicatorColor =
                                                            Color(0xFFE8F5E9),

                                                        unselectedIconColor =
                                                            Color(0xFF9CA3AF),

                                                        unselectedTextColor =
                                                            Color(0xFF6B7280)
                                                    ),

                                                modifier = Modifier.testTag(
                                                    "nav_${screen.route}"
                                                )
                                            )
                                        }
                                    }
                                }

                            ) { innerPadding ->

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                ) {

                                    if (isSearching) {

                                        SearchResultsScreen(
                                            uiState = uiState,
                                            viewModel = viewModel,

                                            onBack = {
                                                isSearching = false
                                            },

                                            onSelectIdea = { idea ->
                                                viewModel.selectDetailIdea(idea)
                                            }
                                        )

                                    } else if (showCalculatorScreen) {

                                        CalculatorScreen(
                                            uiState = uiState,
                                            viewModel = viewModel
                                        )

                                    } else if (secondaryScreen != null) {

                                        when (secondaryScreen) {

                                            SecondaryScreen.MATCHER -> {

                                                MatcherScreen(
                                                    uiState = uiState,
                                                    viewModel = viewModel,

                                                    onNavigateToTrends = {

                                                        secondaryParent = SecondaryScreen.MATCHER
                                                        secondaryScreen = SecondaryScreen.TRENDS
                                                    },

                                                    onNavigateToStories = {

                                                        secondaryParent = SecondaryScreen.MATCHER
                                                        secondaryScreen = SecondaryScreen.STORIES
                                                    }
                                                )
                                            }

                                            SecondaryScreen.TRENDS -> {

                                                TrendsScreen(
                                                    uiState = uiState,
                                                    viewModel = viewModel
                                                )
                                            }

                                            SecondaryScreen.STORIES -> {

                                                SuccessStoriesScreen(
                                                    uiState = uiState,
                                                    viewModel = viewModel
                                                )
                                            }

                                            null -> Unit
                                        }

                                    } else {
                                        AnimatedContent(
                                            targetState = currentScreen,

                                            transitionSpec = {
                                                fadeIn() togetherWith fadeOut()
                                            },

                                            label = "MainScreenTransition"

                                        ) { target ->

                                            when (target) {

                                                Screen.HOME -> {

                                                    HomeDashboardScreen(
                                                        uiState = uiState,
                                                        viewModel = viewModel,

                                                        onNavigateToExplore = { cat ->

                                                            selectedCategoryFilter = cat
                                                            currentScreen = Screen.EXPLORE
                                                        },

                                                        onNavigateToSearch = {
                                                            isSearching = true
                                                        },

                                                        onNavigateToMatcher = {

                                                            secondaryReturnScreen = Screen.HOME
                                                            secondaryParent = null
                                                            secondaryScreen = SecondaryScreen.MATCHER
                                                        },

                                                        onSelectIdea = { idea ->
                                                            viewModel.selectDetailIdea(idea)
                                                        }
                                                    )
                                                }

                                                Screen.EXPLORE -> {

                                                    BrowseIdeasScreen(
                                                        uiState = uiState,
                                                        viewModel = viewModel,

                                                        initialCategory =
                                                            selectedCategoryFilter,

                                                        onCategoryChanged = { category ->
                                                            selectedCategoryFilter = category
                                                        },

                                                        onBack = {
                                                            currentScreen = Screen.HOME
                                                        },

                                                        onSelectIdea = { idea ->
                                                            viewModel.selectDetailIdea(idea)
                                                        }
                                                    )
                                                }

                                                Screen.SAVED -> {

                                                    SavedIdeasScreen(
                                                        uiState = uiState,
                                                        viewModel = viewModel,

                                                        onBack = {
                                                            currentScreen = Screen.HOME
                                                        },

                                                        onSelectIdea = { idea ->
                                                            viewModel.selectDetailIdea(idea)
                                                        },

                                                        onNavigateToExplore = {
                                                            currentScreen = Screen.EXPLORE
                                                        }
                                                    )
                                                }

                                                Screen.PROFILE -> {

                                                    ProfileScreen(
                                                        uiState = uiState,
                                                        viewModel = viewModel,

                                                        accountProfile =
                                                            authenticatedProfile,

                                                        onLogout = {
                                                            logoutAccount()
                                                        },

                                                        onNavigateToSaved = {
                                                            currentScreen = Screen.SAVED
                                                        },

                                                        onNavigateToStories = {

                                                            secondaryReturnScreen = Screen.PROFILE
                                                            secondaryParent = null
                                                            secondaryScreen = SecondaryScreen.STORIES
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    // IDEA DETAILS

                                    uiState.selectedDetailIdea?.let { idea ->

                                        IdeaDetailsScreen(
                                            idea = idea,

                                            isSaved =
                                                uiState.savedIdeaIds.contains(idea.id),

                                            onBack = {
                                                viewModel.selectDetailIdea(null)
                                            },

                                            onToggleSave = {
                                                viewModel.toggleSaveIdea(idea.id)
                                            },

                                            onOpenCalculator = {

                                                viewModel.loadIdeaIntoCalculator(idea)
                                                showCalculatorScreen = true
                                            },

                                            onOpenBusinessPlan = {
                                                viewModel.openBusinessPlanDraft(idea)
                                            },

                                            onStartJourney = {
                                                viewModel.openJourneyForIdea(idea)
                                            }
                                        )
                                    }

                                    // JOURNEY SHEET

                                    if (
                                        uiState.showJourneySheet &&
                                        uiState.activeJourneyIdea != null &&
                                        uiState.activeJourneyProgress != null
                                    ) {

                                        JourneySheet(
                                            idea = uiState.activeJourneyIdea!!,

                                            progress =
                                                uiState.activeJourneyProgress!!,

                                            tasks = uiState.defaultJourneyTasks,

                                            onToggleTask = { taskId ->
                                                viewModel.toggleJourneyTask(taskId)
                                            },

                                            onSaveNotes = { notes ->
                                                viewModel.updateJourneyNotes(notes)
                                            },

                                            onOpenBusinessPlan = {
                                                viewModel.openBusinessPlanDraft(
                                                    uiState.activeJourneyIdea!!
                                                )
                                            },

                                            onDismiss = {
                                                viewModel.closeJourneySheet()
                                            }
                                        )
                                    }

                                    // BUSINESS PLAN

                                    if (
                                        uiState.showPlanDraftSheet &&
                                        uiState.activeBusinessPlanDraft != null
                                    ) {

                                        BusinessPlanDialog(
                                            draft =
                                                uiState.activeBusinessPlanDraft!!,

                                            onSaveDraft = { updated ->
                                                viewModel.updateBusinessPlanDraft(updated)
                                            },

                                            onDismiss = {
                                                viewModel.closeBusinessPlanDraft()
                                            }
                                        )
                                    }

                                    // COMPARE SHEET

                                    if (uiState.showCompareSheet) {

                                        val comparedList =
                                            uiState.allIdeas.filter {
                                                uiState.comparedIdeaIds.contains(it.id)
                                            }

                                        CompareSheet(
                                            ideas = comparedList,

                                            onRemoveFromCompare = { id ->
                                                viewModel.toggleCompareIdea(id)
                                            },

                                            onSelectIdea = { idea ->
                                                viewModel.selectDetailIdea(idea)
                                            },

                                            onDismiss = {
                                                viewModel.closeCompareSheet()
                                            }
                                        )
                                    }

                                    // ABOUT DIALOG

                                    if (uiState.showAboutDialog) {

                                        AboutAppDialog(
                                            onDismiss = {
                                                viewModel.closeAboutDialog()
                                            },

                                            onClearData = {
                                                viewModel.clearAllUserData()
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}