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
import androidx.compose.ui.graphics.Brush
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
    var sortBy by remember { mutableIntStateOf(0) }

    // Keep local search synchronized with searches initiated elsewhere.
    LaunchedEffect(uiState.searchQuery) {
        if (searchQuery != uiState.searchQuery) {
            searchQuery = uiState.searchQuery
        }
    }

    val filteredIdeas = remember(
        searchQuery,
        uiState.categoryFilter,
        selectedTierFilter,
        selectedModelFilter,
        sortBy,
        uiState.allIdeas
    ) {
        val tierFilter = selectedTierFilter

        val filtered = uiState.allIdeas.filter { idea ->

            val matchesQuery = searchQuery.isBlank() ||
                    idea.title.contains(searchQuery, ignoreCase = true) ||
                    idea.shengTitle.contains(searchQuery, ignoreCase = true) ||
                    idea.summary.contains(searchQuery, ignoreCase = true) ||
                    idea.matchedSkills.any {
                        it.contains(searchQuery, ignoreCase = true)
                    }

            val matchesCategory =
                uiState.categoryFilter == null ||
                        idea.category == uiState.categoryFilter

            val matchesTier =
                tierFilter == null ||
                        idea.capitalTier == tierFilter ||
                        idea.capitalMaxKes <= tierFilter.maxKes

            val matchesModel =
                selectedModelFilter == null ||
                        idea.businessModel == selectedModelFilter

            matchesQuery &&
                    matchesCategory &&
                    matchesTier &&
                    matchesModel
        }

        when (sortBy) {
            1 -> filtered.sortedBy { it.capitalMinKes }
            2 -> filtered.sortedByDescending {
                it.estimatedMonthlyProfitKes.last
            }
            else -> filtered
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceCream)
            .testTag("explore_screen"),
        contentPadding = PaddingValues(bottom = 104.dp)
    ) {

        // -------------------------------------------------
        // HEADER
        // -------------------------------------------------

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceCard)
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            color = SciRedContainer,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "SCI BASE COLLECTIONS",
                                modifier = Modifier.padding(
                                    horizontal = 9.dp,
                                    vertical = 5.dp
                                ),
                                color = SciRedDark,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Explore Businesses",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Discover opportunities. Build your future.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    if (uiState.comparedIdeaIds.isNotEmpty()) {
                        FilledTonalIconButton(
                            onClick = { viewModel.openCompareSheet() },
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = SciBlueContainer,
                                contentColor = SciBluePrimary
                            ),
                            modifier = Modifier.testTag("explore_compare_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CompareArrows,
                                contentDescription = "Compare selected businesses"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Catalogue summary banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SciBluePrimary
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(17.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "${uiState.allIdeas.size}",
                                color = Color.White,
                                fontSize = 29.sp,
                                fontWeight = FontWeight.ExtraBold
                            )

                            Text(
                                text = "Business opportunities",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = "Explore ideas that match your resources and ambitions.",
                                color = Color.White.copy(alpha = 0.82f),
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White.copy(alpha = 0.13f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .padding(15.dp)
                                    .size(34.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(17.dp))

                // Search field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.setSearchQuery(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("explore_search_input"),
                    placeholder = {
                        Text(
                            "Search businesses, skills or ideas...",
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = SciRedAccent
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    searchQuery = ""
                                    viewModel.setSearchQuery("")
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear search"
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SciRedAccent,
                        unfocusedBorderColor = SurfaceBorder,
                        focusedContainerColor = SurfaceCard,
                        unfocusedContainerColor = SurfaceCard,
                        cursorColor = SciRedAccent
                    )
                )
            }
        }

        // -------------------------------------------------
        // CATEGORY FILTERS
        // -------------------------------------------------

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 17.dp)
            ) {
                ExploreSectionHeading(
                    title = "Business categories",
                    subtitle = "Choose an area that interests you."
                )

                Spacer(modifier = Modifier.height(11.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExploreFilterChip(
                        label = "All categories",
                        selected = uiState.categoryFilter == null,
                        onClick = {
                            viewModel.setCategoryFilter(null)
                        }
                    )

                    SkillCategory.values().forEach { category ->
                        ExploreFilterChip(
                            label = category.displayName,
                            selected = uiState.categoryFilter == category,
                            onClick = {
                                viewModel.setCategoryFilter(category)
                            }
                        )
                    }
                }
            }
        }

        // -------------------------------------------------
        // BUDGET FILTERS
        // -------------------------------------------------

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp)
            ) {
                ExploreSectionHeading(
                    title = "Your starting budget",
                    subtitle = "Find opportunities within your available capital."
                )

                Spacer(modifier = Modifier.height(11.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExploreFilterChip(
                        label = "Any budget",
                        selected = selectedTierFilter == null,
                        onClick = {
                            selectedTierFilter = null
                        }
                    )

                    CapitalTier.values().forEach { tier ->
                        ExploreFilterChip(
                            label = tier.label,
                            selected = selectedTierFilter == tier,
                            onClick = {
                                selectedTierFilter =
                                    if (selectedTierFilter == tier) {
                                        null
                                    } else {
                                        tier
                                    }
                            }
                        )
                    }
                }
            }
        }

        // -------------------------------------------------
        // BUSINESS MODEL FILTER
        // -------------------------------------------------

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp)
            ) {
                ExploreSectionHeading(
                    title = "Business model",
                    subtitle = "Narrow down the type of opportunity."
                )

                Spacer(modifier = Modifier.height(11.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExploreFilterChip(
                        label = "All models",
                        selected = selectedModelFilter == null,
                        onClick = {
                            selectedModelFilter = null
                        }
                    )

                    BusinessModelType.values().forEach { model ->
                        ExploreFilterChip(
                            label = model.name
                                .replace("_", " ")
                                .lowercase()
                                .replaceFirstChar { it.uppercase() },
                            selected = selectedModelFilter == model,
                            onClick = {
                                selectedModelFilter =
                                    if (selectedModelFilter == model) {
                                        null
                                    } else {
                                        model
                                    }
                            }
                        )
                    }
                }
            }
        }

        // -------------------------------------------------
        // RESULTS HEADER AND SORTING
        // -------------------------------------------------

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 26.dp, bottom = 10.dp)
            ) {
                HorizontalDivider(color = SurfaceBorder)

                Spacer(modifier = Modifier.height(17.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Available opportunities",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${filteredIdeas.size} businesses found",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    Surface(
                        color = SciRedContainer,
                        shape = RoundedCornerShape(9.dp)
                    ) {
                        Text(
                            text = "${filteredIdeas.size} results",
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 7.dp
                            ),
                            color = SciRedDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(13.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = null,
                        tint = SciBluePrimary,
                        modifier = Modifier.size(18.dp)
                    )

                    ExploreFilterChip(
                        label = "Recommended",
                        selected = sortBy == 0,
                        onClick = {
                            sortBy = 0
                        }
                    )

                    ExploreFilterChip(
                        label = "Lowest capital",
                        selected = sortBy == 1,
                        onClick = {
                            sortBy = if (sortBy == 1) 0 else 1
                        }
                    )

                    ExploreFilterChip(
                        label = "Highest profit",
                        selected = sortBy == 2,
                        onClick = {
                            sortBy = if (sortBy == 2) 0 else 2
                        }
                    )
                }
            }
        }

        // -------------------------------------------------
        // EMPTY STATE
        // -------------------------------------------------

        if (filteredIdeas.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SurfaceCard
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(26.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = SciBlueContainer
                        ) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                tint = SciBluePrimary,
                                modifier = Modifier
                                    .padding(17.dp)
                                    .size(35.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        Text(
                            text = "No businesses found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(7.dp))

                        Text(
                            text = "Try changing your search or removing some filters to discover more opportunities.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(17.dp))

                        Button(
                            onClick = {
                                searchQuery = ""
                                viewModel.setSearchQuery("")
                                viewModel.setCategoryFilter(null)
                                selectedTierFilter = null
                                selectedModelFilter = null
                                sortBy = 0
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SciRedAccent
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterAltOff,
                                contentDescription = null,
                                modifier = Modifier.size(17.dp)
                            )

                            Spacer(modifier = Modifier.width(7.dp))

                            Text("Clear all filters")
                        }
                    }
                }
            }
        }

        // -------------------------------------------------
        // BUSINESS IDEA CARDS
        // -------------------------------------------------

        items(
            items = filteredIdeas,
            key = { it.id }
        ) { idea ->

            EnhancedBusinessIdeaCard(
                idea = idea,
                isSaved = uiState.savedIdeaIds.contains(idea.id),
                isCompared = uiState.comparedIdeaIds.contains(idea.id),
                onCardClick = {
                    viewModel.selectDetailIdea(idea)
                },
                onToggleSave = {
                    viewModel.toggleSaveIdea(idea.id)
                },
                onToggleCompare = {
                    viewModel.toggleCompareIdea(idea.id)
                },
                onLoadIntoCalculator = {
                    viewModel.loadIdeaIntoCalculator(idea)
                },
                onStartJourney = {
                    viewModel.openJourneyForIdea(idea)
                },
                onBuildBusinessPlan = {
                    viewModel.openBusinessPlanDraft(idea)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 7.dp)
            )
        }

        // -------------------------------------------------
        // FOOTER
        // -------------------------------------------------

        item {
            Spacer(modifier = Modifier.height(15.dp))
            DeveloperFooter()
        }
    }
}

// -----------------------------------------------------
// REUSABLE EXPLORE SECTION HEADING
// -----------------------------------------------------

@Composable
private fun ExploreSectionHeading(
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

// -----------------------------------------------------
// REUSABLE FILTER CHIP
// -----------------------------------------------------

@Composable
private fun ExploreFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (selected) {
                    FontWeight.Bold
                } else {
                    FontWeight.Medium
                }
            )
        },
        shape = RoundedCornerShape(11.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = SciRedAccent,
            selectedLabelColor = Color.White,
            containerColor = SurfaceCard,
            labelColor = TextSecondary
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = SurfaceBorder,
            selectedBorderColor = SciRedAccent
        )
    )
}