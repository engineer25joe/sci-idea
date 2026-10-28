package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.model.BusinessIdea
import com.example.ui.components.*
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
    var searchQuery by remember { mutableStateOf("food business") }
    var activeTab by remember { mutableStateOf(SearchTab.ALL) }

    val matchingIdeas = remember(uiState.allIdeas, searchQuery, activeTab) {
        if (searchQuery.isBlank()) {
            uiState.allIdeas
        } else {
            uiState.allIdeas.filter { idea ->
                idea.title.contains(searchQuery, ignoreCase = true) ||
                idea.summary.contains(searchQuery, ignoreCase = true) ||
                idea.category.displayName.contains(searchQuery, ignoreCase = true) ||
                idea.targetCustomers.any { it.contains(searchQuery, ignoreCase = true) }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
    ) {
        // Top Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("search_back_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF111827)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Search",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        // Search Input Field
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search business ideas, guides...", color = Color(0xFF9CA3AF)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color(0xFF9CA3AF))
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = Color(0xFF6B7280))
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_query_input"),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = KenyanGreen,
                    unfocusedBorderColor = Color(0xFFE5E7EB)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Filter Tabs: All, Ideas, Guides, Resources
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(SearchTab.values()) { tab ->
                    val isSelected = activeTab == tab
                    Surface(
                        modifier = Modifier
                            .clickable { activeTab = tab }
                            .testTag("search_tab_${tab.name.lowercase()}"),
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) KenyanGreen else Color.White,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) KenyanGreen else Color(0xFFE5E7EB)
                        )
                    ) {
                        Text(
                            text = tab.label,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF374151),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Matching Idea Results
        if (matchingIdeas.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.SearchOff, contentDescription = null, tint = Color(0xFF9CA3AF), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No matching results found for \"$searchQuery\"", fontSize = 14.sp, color = Color(0xFF6B7280))
                    }
                }
            }
        } else {
            items(matchingIdeas) { idea ->
                SearchIdeaResultCard(
                    idea = idea,
                    onClick = { onSelectIdea(idea) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

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
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IdeaCardThumbnail(
                type = idea.thumbnailType,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = idea.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(3.dp))

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFE0F2FE)
                ) {
                    Text(
                        text = idea.category.displayName,
                        color = Color(0xFF0284C7),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = idea.capitalTier.label,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = KenyanGold,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${idea.rating} (${idea.reviewCount}+)",
                        fontSize = 11.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Details",
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
