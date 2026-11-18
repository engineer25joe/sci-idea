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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BusinessIdea
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.BiasharaUiState
import com.example.viewmodel.BiasharaViewModel

enum class SearchTab(val label: String) {
    ALL("All"),
    IDEAS("Ideas"),
    GUIDES("Guides"),
    RESOURCES("Resources")
}

@Composable
fun SearchResultsScreen(
    uiState: BiasharaUiState,
    viewModel: BiasharaViewModel,
    onBack: () -> Unit,
    onSelectIdea: (BusinessIdea) -> Unit
) {
    var searchQuery by remember {
        mutableStateOf(uiState.searchQuery)
    }

    var activeTab by remember {
        mutableStateOf(SearchTab.ALL)
    }

    // Synchronize this screen with the shared ViewModel search state.
    LaunchedEffect(uiState.searchQuery) {
        if (searchQuery != uiState.searchQuery) {
            searchQuery = uiState.searchQuery
        }
    }

    val matchingIdeas = remember(
        uiState.allIdeas,
        searchQuery
    ) {
        if (searchQuery.isBlank()) {
            uiState.allIdeas
        } else {
            uiState.allIdeas.filter { idea ->
                idea.title.contains(searchQuery, ignoreCase = true) ||
                idea.summary.contains(searchQuery, ignoreCase = true) ||
                idea.category.displayName.contains(searchQuery, ignoreCase = true) ||
                idea.targetCustomers.any {
                    it.contains(searchQuery, ignoreCase = true)
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCream),
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 16.dp
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
                    .padding(bottom = 22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    modifier = Modifier
                        .size(44.dp)
                        .clickable { onBack() }
                        .testTag("search_back_button"),
                    shape = RoundedCornerShape(14.dp),
                    color = SurfaceCard,
                    border = BorderStroke(1.dp, SurfaceBorder)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "Search",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = "Find ideas and opportunities",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // ---------------------------------------------------------
        // SEARCH HERO
        // ---------------------------------------------------------

        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(20.dp),
                color = SciBluePrimary
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "WHAT ARE YOU LOOKING FOR?",
                        fontSize = 10.sp,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.75f)
                    )

                    Spacer(modifier = Modifier.height(9.dp))

                    Text(
                        text = "Discover your next opportunity.",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Search business ideas by name, category, description or target customers.",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.82f),
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // ---------------------------------------------------------
        // SEARCH INPUT
        // ---------------------------------------------------------

        item {
            Text(
                text = "Search opportunities",
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
                    .testTag("search_query_input"),
                placeholder = {
                    Text(
                        text = "Try food business, technology...",
                        color = TextMuted,
                        fontSize = 13.sp
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
        // SEARCH CATEGORIES
        // ---------------------------------------------------------

        item {
            Text(
                text = "Browse categories",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(11.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                items(SearchTab.values()) { tab ->

                    val selected = activeTab == tab

                    Surface(
                        modifier = Modifier
                            .clickable {
                                activeTab = tab
                            }
                            .testTag("search_tab_${tab.name.lowercase()}"),
                        shape = RoundedCornerShape(12.dp),
                        color = if (selected) SciRedAccent else SurfaceCard,
                        border = BorderStroke(
                            1.dp,
                            if (selected) SciRedAccent else SurfaceBorder
                        )
                    ) {
                        Text(
                            text = tab.label,
                            modifier = Modifier.padding(
                                horizontal = 17.dp,
                                vertical = 10.dp
                            ),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (selected) Color.White else TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
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
                        text = "Search results",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = if (searchQuery.isBlank()) {
                            "Explore available business ideas"
                        } else {
                            "Results for \"$searchQuery\""
                        },
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SciBlueContainer
                ) {
                    Text(
                        text = "${matchingIdeas.size} found",
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
        // EMPTY RESULTS
        // ---------------------------------------------------------

        if (matchingIdeas.isEmpty()) {

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
                            modifier = Modifier.size(66.dp),
                            shape = RoundedCornerShape(18.dp),
                            color = SciRedContainer
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.SearchOff,
                                    contentDescription = null,
                                    tint = SciRedAccent,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        Text(
                            text = "No matching results",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(7.dp))

                        Text(
                            text = "We couldn't find business ideas matching your search. Try different keywords.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                searchQuery = ""
                                viewModel.setSearchQuery("")
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SciRedAccent
                            )
                        ) {
                            Text("Clear search")
                        }
                    }
                }
            }

        } else {

            // -----------------------------------------------------
            // MATCHING IDEAS
            // -----------------------------------------------------

            items(
                items = matchingIdeas,
                key = { it.id }
            ) { idea ->

                SearchIdeaResultCard(
                    idea = idea,
                    onClick = {
                        onSelectIdea(idea)
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

// =================================================================
// SEARCH RESULT CARD
// =================================================================

@Composable
fun SearchIdeaResultCard(
    idea: BusinessIdea,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("search_result_${idea.id}"),
        shape = RoundedCornerShape(18.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, SurfaceBorder),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IdeaCardThumbnail(
                type = idea.thumbnailType,
                modifier = Modifier.size(76.dp)
            )

            Spacer(modifier = Modifier.width(13.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = idea.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(7.dp))

                Surface(
                    shape = RoundedCornerShape(7.dp),
                    color = SciBlueContainer
                ) {
                    Text(
                        text = idea.category.displayName,
                        color = SciBluePrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = idea.capitalTier.label,
                    fontSize = 11.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = KenyaGold,
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${idea.rating} (${idea.reviewCount}+ reviews)",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(5.dp))

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View idea details",
                tint = SciRedAccent,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}