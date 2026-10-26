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
import com.example.ui.components.AboutAppDialog
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
    MATCHER("matcher", "Matcher", Icons.Filled.Psychology, Icons.Outlined.Psychology),
    EXPLORE("explore", "Catalogue", Icons.Filled.Storefront, Icons.Outlined.Storefront),
    CALCULATOR("calculator", "Calculator", Icons.Filled.Calculate, Icons.Outlined.Calculate),
    STORIES("stories", "Stories", Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents),
    LAUNCHPAD("launchpad", "Launchpad", Icons.Filled.RocketLaunch, Icons.Outlined.RocketLaunch)
}

class MainActivity : ComponentActivity() {

    private val viewModel: BiasharaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BiasharaTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                var currentScreen by remember { mutableStateOf(Screen.MATCHER) }
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

                // Handle hardware back button smoothly
                val hasOpenSheet = uiState.selectedDetailIdea != null ||
                        uiState.showAboutDialog ||
                        uiState.showCompareSheet ||
                        uiState.showJourneySheet ||
                        uiState.showPlanDraftSheet

                BackHandler(enabled = hasOpenSheet || currentScreen != Screen.MATCHER) {
                    when {
                        uiState.selectedDetailIdea != null -> viewModel.selectDetailIdea(null)
                        uiState.showAboutDialog -> viewModel.closeAboutDialog()
                        uiState.showCompareSheet -> viewModel.closeCompareSheet()
                        uiState.showJourneySheet -> viewModel.closeJourneySheet()
                        uiState.showPlanDraftSheet -> viewModel.closeBusinessPlanDraft()
                        currentScreen != Screen.MATCHER -> currentScreen = Screen.MATCHER
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 6.dp,
                            windowInsets = WindowInsets.navigationBars,
                            modifier = Modifier.testTag("bottom_nav_bar")
                        ) {
                            Screen.values().forEach { screen ->
                                val isSelected = currentScreen == screen
                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = { currentScreen = screen },
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
                                        selectedIconColor = SciBluePrimary,
                                        selectedTextColor = SciBluePrimary,
                                        indicatorColor = SciBlueContainer,
                                        unselectedIconColor = TextMuted,
                                        unselectedTextColor = TextMuted
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
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = {
                                fadeIn() togetherWith fadeOut()
                            },
                            label = "ScreenTransition"
                        ) { target ->
                            when (target) {
                                Screen.MATCHER -> MatcherScreen(
                                    uiState = uiState,
                                    viewModel = viewModel,
                                    onNavigateToTrends = { currentScreen = Screen.STORIES },
                                    onNavigateToStories = { currentScreen = Screen.STORIES }
                                )

                                Screen.EXPLORE -> ExploreScreen(
                                    uiState = uiState,
                                    viewModel = viewModel
                                )

                                Screen.CALCULATOR -> CalculatorScreen(
                                    uiState = uiState,
                                    viewModel = viewModel
                                )

                                Screen.STORIES -> SuccessStoriesScreen(
                                    uiState = uiState,
                                    viewModel = viewModel
                                )

                                Screen.LAUNCHPAD -> LaunchpadScreen(
                                    uiState = uiState,
                                    viewModel = viewModel
                                )
                            }
                        }

                        // Detail BottomSheet for selected Idea
                        uiState.selectedDetailIdea?.let { idea ->
                            IdeaDetailSheet(
                                idea = idea,
                                isSaved = uiState.savedIdeaIds.contains(idea.id),
                                isCompared = uiState.comparedIdeaIds.contains(idea.id),
                                onDismiss = { viewModel.selectDetailIdea(null) },
                                onToggleSave = { viewModel.toggleSaveIdea(idea.id) },
                                onToggleCompare = { viewModel.toggleCompareIdea(idea.id) },
                                onOpenCalculator = {
                                    viewModel.loadIdeaIntoCalculator(idea)
                                    currentScreen = Screen.CALCULATOR
                                },
                                onBuildBusinessPlan = {
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
