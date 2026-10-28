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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.SkillCategory
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

class MainActivity : ComponentActivity() {

    private val viewModel: BiasharaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BiasharaTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                var currentScreen by remember { mutableStateOf(Screen.HOME) }
                var isSearching by remember { mutableStateOf(false) }
                var selectedCategoryFilter by remember { mutableStateOf<SkillCategory?>(null) }
                var showCalculatorScreen by remember { mutableStateOf(false) }
                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()

                // Show feedback notifications
                LaunchedEffect(uiState.userNotification) {
                    uiState.userNotification?.let { msg ->
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(msg)
                            viewModel.clearNotification()
                        }
                    }
                }

                // Smooth hardware back button handling
                val hasActiveOverlay = isSearching ||
                        uiState.selectedDetailIdea != null ||
                        showCalculatorScreen ||
                        uiState.showAboutDialog ||
                        uiState.showCompareSheet ||
                        uiState.showJourneySheet ||
                        uiState.showPlanDraftSheet

                BackHandler(enabled = hasActiveOverlay || currentScreen != Screen.HOME) {
                    when {
                        uiState.selectedDetailIdea != null -> viewModel.selectDetailIdea(null)
                        isSearching -> isSearching = false
                        showCalculatorScreen -> showCalculatorScreen = false
                        uiState.showAboutDialog -> viewModel.closeAboutDialog()
                        uiState.showCompareSheet -> viewModel.closeCompareSheet()
                        uiState.showJourneySheet -> viewModel.closeJourneySheet()
                        uiState.showPlanDraftSheet -> viewModel.closeBusinessPlanDraft()
                        currentScreen != Screen.HOME -> currentScreen = Screen.HOME
                    }
                }

                if (!uiState.isOnboardingCompleted) {
                    // First-Time User Onboarding Flow (Screens 1 to 4)
                    OnboardingFlow(
                        availableSkills = uiState.allSkills,
                        onFinishOnboarding = { name, email, selectedSkills, capital ->
                            viewModel.completeOnboarding(
                                name = name,
                                email = email,
                                selectedSkillIds = selectedSkills,
                                capitalKes = capital
                            )
                        }
                    )
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        bottomBar = {
                            NavigationBar(
                                containerColor = Color.White,
                                tonalElevation = 6.dp,
                                windowInsets = WindowInsets.navigationBars,
                                modifier = Modifier.testTag("bottom_nav_bar")
                            ) {
                                Screen.values().forEach { screen ->
                                    val isSelected = currentScreen == screen && !isSearching
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = {
                                            isSearching = false
                                            currentScreen = screen
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                                                contentDescription = screen.title
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = screen.title,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = KenyanGreen,
                                            selectedTextColor = KenyanGreen,
                                            indicatorColor = Color(0xFFE8F5E9),
                                            unselectedIconColor = Color(0xFF9CA3AF),
                                            unselectedTextColor = Color(0xFF6B7280)
                                        ),
                                        modifier = Modifier.testTag("nav_${screen.route}")
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
                                // Screen 8: Search Results Screen
                                SearchResultsScreen(
                                    uiState = uiState,
                                    viewModel = viewModel,
                                    onBack = { isSearching = false },
                                    onSelectIdea = { idea -> viewModel.selectDetailIdea(idea) }
                                )
                            } else if (showCalculatorScreen) {
                                CalculatorScreen(
                                    uiState = uiState,
                                    viewModel = viewModel
                                )
                            } else {
                                AnimatedContent(
                                    targetState = currentScreen,
                                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                                    label = "MainScreenTransition"
                                ) { target ->
                                    when (target) {
                                        Screen.HOME -> HomeDashboardScreen(
                                            uiState = uiState,
                                            viewModel = viewModel,
                                            onNavigateToExplore = { cat ->
                                                selectedCategoryFilter = cat
                                                currentScreen = Screen.EXPLORE
                                            },
                                            onNavigateToSearch = { isSearching = true },
                                            onSelectIdea = { idea -> viewModel.selectDetailIdea(idea) }
                                        )

                                        Screen.EXPLORE -> BrowseIdeasScreen(
                                            uiState = uiState,
                                            viewModel = viewModel,
                                            initialCategory = selectedCategoryFilter,
                                            onBack = { currentScreen = Screen.HOME },
                                            onSelectIdea = { idea -> viewModel.selectDetailIdea(idea) }
                                        )

                                        Screen.SAVED -> SavedIdeasScreen(
                                            uiState = uiState,
                                            viewModel = viewModel,
                                            onBack = { currentScreen = Screen.HOME },
                                            onSelectIdea = { idea -> viewModel.selectDetailIdea(idea) },
                                            onNavigateToExplore = { currentScreen = Screen.EXPLORE }
                                        )

                                        Screen.PROFILE -> ProfileScreen(
                                            uiState = uiState,
                                            viewModel = viewModel,
                                            onNavigateToSaved = { currentScreen = Screen.SAVED },
                                            onNavigateToStories = {
                                                // Quick jump to explore
                                                currentScreen = Screen.EXPLORE
                                            }
                                        )
                                    }
                                }
                            }

                            // Screen 7: Idea Details Screen (opened whenever an idea is selected)
                            uiState.selectedDetailIdea?.let { idea ->
                                IdeaDetailsScreen(
                                    idea = idea,
                                    isSaved = uiState.savedIdeaIds.contains(idea.id),
                                    onBack = { viewModel.selectDetailIdea(null) },
                                    onToggleSave = { viewModel.toggleSaveIdea(idea.id) },
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

                            // 6-Stage Hustle Journey Sheet
                            if (uiState.showJourneySheet && uiState.activeJourneyIdea != null && uiState.activeJourneyProgress != null) {
                                JourneySheet(
                                    idea = uiState.activeJourneyIdea!!,
                                    progress = uiState.activeJourneyProgress!!,
                                    tasks = uiState.defaultJourneyTasks,
                                    onToggleTask = { taskId -> viewModel.toggleJourneyTask(taskId) },
                                    onSaveNotes = { notes -> viewModel.updateJourneyNotes(notes) },
                                    onOpenBusinessPlan = {
                                        viewModel.openBusinessPlanDraft(uiState.activeJourneyIdea!!)
                                    },
                                    onDismiss = { viewModel.closeJourneySheet() }
                                )
                            }

                            // SCI Business Plan Draft Sheet
                            if (uiState.showPlanDraftSheet && uiState.activeBusinessPlanDraft != null) {
                                BusinessPlanDialog(
                                    draft = uiState.activeBusinessPlanDraft!!,
                                    onSaveDraft = { updated -> viewModel.updateBusinessPlanDraft(updated) },
                                    onDismiss = { viewModel.closeBusinessPlanDraft() }
                                )
                            }

                            // Compare Sheet
                            if (uiState.showCompareSheet) {
                                val comparedList = uiState.allIdeas.filter { uiState.comparedIdeaIds.contains(it.id) }
                                CompareSheet(
                                    ideas = comparedList,
                                    onRemoveFromCompare = { id -> viewModel.toggleCompareIdea(id) },
                                    onSelectIdea = { idea -> viewModel.selectDetailIdea(idea) },
                                    onDismiss = { viewModel.closeCompareSheet() }
                                )
                            }

                            // About Dialog
                            if (uiState.showAboutDialog) {
                                AboutAppDialog(
                                    onDismiss = { viewModel.closeAboutDialog() },
                                    onClearData = { viewModel.clearAllUserData() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
