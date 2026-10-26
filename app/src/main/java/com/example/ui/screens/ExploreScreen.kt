package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BusinessModelType
import com.example.model.CapitalTier
import com.example.model.SkillCategory
import com.example.ui.components.DeveloperFooter
import com.example.ui.theme.*
import com.example.viewmodel.BiasharaUiState
import com.example.viewmodel.BiasharaViewModel

@Composable
fun ExploreScreen(
    uiState: BiasharaUiState,
    viewModel: BiasharaViewModel,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf(uiState.searchQuery) }
    var selectedTierFilter by remember { mutableStateOf<CapitalTier?>(null) }
    var selectedModelFilter by remember { mutableStateOf<BusinessModelType?>(null) }
    var sortBy by remember { mutableStateOf(0) } // 0 = Default, 1 = Lowest Capital, 2 = Highest Profit

    val filteredIdeas = remember(
        searchQuery,
        uiState.categoryFilter,
        selectedTierFilter,
        selectedModelFilter,
        sortBy,
        uiState.allIdeas
    ) {
        val list = uiState.allIdeas.filter { idea ->
            val matchesQuery = searchQuery.isBlank() ||
                    idea.title.contains(searchQuery, ignoreCase = true) ||
                    idea.shengTitle.contains(searchQuery, ignoreCase = true) ||
                    idea.summary.contains(searchQuery, ignoreCase = true) ||
                    idea.matchedSkills.any { it.contains(searchQuery, ignoreCase = true) }

            val matchesCat = uiState.categoryFilter == null || idea.category == uiState.categoryFilter
            val matchesTier = selectedTierFilter == null || idea.capitalTier == selectedTierFilter || idea.capitalMaxKes <= selectedTierFilter!!.maxKes
            val matchesModel = selectedModelFilter == null || idea.businessModel == selectedModelFilter

            matchesQuery && matchesCat && matchesTier && matchesModel
        }

        when (sortBy) {
            1 -> list.sortedBy { it.capitalMinKes }
            2 -> list.sortedByDescending { it.estimatedMonthlyProfitKes.last }
            else -> list
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("explore_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Search & Filter Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceCard)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = SciRedAccent,
                                modifier = Modifier.padding(end = 6.dp)
                            ) {
                                Text(
                                    text = "SCI BASE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "Biashara Catalogue",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SciBluePrimary
                                )
                            )
                        }
                        Text(
                            text = "Browse ${uiState.allIdeas.size} verified Kenyan business opportunities offline",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }

                    if (uiState.comparedIdeaIds.isNotEmpty()) {
                        IconButton(onClick = { viewModel.openCompareSheet() }) {
                            Icon(imageVector = Icons.Default.CompareArrows, contentDescription = "Compare", tint = KenyaGoldDark)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.setSearchQuery(it)
                    },
                    placeholder = { Text("Search by name, skill, or town (e.g. kuku, boda, canva)") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = SciBluePrimary)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                searchQuery = ""
                                viewModel.setSearchQuery("")
                            }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("explore_search_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category Tabs horizontal scroll
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = uiState.categoryFilter == null,
                        onClick = { viewModel.setCategoryFilter(null) },
                        label = { Text("All Categories") }
                    )
                    SkillCategory.values().forEach { cat ->
                        FilterChip(
                            selected = uiState.categoryFilter == cat,
                            onClick = { viewModel.setCategoryFilter(cat) },
                            label = { Text(cat.displayName) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Secondary filters: Capital & Sort
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Budget:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    CapitalTier.values().forEach { tier ->
                        FilterChip(
                            selected = selectedTierFilter == tier,
                            onClick = { selectedTierFilter = if (selectedTierFilter == tier) null else tier },
                            label = { Text(tier.label, fontSize = 11.sp) }
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sort:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    FilterChip(
                        selected = sortBy == 1,
                        onClick = { sortBy = if (sortBy == 1) 0 else 1 },
                        label = { Text("Lowest Capital", fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = sortBy == 2,
                        onClick = { sortBy = if (sortBy == 2) 0 else 2 },
                        label = { Text("Highest Profit", fontSize = 11.sp) }
                    )
                }
            }
        }

        // Result count
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Showing ${filteredIdeas.size} verified ventures",
                    style = MaterialTheme.typography.labelMedium.copy(color = TextSecondary)
                )
            }
        }

        // Ideas list
        items(filteredIdeas) { idea ->
            EnhancedBusinessIdeaCard(
                idea = idea,
                isSaved = uiState.savedIdeaIds.contains(idea.id),
                isCompared = uiState.comparedIdeaIds.contains(idea.id),
                onCardClick = { viewModel.selectDetailIdea(idea) },
                onToggleSave = { viewModel.toggleSaveIdea(idea.id) },
                onToggleCompare = { viewModel.toggleCompareIdea(idea.id) },
                onLoadIntoCalculator = { viewModel.loadIdeaIntoCalculator(idea) },
                onStartJourney = { viewModel.openJourneyForIdea(idea) },
                onBuildBusinessPlan = { viewModel.openBusinessPlanDraft(idea) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // Developer Footer
        item {
            DeveloperFooter()
        }
    }
}
