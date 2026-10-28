package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.viewmodel.BiasharaUiState
import com.example.viewmodel.BiasharaViewModel

enum class IdeaFilterTag(val label: String) {
    ALL("All"),
    POPULAR("Popular"),
    LOW_CAPITAL("Low Capital"),
    HIGH_GROWTH("High Growth")
}

@Composable
fun BrowseIdeasScreen(
    uiState: BiasharaUiState,
    viewModel: BiasharaViewModel,
    initialCategory: SkillCategory? = null,
    onBack: () -> Unit,
    onSelectIdea: (BusinessIdea) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var activeFilter by remember { mutableStateOf(IdeaFilterTag.ALL) }
    var selectedCategory by remember { mutableStateOf(initialCategory) }

    val filteredIdeas = remember(uiState.allIdeas, searchQuery, activeFilter, selectedCategory) {
        uiState.allIdeas.filter { idea ->
            val matchesSearch = searchQuery.isBlank() ||
                    idea.title.contains(searchQuery, ignoreCase = true) ||
                    idea.summary.contains(searchQuery, ignoreCase = true) ||
                    idea.category.displayName.contains(searchQuery, ignoreCase = true)

            val matchesCategory = selectedCategory == null || idea.category == selectedCategory

            val matchesTag = when (activeFilter) {
                IdeaFilterTag.ALL -> true
                IdeaFilterTag.POPULAR -> idea.rating >= 4.4 || idea.reviewCount >= 85
                IdeaFilterTag.LOW_CAPITAL -> idea.capitalTier == CapitalTier.MICRO || idea.capitalTier == CapitalTier.LOW
                IdeaFilterTag.HIGH_GROWTH -> idea.capitalTier == CapitalTier.EXPANDED || idea.estimatedMonthlyProfitKes.last >= 70000
            }

            matchesSearch && matchesCategory && matchesTag
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("browse_ideas_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF111827)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Business Ideas",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                }

                IconButton(
                    onClick = {
                        // Toggle category clear or filter sheet
                        if (selectedCategory != null) selectedCategory = null
                    },
                    modifier = Modifier.testTag("browse_ideas_filter_icon")
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Filter",
                        tint = if (selectedCategory != null) KenyanGreen else Color(0xFF111827)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        // Search Input
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search ideas...", color = Color(0xFF9CA3AF), fontSize = 14.sp) },
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
                    .testTag("browse_search_input"),
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

        // Filter Pills: All, Popular, Low Capital, High Growth
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(IdeaFilterTag.values()) { tag ->
                    val isSelected = activeFilter == tag
                    Surface(
                        modifier = Modifier
                            .clickable { activeFilter = tag }
                            .testTag("filter_chip_${tag.name.lowercase()}"),
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) KenyanGreen else Color.White,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) KenyanGreen else Color(0xFFE5E7EB)
                        )
                    ) {
                        Text(
                            text = tag.label,
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

        // Selected Category pill if active
        if (selectedCategory != null) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filtered by: ",
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280)
                    )
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE8F5E9)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedCategory!!.displayName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = KenyanGreen
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear Category",
                                tint = KenyanGreen,
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable { selectedCategory = null }
                            )
                        }
                    }
                }
            }
        }

        // Ideas List
        if (filteredIdeas.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No matching business ideas found",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF6B7280)
                        )
                    }
                }
            }
        } else {
            items(filteredIdeas) { idea ->
                FeaturedIdeaRowCard(
                    idea = idea,
                    isSaved = uiState.savedIdeaIds.contains(idea.id),
                    onSelect = { onSelectIdea(idea) },
                    onToggleSave = { viewModel.toggleSaveIdea(idea.id) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
