package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BusinessIdea
import com.example.model.SkillCategory
import com.example.ui.components.BiasharaLogoBadge
import com.example.ui.components.FeasibilityBadge
import com.example.ui.components.IdeaCardThumbnail
import com.example.ui.theme.*
import com.example.viewmodel.BiasharaUiState
import com.example.viewmodel.BiasharaViewModel

@Composable
fun HomeDashboardScreen(
    uiState: BiasharaUiState,
    viewModel: BiasharaViewModel,
    onNavigateToExplore: (SkillCategory?) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToMatcher: () -> Unit,
    onSelectIdea: (BusinessIdea) -> Unit
) {
    val displayName = uiState.userProfileName
        .substringBefore(" ")
        .ifBlank { "Entrepreneur" }

    val topIdeas = if (uiState.matchedIdeas.isNotEmpty()) {
        uiState.matchedIdeas.take(4)
    } else {
        uiState.allIdeas.take(4)
    }

    val categories = listOf(
        CategoryItem(
            "Agriculture",
            SkillCategory.AGRIBUSINESS,
            Icons.Default.Eco,
            KenyaGreenContainer,
            KenyaGreenPrimary
        ),
        CategoryItem(
            "Retail & Trade",
            SkillCategory.RETAIL_COMMERCE,
            Icons.Default.Storefront,
            SciRedContainer,
            SciRedDark
        ),
        CategoryItem(
            "Food & Beverage",
            SkillCategory.FOOD_HOSPITALITY,
            Icons.Default.Restaurant,
            KenyaGoldLight,
            KenyaGoldDark
        ),
        CategoryItem(
            "Technology",
            SkillCategory.DIGITAL_TECH,
            Icons.Default.Laptop,
            SciBlueContainer,
            SciBluePrimary
        ),
        CategoryItem(
            "Services",
            SkillCategory.SERVICES_LOGISTICS,
            Icons.Default.Handshake,
            SciRedContainer,
            SciRedDark
        ),
        CategoryItem(
            "Manufacturing",
            SkillCategory.MANUFACTURING,
            Icons.Default.PrecisionManufacturing,
            SciBlueContainer,
            SciBluePrimary
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCream)
            .testTag("home_dashboard_screen"),
        contentPadding = PaddingValues(
            start = 18.dp,
            end = 18.dp,
            top = 12.dp,
            bottom = 112.dp
        ),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        // ------------------------------------------------
        // HEADER
        // ------------------------------------------------

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BiasharaLogoBadge(sizeDp = 42)

                    Spacer(Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "BiasharaHub",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                        )

                        Text(
                            text = "SCI BASE COLLECTIONS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondary,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = SciBlueContainer
                ) {
                    IconButton(
                        onClick = { viewModel.triggerDjangoSync() },
                        modifier = Modifier.testTag("home_notification_bell")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Synchronise notifications",
                            tint = SciBluePrimary
                        )
                    }
                }
            }
        }

        // ------------------------------------------------
        // WELCOME SECTION
        // ------------------------------------------------

        item {
            Column {
                Text(
                    text = "Habari, $displayName 👋",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = "Your next business opportunity starts here.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary
                    )
                )
            }
        }

        // ------------------------------------------------
        // SEARCH
        // ------------------------------------------------

        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { onNavigateToSearch() }
                    .testTag("home_search_bar"),
                shape = RoundedCornerShape(16.dp),
                color = SurfaceCard,
                border = BorderStroke(1.dp, SurfaceBorder),
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = SciBluePrimary
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = "Search business ideas, skills...",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = SciRedAccent
                    )
                }
            }
        }

        // ------------------------------------------------
        // HERO BANNER
        // ------------------------------------------------

        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToExplore(null) }
                    .testTag("home_hero_banner"),
                shape = RoundedCornerShape(22.dp),
                color = SciBluePrimary,
                shadowElevation = 3.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(
                                    SciBluePrimary,
                                    SciBlueDark
                                )
                            )
                        )
                        .padding(22.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SciRedAccent
                        ) {
                            Text(
                                text = "YOUR BUSINESS JOURNEY",
                                modifier = Modifier.padding(
                                    horizontal = 9.dp,
                                    vertical = 5.dp
                                ),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.7.sp
                                )
                            )
                        }

                        Spacer(Modifier.height(15.dp))

                        Text(
                            text = "Small Capital.\nBig Possibilities.",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                lineHeight = 34.sp
                            )
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Discover practical business opportunities that match your skills, interests and budget.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White.copy(alpha = 0.85f),
                                lineHeight = 21.sp
                            )
                        )

                        Spacer(Modifier.height(18.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SciRedAccent
                        ) {
                            Row(
                                modifier = Modifier
                                    .clickable { onNavigateToExplore(null) }
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = 11.dp
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Explore opportunities",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                Spacer(Modifier.width(8.dp))

                                Icon(
                                    Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(17.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // ------------------------------------------------
        // BUSINESS MATCHER ENTRY
        // ------------------------------------------------

        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToMatcher() }
                    .testTag("home_matcher_entry"),
                shape = RoundedCornerShape(18.dp),
                color = SciRedContainer,
                border = BorderStroke(1.dp, SciRedAccent.copy(alpha = 0.18f))
            ) {
                Row(
                    modifier = Modifier.padding(17.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(13.dp),
                        color = SciRedAccent
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .padding(12.dp)
                                .size(25.dp)
                        )
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Find My Business",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "Match your skills, interests and capital with practical business opportunities.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )
                        )

                        Spacer(Modifier.height(9.dp))

                        Text(
                            text = "START MATCHING  →",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = SciRedDark,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Open business matcher",
                        tint = SciRedAccent
                    )
                }
            }
        }
        // ------------------------------------------------
        // QUICK STATISTICS
        // ------------------------------------------------

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HomeStatCard(
                    title = "Opportunities",
                    value = uiState.allIdeas.size.toString(),
                    icon = Icons.Default.Lightbulb,
                    modifier = Modifier.weight(1f)
                )

                HomeStatCard(
                    title = "Saved ideas",
                    value = uiState.savedIdeaIds.size.toString(),
                    icon = Icons.Outlined.BookmarkBorder,
                    modifier = Modifier.weight(1f)
                )

                HomeStatCard(
                    title = "Skills",
                    value = uiState.selectedSkillIds.size.toString(),
                    icon = Icons.Default.Psychology,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // ------------------------------------------------
        // CAPITAL SUMMARY
        // ------------------------------------------------

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = SurfaceCard,
                border = BorderStroke(1.dp, SurfaceBorder)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = KenyaGoldLight
                    ) {
                        Icon(
                            Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = KenyaGoldDark,
                            modifier = Modifier
                                .padding(12.dp)
                                .size(25.dp)
                        )
                    }

                    Spacer(Modifier.width(13.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Your starting capital",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = TextSecondary
                            )
                        )

                        Text(
                            text = uiState.exactCapitalKes?.let {
                                "KSh ${String.format("%,d", it)}"
                            } ?: "Not yet specified",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Icon(
                        Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = KenyaGreenPrimary
                    )
                }
            }
        }

        // ------------------------------------------------
        // CATEGORIES
        // ------------------------------------------------

        item {
            Column {
                HomeSectionHeader(
                    title = "Explore categories",
                    subtitle = "Find opportunities in your area of interest.",
                    actionText = "View all",
                    onAction = { onNavigateToExplore(null) }
                )

                Spacer(Modifier.height(13.dp))

                categories.chunked(2).forEach { rowCategories ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowCategories.forEach { category ->
                            CategoryCard(
                                item = category,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    onNavigateToExplore(category.skillCategory)
                                }
                            )
                        }

                        if (rowCategories.size == 1) {
                            Spacer(Modifier.weight(1f))
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                }
            }
        }

        // ------------------------------------------------
        // PERSONALIZED OPPORTUNITIES
        // ------------------------------------------------

        item {
            HomeSectionHeader(
                title = "Recommended opportunities",
                subtitle = "Ideas worth exploring based on your profile.",
                actionText = "Explore all",
                onAction = { onNavigateToExplore(null) }
            )
        }

        if (topIdeas.isEmpty()) {
            item {
                EmptyHomeCard(
                    onExplore = { onNavigateToExplore(null) }
                )
            }
        } else {
            items(
                items = topIdeas,
                key = { it.id }
            ) { idea ->
                FeaturedIdeaRowCard(
                    idea = idea,
                    isSaved = uiState.savedIdeaIds.contains(idea.id),
                    onSelect = { onSelectIdea(idea) },
                    onToggleSave = {
                        viewModel.toggleSaveIdea(idea.id)
                    }
                )
            }
        }

        // ------------------------------------------------
        // FOOTER
        // ------------------------------------------------

        item {
            HorizontalDivider(color = SurfaceBorder)

            Spacer(Modifier.height(8.dp))

            Text(
                text = "SCI BASE COLLECTIONS",
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )
            )
        }
    }
}

