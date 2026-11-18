package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BusinessIdea
import com.example.model.CapitalTier
import com.example.model.SkillCategory
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.BiasharaUiState
import com.example.viewmodel.BiasharaViewModel

enum class IdeaFilterTag(val label: String) {
    ALL("All Ideas"),
    POPULAR("Popular"),
    LOW_CAPITAL("Low Capital"),
    HIGH_GROWTH("High Growth")
}

@Composable
fun BrowseIdeasScreen(
    uiState: BiasharaUiState,
    viewModel: BiasharaViewModel,
    initialCategory: SkillCategory? = null,
    onCategoryChanged: (SkillCategory?) -> Unit,
    onBack: () -> Unit,
    onSelectIdea: (BusinessIdea) -> Unit
) {
    var searchQuery by remember {
        mutableStateOf(uiState.searchQuery)
    }

    var activeFilter by remember {
        mutableStateOf(IdeaFilterTag.ALL)
    }

    var selectedCategory by remember {
        mutableStateOf(initialCategory)
    }

    // Keep the category selection synchronized when a new category
    // is passed from the dashboard.
    LaunchedEffect(initialCategory) {
        selectedCategory = initialCategory
    }

    val filteredIdeas = remember(
        uiState.allIdeas,
        searchQuery,
        activeFilter,
        selectedCategory
    ) {
        uiState.allIdeas.filter { idea ->

            val matchesSearch =
                searchQuery.isBlank() ||
                idea.title.contains(searchQuery, ignoreCase = true) ||
                idea.summary.contains(searchQuery, ignoreCase = true) ||
                idea.category.displayName.contains(searchQuery, ignoreCase = true)

            val matchesCategory =
                selectedCategory == null ||
                idea.category == selectedCategory

            val matchesTag = when (activeFilter) {

                IdeaFilterTag.ALL -> true

                IdeaFilterTag.POPULAR ->
                    idea.rating >= 4.4 ||
                    idea.reviewCount >= 85

                IdeaFilterTag.LOW_CAPITAL ->
                    idea.capitalTier == CapitalTier.MICRO ||
                    idea.capitalTier == CapitalTier.LOW

                IdeaFilterTag.HIGH_GROWTH ->
                    idea.capitalTier == CapitalTier.EXPANDED ||
                    idea.estimatedMonthlyProfitKes.last >= 70000
            }

            matchesSearch && matchesCategory && matchesTag
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCream),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 12.dp,
            bottom = 110.dp
        ),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {

        // ---------------------------------------------------------
        // HEADER
        // ---------------------------------------------------------

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    modifier = Modifier
                        .size(44.dp)
                        .clickable { onBack() }
                        .testTag("browse_ideas_back_button"),
                    shape = RoundedCornerShape(14.dp),
                    color = SurfaceCard,
                    border = BorderStroke(1.dp, SurfaceBorder)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go back",
                            tint = TextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Business Ideas",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = "Discover opportunities. Build your future.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Surface(
                    modifier = Modifier
                        .size(44.dp)
                        .clickable {
                            searchQuery = ""
                            viewModel.setSearchQuery("")
                            selectedCategory = null
                            onCategoryChanged(null)
                            activeFilter = IdeaFilterTag.ALL
                        }
                        .testTag("browse_ideas_filter_icon"),
                    shape = RoundedCornerShape(14.dp),
                    color = if (
                        selectedCategory != null ||
                        searchQuery.isNotBlank() ||
                        activeFilter != IdeaFilterTag.ALL
                    ) SciRedContainer else SurfaceCard,
                    border = BorderStroke(
                        1.dp,
                        if (
                            selectedCategory != null ||
                            searchQuery.isNotBlank() ||
                            activeFilter != IdeaFilterTag.ALL
                        ) SciRedAccent.copy(alpha = 0.25f)
                        else SurfaceBorder
                    )
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Reset filters",
                            tint = if (
                                selectedCategory != null ||
                                searchQuery.isNotBlank() ||
                                activeFilter != IdeaFilterTag.ALL
                            ) SciRedAccent else TextPrimary
                        )
                    }
                }
            }
        }

        // ---------------------------------------------------------
        // INTRODUCTION BANNER
        // ---------------------------------------------------------

        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(22.dp),
                color = SciBluePrimary
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.13f)
                    ) {
                        Text(
                            text = "SCI IDEA DISCOVERY",
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 6.dp
                            ),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Your next big idea starts here.",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Explore business opportunities that match your interests, resources and ambitions.",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.82f),
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(17.dp)
                        )

                        Spacer(modifier = Modifier.width(7.dp))

                        Text(
                            text = "${uiState.allIdeas.size} opportunities available",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // ---------------------------------------------------------
        // SEARCH
        // ---------------------------------------------------------

        item {
            Text(
                text = "Find your opportunity",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.setSearchQuery(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("browse_search_input"),
                placeholder = {
                    Text(
                        text = "Search business ideas...",
                        color = TextMuted,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = SciBluePrimary
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
                                contentDescription = "Clear search",
                                tint = TextSecondary
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceCard,
                    unfocusedContainerColor = SurfaceCard,
                    focusedBorderColor = SciRedAccent,
                    unfocusedBorderColor = SurfaceBorder,
                    cursorColor = SciRedAccent
                )
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        // ---------------------------------------------------------
        // FILTERS
        // ---------------------------------------------------------

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Explore by preference",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = null,
                    tint = SciBluePrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(9.dp),
                contentPadding = PaddingValues(end = 4.dp)
            ) {
                items(IdeaFilterTag.values()) { tag ->

                    val selected = activeFilter == tag

                    Surface(
                        modifier = Modifier
                            .clickable {
                                activeFilter = tag
                            }
                            .testTag("filter_chip_${tag.name.lowercase()}"),
                        shape = RoundedCornerShape(12.dp),
                        color = if (selected) SciRedAccent else SurfaceCard,
                        border = BorderStroke(
                            1.dp,
                            if (selected) SciRedAccent else SurfaceBorder
                        )
                    ) {
                        Text(
                            text = tag.label,
                            modifier = Modifier.padding(
                                horizontal = 15.dp,
                                vertical = 10.dp
                            ),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (selected) Color.White else TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // ---------------------------------------------------------
        // ACTIVE CATEGORY
        // ---------------------------------------------------------

        if (selectedCategory != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SciRedContainer
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 9.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Category: ${selectedCategory!!.displayName}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SciRedDark
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear category",
                            tint = SciRedDark,
                            modifier = Modifier
                                .size(17.dp)
                                .clickable {
                                    selectedCategory = null
                                    onCategoryChanged(null)
                                }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        // ---------------------------------------------------------
        // RESULTS HEADER
        // ---------------------------------------------------------

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Available opportunities",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = "Ideas worth exploring",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SciBlueContainer
                ) {
                    Text(
                        text = "${filteredIdeas.size} found",
                        modifier = Modifier.padding(
                            horizontal = 11.dp,
                            vertical = 7.dp
                        ),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SciBluePrimary
                    )
                }
            }
        }

        // ---------------------------------------------------------
        // EMPTY STATE
        // ---------------------------------------------------------

        if (filteredIdeas.isEmpty()) {

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceCard,
                    border = BorderStroke(1.dp, SurfaceBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = SciRedContainer,
                            modifier = Modifier.size(64.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.SearchOff,
                                    contentDescription = null,
                                    tint = SciRedAccent,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "No matching ideas found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Try another search term or remove some filters to discover more opportunities.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                searchQuery = ""
                                viewModel.setSearchQuery("")
                                selectedCategory = null
                                onCategoryChanged(null)
                                activeFilter = IdeaFilterTag.ALL
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SciRedAccent
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Clear all filters",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

        } else {

            // -----------------------------------------------------
            // BUSINESS IDEA RESULTS
            // -----------------------------------------------------

            items(
                items = filteredIdeas,
                key = { it.id }
            ) { idea ->

                FeaturedIdeaRowCard(
                    idea = idea,
                    isSaved = uiState.savedIdeaIds.contains(idea.id),
                    onSelect = {
                        onSelectIdea(idea)
                    },
                    onToggleSave = {
                        viewModel.toggleSaveIdea(idea.id)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // ---------------------------------------------------------
        // FOOTER
        // ---------------------------------------------------------

        item {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "SCI IDEA • Discover. Plan. Build.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                fontSize = 11.sp,
                color = TextMuted,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}