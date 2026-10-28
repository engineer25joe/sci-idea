package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BusinessIdea
import com.example.model.SkillCategory
import com.example.ui.components.*
import com.example.viewmodel.BiasharaUiState
import com.example.viewmodel.BiasharaViewModel

@Composable
fun HomeDashboardScreen(
    uiState: BiasharaUiState,
    viewModel: BiasharaViewModel,
    onNavigateToExplore: (SkillCategory?) -> Unit,
    onNavigateToSearch: () -> Unit,
    onSelectIdea: (BusinessIdea) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
    ) {
        // Top App Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BiasharaLogoBadge(sizeDp = 38)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "BiasharaHub ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = KenyanGreen
                    ) {
                        Text(
                            text = "KE",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }

                // Notification Bell
                IconButton(
                    onClick = {
                        viewModel.triggerDjangoSync()
                    },
                    modifier = Modifier.testTag("home_notification_bell")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = Color(0xFF1F2937),
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Hello, Welcome back!
        item {
            Text(
                text = "Hello, 👋\nWelcome back!",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF111827),
                lineHeight = 32.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Explore, learn and start your business journey.",
                fontSize = 14.sp,
                color = Color(0xFF6B7280)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Search Bar (Interactive pill container)
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clickable { onNavigateToSearch() }
                    .testTag("home_search_bar"),
                shape = RoundedCornerShape(26.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Search for business ideas...",
                            fontSize = 14.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(KenyanGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Hero Banner Card: "Small Ideas Big Dreams"
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onNavigateToExplore(null) }
                    .testTag("home_hero_banner"),
                color = Color(0xFF0F3D27),
                shadowElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFF0A3A24), Color(0xFF135A39))
                            )
                        )
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Small Ideas\nBig Dreams",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 26.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Your next opportunity\nis just a tap away!",
                            fontSize = 13.sp,
                            color = Color(0xFFD1FAE5),
                            lineHeight = 18.sp
                        )
                    }

                    // Potted seedling illustration
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E5E3D)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Spa,
                            contentDescription = "Growing seedling",
                            tint = Color(0xFF4ADE80),
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Section: "Popular Categories"
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Popular Categories",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Text(
                    text = "See all >",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = KenyanGreen,
                    modifier = Modifier
                        .clickable { onNavigateToExplore(null) }
                        .testTag("home_see_all_categories")
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // 6 Category Grid (2 rows x 3 columns)
        item {
            val categories = listOf(
                CategoryItem("Agriculture", SkillCategory.AGRIBUSINESS, Icons.Default.Eco, Color(0xFFE8F5E9), Color(0xFF1B5E20)),
                CategoryItem("Retail & Trade", SkillCategory.RETAIL_COMMERCE, Icons.Default.Storefront, Color(0xFFFFEBEE), Color(0xFFC62828)),
                CategoryItem("Food & Beverage", SkillCategory.FOOD_HOSPITALITY, Icons.Default.Fastfood, Color(0xFFF3E8FF), Color(0xFF7E22CE)),
                CategoryItem("Technology", SkillCategory.DIGITAL_TECH, Icons.Default.Laptop, Color(0xFFE0F2FE), Color(0xFF0369A1)),
                CategoryItem("Services", SkillCategory.SERVICES_LOGISTICS, Icons.Default.Handshake, Color(0xFFFFF7ED), Color(0xFFC2410C)),
                CategoryItem("Manufacturing", SkillCategory.MANUFACTURING, Icons.Default.PrecisionManufacturing, Color(0xFFEFF6FF), Color(0xFF1D4ED8))
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                categories.chunked(3).forEach { rowList ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowList.forEach { cat ->
                            CategoryCard(
                                item = cat,
                                modifier = Modifier.weight(1f),
                                onClick = { onNavigateToExplore(cat.skillCategory) }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(28.dp))
        }

        // Section: "Top Opportunities For You"
        item {
            Text(
                text = "Top Opportunities",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Featured idea cards from repository
        val topIdeas = uiState.allIdeas.take(4)
        items(topIdeas) { idea ->
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

data class CategoryItem(
    val title: String,
    val skillCategory: SkillCategory,
    val icon: ImageVector,
    val bgColor: Color,
    val iconColor: Color
)

@Composable
fun CategoryCard(
    item: CategoryItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(96.dp)
            .clickable { onClick() }
            .testTag("cat_card_${item.title.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(item.bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = item.iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = item.title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF374151),
                maxLines = 1
            )
        }
    }
}

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
                modifier = Modifier.size(70.dp)
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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFE8F5E9)
                    ) {
                        Text(
                            text = idea.category.displayName,
                            color = KenyanGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFE0F2FE)
                    ) {
                        Text(
                            text = idea.capitalTier.label,
                            color = Color(0xFF0369A1),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "KSh ${String.format("%,d", idea.capitalMinKes)} - ${String.format("%,d", idea.capitalMaxKes)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF374151)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = KenyanGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${idea.rating} (${idea.reviewCount}+)",
                        fontSize = 11.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            IconButton(
                onClick = onToggleSave,
                modifier = Modifier.testTag("save_btn_${idea.id}")
            ) {
                Icon(
                    imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Save",
                    tint = if (isSaved) KenyanGreen else Color(0xFF9CA3AF)
                )
            }
        }
    }
}