// ======================================================
// SECTION HEADER
// ======================================================

@Composable
private fun HomeSectionHeader(
    title: String,
    subtitle: String,
    actionText: String,
    onAction: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary
                )
            )
        }

        TextButton(onClick = onAction) {
            Text(
                text = actionText,
                color = SciRedAccent,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ======================================================
// STATISTICS CARD
// ======================================================

@Composable
private fun HomeStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SciRedAccent,
                modifier = Modifier.size(21.dp)
            )

            Spacer(Modifier.height(9.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
            )

            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ======================================================
// CATEGORY MODEL
// ======================================================

data class CategoryItem(
    val title: String,
    val skillCategory: SkillCategory,
    val icon: ImageVector,
    val bgColor: Color,
    val iconColor: Color
)

// ======================================================
// CATEGORY CARD
// ======================================================

@Composable
fun CategoryCard(
    item: CategoryItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(112.dp)
            .clickable { onClick() }
            .testTag(
                "cat_card_${item.title.lowercase().replace(" ", "_")}"
            ),
        shape = RoundedCornerShape(17.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = item.bgColor
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = item.iconColor,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(23.dp)
                )
            }

            Spacer(Modifier.height(9.dp))

            Text(
                text = item.title,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ======================================================
// FEATURED BUSINESS IDEA CARD
// ======================================================

@Composable
fun FeaturedIdeaRowCard(
    idea: BusinessIdea,
    isSaved: Boolean,
    onSelect: () -> Unit,
    onToggleSave: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("featured_idea_${idea.id}"),
        shape = RoundedCornerShape(18.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IdeaCardThumbnail(
                type = idea.thumbnailType,
                modifier = Modifier
                    .size(86.dp)
                    .clip(RoundedCornerShape(13.dp))
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = idea.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = idea.category.displayName,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = SciBluePrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = "KSh ${String.format("%,d", idea.capitalMinKes)} – ${String.format("%,d", idea.capitalMaxKes)}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(Modifier.height(5.dp))

                FeasibilityBadge(
                    group = idea.feasibilityGroup
                )
            }

            IconButton(
                onClick = onToggleSave,
                modifier = Modifier.testTag("save_btn_${idea.id}")
            ) {
                Icon(
                    imageVector = if (isSaved) {
                        Icons.Default.Bookmark
                    } else {
                        Icons.Outlined.BookmarkBorder
                    },
                    contentDescription = if (isSaved) {
                        "Remove saved idea"
                    } else {
                        "Save idea"
                    },
                    tint = if (isSaved) SciRedAccent else TextSecondary
                )
            }
        }
    }
}

// ======================================================
// EMPTY RECOMMENDATIONS
// ======================================================

@Composable
private fun EmptyHomeCard(
    onExplore: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Lightbulb,
                contentDescription = null,
                tint = KenyaGoldDark,
                modifier = Modifier.size(40.dp)
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Discover your first opportunity",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = "Explore the catalogue to find a business idea that matches your goals.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary
                )
            )

            Spacer(Modifier.height(14.dp))

            Button(
                onClick = onExplore,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SciRedAccent
                )
            ) {
                Text("Explore ideas")
            }
        }
    }
}