package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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

@Composable
fun SavedIdeasScreen(
    uiState: BiasharaUiState,
    viewModel: BiasharaViewModel,
    onBack: () -> Unit,
    onSelectIdea: (BusinessIdea) -> Unit,
    onNavigateToExplore: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Ideas, 1 = Guides

    val savedIdeas = remember(uiState.allIdeas, uiState.savedIdeaIds) {
        uiState.allIdeas.filter { uiState.savedIdeaIds.contains(it.id) }
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("saved_back_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF111827)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Saved Ideas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Segmented Control: Ideas | Guides
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFFE5E7EB)
            ) {
                Row(modifier = Modifier.fillMaxSize().padding(4.dp)) {
                    // Ideas Tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { selectedTab = 0 }
                            .background(
                                color = if (selectedTab == 0) KenyanGreen else Color.Transparent,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .testTag("saved_tab_ideas"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ideas (${savedIdeas.size})",
                            fontSize = 14.sp,
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTab == 0) Color.White else Color(0xFF4B5563)
                        )
                    }

                    // Guides Tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { selectedTab = 1 }
                            .background(
                                color = if (selectedTab == 1) KenyanGreen else Color.Transparent,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .testTag("saved_tab_guides"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Guides (${uiState.documentLibrary.size})",
                            fontSize = 14.sp,
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTab == 1) Color.White else Color(0xFF4B5563)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }

        if (selectedTab == 0) {
            // Ideas Tab Content
            if (savedIdeas.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Outlined.BookmarkBorder,
                                contentDescription = null,
                                tint = Color(0xFF9CA3AF),
                                modifier = Modifier.size(52.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "You haven't saved any ideas yet",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF374151)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Browse opportunities and tap the bookmark icon to save them here.",
                                fontSize = 13.sp,
                                color = Color(0xFF6B7280),
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                            Spacer(modifier = Modifier.height(18.dp))
                            Button(
                                onClick = onNavigateToExplore,
                                colors = ButtonDefaults.buttonColors(containerColor = KenyanGreen),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.testTag("saved_empty_explore_button")
                            ) {
                                Text("Explore Opportunities", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                items(savedIdeas) { idea ->
                    SavedIdeaCard(
                        idea = idea,
                        onClick = { onSelectIdea(idea) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        } else {
            // Guides Tab Content
            items(uiState.documentLibrary) { doc ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Description, contentDescription = null, tint = KenyanGreen)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(doc.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF111827))
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(doc.category.name.replace("_", " "), fontSize = 12.sp, color = Color(0xFF6B7280))
                        }
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF9CA3AF))
                    }
                }
            }
        }
    }
}

@Composable
fun SavedIdeaCard(
    idea: BusinessIdea,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("saved_item_${idea.id}"),
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

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = idea.category.displayName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0284C7)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = idea.capitalTier.label,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
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
