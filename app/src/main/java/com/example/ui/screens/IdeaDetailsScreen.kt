package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BusinessIdea
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun IdeaDetailsScreen(
    idea: BusinessIdea,
    isSaved: Boolean,
    onBack: () -> Unit,
    onToggleSave: () -> Unit,
    onOpenCalculator: (BusinessIdea) -> Unit,
    onOpenBusinessPlan: (BusinessIdea) -> Unit,
    onStartJourney: (BusinessIdea) -> Unit
) {
    val context = LocalContext.current

    var isRequirementsExpanded by remember { mutableStateOf(false) }
    var isCustomersExpanded by remember { mutableStateOf(false) }
    var isMarketingExpanded by remember { mutableStateOf(false) }
    var isNextStepsExpanded by remember { mutableStateOf(false) }

    val formattedCapitalMin = remember(idea.capitalMinKes) {
        "%,d".format(idea.capitalMinKes)
    }
    val formattedCapitalMax = remember(idea.capitalMaxKes) {
        "%,d".format(idea.capitalMaxKes)
    }
    val formattedProfitMin = remember(idea.estimatedMonthlyProfitKes) {
        "%,d".format(idea.estimatedMonthlyProfitKes.first)
    }
    val formattedProfitMax = remember(idea.estimatedMonthlyProfitKes) {
        "%,d".format(idea.estimatedMonthlyProfitKes.last)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCream)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 104.dp)
        ) {
            // ---------------------------------------------------------
            // HERO
            // ---------------------------------------------------------

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                IdeaCardThumbnail(
                    type = idea.thumbnailType,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.62f),
                                    Color.Transparent,
                                    SciBlueDark.copy(alpha = 0.65f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 34.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HeroActionButton(
                            icon = Icons.Default.ArrowBack,
                            description = "Back",
                            testTag = "details_back_button",
                            onClick = onBack
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            HeroActionButton(
                                icon = Icons.Default.Share,
                                description = "Share idea",
                                testTag = "details_share_button"
                            ) {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Check out this Kenyan youth business idea on BiasharaHub KE: " +
                                            "${idea.title}!\n" +
                                            "Estimated investment: KSh $formattedCapitalMin - KSh $formattedCapitalMax"
                                    )
                                }

                                context.startActivity(
                                    Intent.createChooser(shareIntent, "Share business idea")
                                )
                            }

                            HeroActionButton(
                                icon = if (isSaved) {
                                    Icons.Default.Bookmark
                                } else {
                                    Icons.Outlined.BookmarkBorder
                                },
                                description = if (isSaved) "Remove bookmark" else "Save bookmark",
                                testTag = "details_bookmark_button",
                                tint = if (isSaved) SciRedAccent else TextPrimary,
                                onClick = onToggleSave
                            )
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            color = SciRedAccent,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "BUSINESS IDEA",
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 5.dp
                                ),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                        }

                        Text(
                            text = idea.title,
                            color = Color.White,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 31.sp
                        )
                    }
                }
            }

            // ---------------------------------------------------------
            // MAIN CONTENT
            // ---------------------------------------------------------

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
            ) {
                Spacer(modifier = Modifier.height(18.dp))

                // Category and capital tier
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DetailTag(
                        text = idea.category.displayName,
                        background = SciRedContainer,
                        foreground = SciRedDark
                    )

                    DetailTag(
                        text = idea.capitalTier.label,
                        background = SciBlueContainer,
                        foreground = SciBluePrimary
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Financial summary
                Text(
                    text = "Financial overview",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, SurfaceBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FinancialMetric(
                                title = "ESTIMATED INVESTMENT",
                                amount = "KSh $formattedCapitalMin",
                                rangeEnd = "KSh $formattedCapitalMax",
                                icon = Icons.Default.AccountBalanceWallet,
                                accent = SciBluePrimary,
                                modifier = Modifier.weight(1f)
                            )

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(72.dp)
                                    .background(SurfaceBorder)
                            )

                            FinancialMetric(
                                title = "POTENTIAL MONTHLY PROFIT",
                                amount = "KSh $formattedProfitMin",
                                rangeEnd = "KSh $formattedProfitMax",
                                icon = Icons.Default.TrendingUp,
                                accent = KenyaGreenPrimary,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 14.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        HorizontalDivider(color = SurfaceBorder)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(7.dp))

                            Text(
                                text = "Figures are estimates. Actual costs and returns depend on your market and execution.",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = KenyaGold,
                        modifier = Modifier.size(19.dp)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = idea.rating.toString(),
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = "(${idea.reviewCount}+ reviews)",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Overview
                SectionHeader(
                    title = "Business overview",
                    subtitle = "Understand the opportunity"
                )

                Spacer(modifier = Modifier.height(9.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, SurfaceBorder)
                ) {
                    Text(
                        text = idea.summary,
                        modifier = Modifier.padding(16.dp),
                        color = TextSecondary,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // -----------------------------------------------------
                // EXPANDABLE DETAILS
                // -----------------------------------------------------

                SectionHeader(
                    title = "Explore the details",
                    subtitle = "Everything you need to prepare for launch"
                )

                Spacer(modifier = Modifier.height(12.dp))

                AccordionItem(
                    title = "Startup requirements",
                    icon = Icons.Default.Inventory2,
                    isExpanded = isRequirementsExpanded,
                    onToggle = {
                        isRequirementsExpanded = !isRequirementsExpanded
                    },
                    testTag = "acc_startup_requirements"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        DetailSubheading("Capital breakdown")

                        if (idea.capitalBreakdown.isEmpty()) {
                            DetailEmptyText("No capital breakdown has been provided yet.")
                        } else {
                            idea.capitalBreakdown.forEach { (item, cost) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item,
                                        modifier = Modifier.weight(1f),
                                        color = TextSecondary,
                                        fontSize = 13.sp
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        text = "KSh ${"%,d".format(cost)}",
                                        color = TextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = SurfaceBorder)

                        DetailSubheading("Required equipment")

                        if (idea.equipmentNeeded.isEmpty()) {
                            DetailEmptyText("No specific equipment listed.")
                        } else {
                            idea.equipmentNeeded.forEach { equipment ->
                                DetailBulletRow(
                                    text = equipment,
                                    icon = Icons.Default.CheckCircle
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                AccordionItem(
                    title = "Potential customers",
                    icon = Icons.Default.People,
                    isExpanded = isCustomersExpanded,
                    onToggle = {
                        isCustomersExpanded = !isCustomersExpanded
                    },
                    testTag = "acc_potential_customers"
                ) {
                    if (idea.targetCustomers.isEmpty()) {
                        DetailEmptyText("Customer information is not available yet.")
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            idea.targetCustomers.forEach { customer ->
                                DetailBulletRow(
                                    text = customer,
                                    icon = Icons.Default.PersonOutline
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                AccordionItem(
                    title = "Marketing strategies",
                    icon = Icons.Default.Campaign,
                    isExpanded = isMarketingExpanded,
                    onToggle = {
                        isMarketingExpanded = !isMarketingExpanded
                    },
                    testTag = "acc_marketing_strategies"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = idea.marketingStrategy,
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SciBlueContainer
                        ) {
                            Column(
                                modifier = Modifier.padding(13.dp),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AccountBalanceWallet,
                                        contentDescription = null,
                                        tint = SciBluePrimary,
                                        modifier = Modifier.size(17.dp)
                                    )

                                    Spacer(modifier = Modifier.width(7.dp))

                                    Text(
                                        text = "M-PESA advantage",
                                        color = SciBluePrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }

                                Text(
                                    text = idea.mpesaTip,
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                AccordionItem(
                    title = "Next steps",
                    icon = Icons.Default.Navigation,
                    isExpanded = isNextStepsExpanded,
                    onToggle = {
                        isNextStepsExpanded = !isNextStepsExpanded
                    },
                    testTag = "acc_next_steps"
                ) {
                    if (idea.launchSteps.isEmpty()) {
                        DetailEmptyText("Launch steps are not available yet.")
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            idea.launchSteps.forEachIndexed { index, step ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(27.dp)
                                            .clip(CircleShape)
                                            .background(SciRedContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${index + 1}",
                                            color = SciRedDark,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(11.dp))

                                    Text(
                                        text = step,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(top = 3.dp),
                                        color = TextSecondary,
                                        fontSize = 13.sp,
                                        lineHeight = 19.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // -----------------------------------------------------
                // BUSINESS TOOLS
                // -----------------------------------------------------

                SectionHeader(
                    title = "Put your idea into action",
                    subtitle = "Use these tools to plan your next move"
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { onOpenCalculator(idea) },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("details_calculator_button"),
                        shape = RoundedCornerShape(13.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SciRedAccent
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "Calculator",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = { onOpenBusinessPlan(idea) },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("details_plan_button"),
                        shape = RoundedCornerShape(13.dp),
                        border = BorderStroke(1.dp, SciBluePrimary),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = SciBluePrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "Plan draft",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Start a guided journey
                OutlinedButton(
                    onClick = { onStartJourney(idea) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(13.dp),
                    border = BorderStroke(1.dp, SciRedAccent),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = SciRedAccent
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Start business journey",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // -------------------------------------------------------------
        // STICKY SAVE ACTION
        // -------------------------------------------------------------

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = Color.White,
            shadowElevation = 12.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = if (isSaved) "In your collection" else "Keep this idea",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = if (isSaved) {
                            "You can find it in My Ideas."
                        } else {
                            "Save it for later."
                        },
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = onToggleSave,
                    modifier = Modifier
                        .height(49.dp)
                        .widthIn(min = 142.dp)
                        .testTag("details_sticky_save_button"),
                    shape = RoundedCornerShape(13.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSaved) {
                            SciBluePrimary
                        } else {
                            SciRedAccent
                        }
                    )
                ) {
                    Icon(
                        imageVector = if (isSaved) {
                            Icons.Default.BookmarkAdded
                        } else {
                            Icons.Default.BookmarkAdd
                        },
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        text = if (isSaved) "Saved" else "Save idea",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

// =====================================================================
// REUSABLE COMPONENTS
// =====================================================================

@Composable
private fun HeroActionButton(
    icon: ImageVector,
    description: String,
    testTag: String,
    tint: Color = TextPrimary,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.94f))
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun DetailTag(
    text: String,
    background: Color,
    foreground: Color
) {
    Surface(
        color = background,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 11.dp,
                vertical = 6.dp
            ),
            color = foreground,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun FinancialMetric(
    title: String,
    amount: String,
    rangeEnd: String,
    icon: ImageVector,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accent,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = title,
            color = TextSecondary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.4.sp,
            lineHeight = 13.sp
        )

        Text(
            text = amount,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Text(
            text = "to $rangeEnd",
            color = TextSecondary,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun DetailSubheading(text: String) {
    Text(
        text = text,
        color = TextPrimary,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun DetailEmptyText(text: String) {
    Text(
        text = text,
        color = TextSecondary,
        fontSize = 12.sp,
        lineHeight = 18.sp
    )
}

@Composable
private fun DetailBulletRow(
    text: String,
    icon: ImageVector
) {
    Row(
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = KenyaGreenPrimary,
            modifier = Modifier
                .size(17.dp)
                .padding(top = 1.dp)
        )

        Spacer(modifier = Modifier.width(9.dp))

        Text(
            text = text,
            modifier = Modifier.weight(1f),
            color = TextSecondary,
            fontSize = 13.sp,
            lineHeight = 19.sp
        )
    }
}

// Kept public to preserve compatibility with other screens.
@Composable
fun AccordionItem(
    title: String,
    icon: ImageVector,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    testTag: String,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag),
        shape = RoundedCornerShape(15.dp),
        color = Color.White,
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle)
                    .padding(horizontal = 15.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(37.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(SciRedContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = SciRedDark,
                        modifier = Modifier.size(19.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Icon(
                    imageVector = if (isExpanded) {
                        Icons.Default.ExpandLess
                    } else {
                        Icons.Default.ExpandMore
                    },
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = TextSecondary,
                    modifier = Modifier.size(22.dp)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 2.dp,
                            bottom = 17.dp
                        )
                ) {
                    HorizontalDivider(
                        color = SurfaceBorder,
                        modifier = Modifier.padding(bottom = 14.dp)
                    )

                    content()
                }
            }
        }
    }
}