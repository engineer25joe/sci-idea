package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 90.dp)
        ) {
            // Hero Image Header with Overlaid Action Icons
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
            ) {
                IdeaCardThumbnail(
                    type = idea.thumbnailType,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark gradient overlay for readable controls
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent, Color.Black.copy(alpha = 0.4f))
                            )
                        )
                )

                // Top Controls: Back arrow, Share, Bookmark
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 36.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.85f))
                            .testTag("details_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF111827)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = {
                                val shareIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Check out this Kenyan youth business idea on BiasharaHub KE: ${idea.title}!\nEst. Investment: KSh ${idea.capitalMinKes} - ${idea.capitalMaxKes}"
                                    )
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Idea"))
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.85f))
                                .testTag("details_share_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color(0xFF111827),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = onToggleSave,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.85f))
                                .testTag("details_bookmark_button")
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (isSaved) KenyanGreen else Color(0xFF111827),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Body Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Title
                Text(
                    text = idea.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tags: Agriculture (green pill), Low Capital (blue pill)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFE8F5E9)
                    ) {
                        Text(
                            text = idea.category.displayName,
                            color = KenyanGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFE0F2FE)
                    ) {
                        Text(
                            text = idea.capitalTier.label,
                            color = Color(0xFF0369A1),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2-Column Metrics Box (Estimated Investment & Potential Monthly Profit)
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFF9FAFB),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Estimated Investment",
                                fontSize = 11.sp,
                                color = Color(0xFF6B7280),
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "KSh ${String.format("%,d", idea.capitalMinKes)} - ${String.format("%,d", idea.capitalMaxKes)}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827)
                            )
                        }

                        Divider(
                            modifier = Modifier
                                .height(40.dp)
                                .width(1.dp),
                            color = Color(0xFFE5E7EB)
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp)
                        ) {
                            Text(
                                text = "Potential Monthly Profit",
                                fontSize = 11.sp,
                                color = Color(0xFF6B7280),
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "KSh ${String.format("%,d", idea.estimatedMonthlyProfitKes.first)} - ${String.format("%,d", idea.estimatedMonthlyProfitKes.last)}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = KenyanGreen
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Star Rating Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = KenyanGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${idea.rating} ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                    Text(
                        text = "(${idea.reviewCount}+ reviews)",
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Overview Section
                Text(
                    text = "Overview",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = idea.summary,
                    fontSize = 14.sp,
                    color = Color(0xFF4B5563),
                    lineHeight = 21.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 4 Expandable Accordion Sections
                AccordionItem(
                    title = "Startup Requirements",
                    icon = Icons.Default.Inventory2,
                    isExpanded = isRequirementsExpanded,
                    onToggle = { isRequirementsExpanded = !isRequirementsExpanded },
                    testTag = "acc_startup_requirements"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Capital Breakdown:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF374151)
                        )
                        idea.capitalBreakdown.forEach { (item, cost) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("• $item", fontSize = 13.sp, color = Color(0xFF4B5563), modifier = Modifier.weight(1f))
                                Text("KSh ${String.format("%,d", cost)}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = KenyanGreen)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Required Equipment:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF374151)
                        )
                        idea.equipmentNeeded.forEach { eq ->
                            Text("✓ $eq", fontSize = 13.sp, color = Color(0xFF4B5563))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                AccordionItem(
                    title = "Potential Customers",
                    icon = Icons.Default.People,
                    isExpanded = isCustomersExpanded,
                    onToggle = { isCustomersExpanded = !isCustomersExpanded },
                    testTag = "acc_potential_customers"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        idea.targetCustomers.forEach { customer ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = KenyanGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(customer, fontSize = 13.sp, color = Color(0xFF374151))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                AccordionItem(
                    title = "Marketing Strategies",
                    icon = Icons.Default.Campaign,
                    isExpanded = isMarketingExpanded,
                    onToggle = { isMarketingExpanded = !isMarketingExpanded },
                    testTag = "acc_marketing_strategies"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(idea.marketingStrategy, fontSize = 13.sp, color = Color(0xFF4B5563), lineHeight = 19.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("M-PESA Edge:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = KenyanGreen)
                        Text(idea.mpesaTip, fontSize = 12.sp, color = Color(0xFF374151))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                AccordionItem(
                    title = "Next Steps",
                    icon = Icons.Default.Navigation,
                    isExpanded = isNextStepsExpanded,
                    onToggle = { isNextStepsExpanded = !isNextStepsExpanded },
                    testTag = "acc_next_steps"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        idea.launchSteps.forEachIndexed { index, step ->
                            Row(verticalAlignment = Alignment.Top) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(KenyanGreen),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(step, fontSize = 13.sp, color = Color(0xFF374151), lineHeight = 18.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Tools: Calculator & Plan Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { onOpenCalculator(idea) },
                        modifier = Modifier.weight(1f).testTag("details_calculator_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = KenyanGreen)
                    ) {
                        Icon(imageVector = Icons.Default.Calculate, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Calculator", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { onOpenBusinessPlan(idea) },
                        modifier = Modifier.weight(1f).testTag("details_plan_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0F268C))
                    ) {
                        Icon(imageVector = Icons.Default.Description, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Plan Draft", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Bottom Sticky Action Button: "Save Idea" / "Saved"
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)) {
                Button(
                    onClick = onToggleSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("details_sticky_save_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSaved) Color(0xFF0A512D) else KenyanGreen
                    ),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.BookmarkAdded else Icons.Default.BookmarkAdd,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isSaved) "Saved to My Ideas" else "Save Idea",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

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
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF9FAFB),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() }
                    .padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = KenyanGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1F2937)
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFF6B7280),
                    modifier = Modifier.size(20.dp)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 4.dp)
                ) {
                    Divider(color = Color(0xFFE5E7EB), modifier = Modifier.padding(bottom = 12.dp))
                    content()
                }
            }
        }
    }
}
