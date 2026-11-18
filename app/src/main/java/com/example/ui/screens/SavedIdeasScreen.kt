package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BusinessIdea
import com.example.ui.components.*
import com.example.ui.theme.*
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
    var selectedTab by remember { mutableIntStateOf(0) }

    val savedIdeas = remember(uiState.allIdeas, uiState.savedIdeaIds) {
        uiState.allIdeas.filter { it.id in uiState.savedIdeaIds }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCream)
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = 110.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("saved_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }

                    Spacer(Modifier.width(6.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "My Collection",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Text(
                            text = "Your saved business resources",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = SciRedContainer
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmarks,
                            contentDescription = "Saved collection",
                            tint = SciRedAccent,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                Spacer(Modifier.height(18.dp))

                // Collection summary
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = SciBluePrimary
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "BUILD YOUR FUTURE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp,
                                color = Color.White.copy(alpha = 0.75f)
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                text = "Ideas worth keeping.",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = "Keep track of opportunities you want to explore.",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.82f)
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White.copy(alpha = 0.13f)
                        ) {
                            Column(
                                modifier = Modifier.padding(
                                    horizontal = 17.dp,
                                    vertical = 12.dp
                                ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = savedIdeas.size.toString(),
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Text(
                                    text = "IDEAS",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Tabs
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFE9EBF1)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                ) {
                    SavedCollectionTab(
                        title = "Ideas (${savedIdeas.size})",
                        selected = selectedTab == 0,
                        icon = Icons.Default.Lightbulb,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("saved_tab_ideas"),
                        onClick = { selectedTab = 0 }
                    )

                    SavedCollectionTab(
                        title = "Guides (${uiState.documentLibrary.size})",
                        selected = selectedTab == 1,
                        icon = Icons.Default.MenuBook,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("saved_tab_guides"),
                        onClick = { selectedTab = 1 }
                    )
                }
            }
        }

        // Ideas tab
        if (selectedTab == 0) {
            if (savedIdeas.isEmpty()) {
                item {
                    SavedCollectionEmptyState(
                        icon = Icons.Outlined.BookmarkBorder,
                        title = "Your collection is empty",
                        description = "You haven't saved any business ideas yet. Explore opportunities and bookmark the ones that interest you.",
                        buttonText = "Explore Opportunities",
                        onAction = onNavigateToExplore,
                        modifier = Modifier.testTag("saved_empty_state")
                    )
                }
            } else {
                item {
                    Text(
                        text = "SAVED OPPORTUNITIES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(
                            top = 4.dp,
                            bottom = 2.dp
                        )
                    )
                }

                items(
                    items = savedIdeas,
                    key = { it.id }
                ) { idea ->
                    SavedIdeaCard(
                        idea = idea,
                        onClick = { onSelectIdea(idea) }
                    )
                }
            }
        } else {
            // Guides tab
            if (uiState.documentLibrary.isEmpty()) {
                item {
                    SavedCollectionEmptyState(
                        icon = Icons.Outlined.MenuBook,
                        title = "No guides available",
                        description = "Business guides and learning resources will appear here when they become available.",
                        buttonText = "Explore Opportunities",
                        onAction = onNavigateToExplore
                    )
                }
            } else {
                item {
                    Text(
                        text = "LEARNING RESOURCES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(
                            top = 4.dp,
                            bottom = 2.dp
                        )
                    )
                }

                items(
                    items = uiState.documentLibrary,
                    key = { it.id }
                ) { doc ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(17.dp),
                        color = SurfaceCard,
                        border = BorderStroke(1.dp, SurfaceBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(13.dp),
                                color = SciRedContainer
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    tint = SciRedAccent,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }

                            Spacer(Modifier.width(13.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = doc.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(Modifier.height(5.dp))

                                Text(
                                    text = doc.category.name
                                        .replace("_", " ")
                                        .lowercase()
                                        .replaceFirstChar { it.uppercase() },
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Resource",
                                tint = SciBluePrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedCollectionTab(
    title: String,
    selected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(13.dp),
        color = if (selected) SciRedAccent else Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(17.dp),
                tint = if (selected) Color.White else TextSecondary
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) Color.White else TextSecondary
            )
        }
    }
}

@Composable
private fun SavedCollectionEmptyState(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    buttonText: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 14.dp),
        shape = RoundedCornerShape(22.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 22.dp,
                    vertical = 32.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SciRedContainer
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = SciRedAccent,
                    modifier = Modifier
                        .padding(19.dp)
                        .size(35.dp)
                )
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = description,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = TextSecondary,
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            Spacer(Modifier.height(22.dp))

            Button(
                onClick = onAction,
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SciRedAccent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(49.dp)
                    .testTag("saved_empty_explore_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Explore,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = buttonText,
                    fontWeight = FontWeight.Bold
                )
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
            .clickable(onClick = onClick)
            .testTag("saved_item_${idea.id}"),
        shape = RoundedCornerShape(17.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, SurfaceBorder)
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

            Spacer(Modifier.width(13.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = idea.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(6.dp))

                Surface(
                    shape = RoundedCornerShape(7.dp),
                    color = SciBlueContainer
                ) {
                    Text(
                        text = idea.category.displayName,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SciBluePrimary,
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        )
                    )
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text = idea.capitalTier.label,
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }

            Spacer(Modifier.width(5.dp))

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View idea details",
                tint = SciBluePrimary,
                modifier = Modifier.size(23.dp)
            )
        }
    }
}