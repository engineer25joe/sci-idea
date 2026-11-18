package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BusinessIdea
import com.example.ui.components.DeveloperFooter
import com.example.ui.components.FeasibilityBadge
import com.example.ui.components.KesTag
import com.example.ui.components.MatchScoreBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdeaDetailSheet(
    idea: BusinessIdea,
    isSaved: Boolean,
    isCompared: Boolean = false,
    onDismiss: () -> Unit,
    onToggleSave: () -> Unit,
    onToggleCompare: () -> Unit = {},
    onOpenCalculator: () -> Unit,
    onBuildBusinessPlan: () -> Unit = {},
    onStartJourney: () -> Unit = {}
) {
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(
            topStart = 26.dp,
            topEnd = 26.dp
        ),
        containerColor = SurfaceCream,
        modifier = Modifier.testTag("idea_detail_sheet"),
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = TextMuted.copy(alpha = 0.6f)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
                .padding(bottom = 30.dp)
        ) {

            // ---------------------------------------------------------
            // HEADER
            // ---------------------------------------------------------

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FeasibilityBadge(group = idea.feasibilityGroup)

                    Surface(
                        color = SciBlueContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = idea.shengTitle,
                            modifier = Modifier.padding(
                                horizontal = 9.dp,
                                vertical = 5.dp
                            ),
                            color = SciBluePrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SheetIconButton(
                        icon = Icons.Default.CompareArrows,
                        description = "Compare idea",
                        tint = if (isCompared) SciRedAccent else TextSecondary,
                        onClick = onToggleCompare
                    )

                    SheetIconButton(
                        icon = Icons.Default.Share,
                        description = "Share idea",
                        tint = SciBluePrimary
                    ) {
                        val shareText =
                            "Check out this Kenyan youth business blueprint for " +
                                "'${idea.title}' (${idea.shengTitle})!\n" +
                                "Startup capital: KSh ${idea.capitalMinKes} - " +
                                "${idea.capitalMaxKes}\n" +
                                "Estimated monthly profit: KSh " +
                                "${idea.estimatedMonthlyProfitKes.first} - " +
                                "${idea.estimatedMonthlyProfitKes.last}.\n" +
                                "Powered by BiasharaHub KE (SCI BASE COLLECTIONS)."

                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            action = Intent.ACTION_SEND
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }

                        context.startActivity(
                            Intent.createChooser(sendIntent, "Share business idea")
                        )
                    }

                    SheetIconButton(
                        icon = if (isSaved) {
                            Icons.Default.Bookmark
                        } else {
                            Icons.Outlined.BookmarkBorder
                        },
                        description = if (isSaved) "Remove saved idea" else "Save idea",
                        tint = if (isSaved) SciRedAccent else TextSecondary,
                        onClick = onToggleSave
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ---------------------------------------------------------
            // IDEA TITLE
            // ---------------------------------------------------------

            Text(
                text = idea.title,
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            MatchScoreBadge(score = idea.matchScore)

            Spacer(modifier = Modifier.height(13.dp))

            Text(
                text = idea.summary,
                color = TextSecondary,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(17.dp))

            // ---------------------------------------------------------
            // PROBLEM AND SOLUTION
            // ---------------------------------------------------------

            if (
                idea.problemBeingSolved.isNotBlank() ||
                idea.proposedSolution.isNotBlank()
            ) {
                SheetSectionHeading(
                    title = "Problem and solution",
                    subtitle = "The opportunity behind this business",
                    icon = Icons.Default.Lightbulb
                )

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        SurfaceBorder
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(15.dp),
                        verticalArrangement = Arrangement.spacedBy(13.dp)
                    ) {
                        if (idea.problemBeingSolved.isNotBlank()) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Text(
                                    text = "THE MARKET PROBLEM",
                                    color = SciRedAccent,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.8.sp
                                )

                                Text(
                                    text = idea.problemBeingSolved,
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    lineHeight = 20.sp
                                )
                            }
                        }

                        if (idea.proposedSolution.isNotBlank()) {
                            HorizontalDivider(color = SurfaceBorder)

                            Column(
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Text(
                                    text = "PROPOSED SOLUTION",
                                    color = KenyaGreenPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.8.sp
                                )

                                Text(
                                    text = idea.proposedSolution,
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ---------------------------------------------------------
            // FINANCIAL OVERVIEW
            // ---------------------------------------------------------

            SheetSectionHeading(
                title = "Financial overview",
                subtitle = "Understand the estimated investment",
                icon = Icons.Default.AccountBalanceWallet
            )

            Spacer(modifier = Modifier.height(11.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(17.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    SurfaceBorder
                )
            ) {
                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    KesTag(
                        amountText = "KSh ${"%,d".format(idea.capitalMinKes)} - " +
                            "${"%,d".format(idea.capitalMaxKes)}",
                        label = "Required startup capital",
                        color = SciBluePrimary,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    HorizontalDivider(color = SurfaceBorder)

                    Spacer(modifier = Modifier.height(12.dp))

                    KesTag(
                        amountText = "KSh ${"%,d".format(idea.estimatedMonthlyProfitKes.first)} - " +
                            "${"%,d".format(idea.estimatedMonthlyProfitKes.last)}",
                        label = "Estimated monthly net profit",
                        color = KenyaGreenPrimary,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(15.dp)
                        )

                        Spacer(modifier = Modifier.width(7.dp))

                        Text(
                            text = "These are planning estimates, not guaranteed returns. " +
                                "Actual results depend on operating costs, customer demand, " +
                                "location and business execution.",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ---------------------------------------------------------
            // OPERATIONS AND SOURCING
            // ---------------------------------------------------------

            SheetSectionHeading(
                title = "Operations and sourcing",
                subtitle = "How the business will operate",
                icon = Icons.Default.Settings
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    SurfaceBorder
                )
            ) {
                Column(
                    modifier = Modifier.padding(15.dp),
                    verticalArrangement = Arrangement.spacedBy(13.dp)
                ) {
                    OperationalDetail(
                        label = "Business model",
                        value = idea.businessModel.label,
                        icon = Icons.Default.Business
                    )

                    OperationalDetail(
                        label = "Risk profile",
                        value = idea.riskLevel.label,
                        icon = Icons.Default.Security
                    )

                    OperationalDetail(
                        label = "Time commitment",
                        value = idea.timeCommitment.label,
                        icon = Icons.Default.Schedule
                    )

                    OperationalDetail(
                        label = "Payback period",
                        value = idea.paybackPeriod,
                        icon = Icons.Default.EventAvailable
                    )

                    if (idea.supplierAndSourcingGuidance.isNotBlank()) {
                        HorizontalDivider(color = SurfaceBorder)

                        Text(
                            text = "SUPPLIER AND SOURCING GUIDANCE",
                            color = SciBluePrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )

                        Text(
                            text = idea.supplierAndSourcingGuidance,
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ---------------------------------------------------------
            // CAPITAL BREAKDOWN
            // ---------------------------------------------------------

            SheetSectionHeading(
                title = "Startup capital breakdown",
                subtitle = "Where your initial investment may go",
                icon = Icons.Default.ReceiptLong
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    SurfaceBorder
                )
            ) {
                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    val capitalBreakdownEntries = idea.capitalBreakdown.entries.toList()

                    if (capitalBreakdownEntries.isEmpty()) {
                        SheetEmptyMessage(
                            "Capital breakdown information is not available yet."
                        )
                    } else {
                        capitalBreakdownEntries.forEachIndexed { index, entry ->
                            val item = entry.key
                            val cost = entry.value
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 9.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(25.dp)
                                            .clip(RoundedCornerShape(7.dp))
                                            .background(SciRedContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${index + 1}",
                                            color = SciRedDark,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(9.dp))

                                    Text(
                                        text = item,
                                        color = TextPrimary,
                                        fontSize = 13.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "KSh ${"%,d".format(cost)}",
                                    color = SciBluePrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            if (index != capitalBreakdownEntries.lastIndex) {
                                HorizontalDivider(color = SurfaceBorder)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ---------------------------------------------------------
            // LAUNCH ROADMAP
            // ---------------------------------------------------------

            SheetSectionHeading(
                title = "Launch roadmap",
                subtitle = "Your practical steps towards starting",
                icon = Icons.Default.RocketLaunch
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    SurfaceBorder
                )
            ) {
                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    if (idea.launchSteps.isEmpty()) {
                        SheetEmptyMessage(
                            "Launch steps are not available yet."
                        )
                    } else {
                        idea.launchSteps.forEachIndexed { index, step ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(SciRedAccent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = step,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(top = 5.dp),
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    lineHeight = 20.sp
                                )
                            }

                            if (index != idea.launchSteps.lastIndex) {
                                Box(
                                    modifier = Modifier
                                        .padding(start = 14.dp)
                                        .width(2.dp)
                                        .height(15.dp)
                                        .background(SurfaceBorder)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ---------------------------------------------------------
            // COMPLIANCE
            // ---------------------------------------------------------

            SheetSectionHeading(
                title = "Compliance and permits",
                subtitle = "County and regulatory considerations",
                icon = Icons.Default.Gavel
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (idea.compliance.isEmpty()) {
                SheetEmptyMessage(
                    "Specific compliance information has not been provided. " +
                        "Confirm applicable requirements with your county and relevant authorities."
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    idea.compliance.forEach { comp ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                SurfaceBorder
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(13.dp),
                                verticalArrangement = Arrangement.spacedBy(7.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = comp.title,
                                        modifier = Modifier.weight(1f),
                                        color = TextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = SciBlueContainer
                                    ) {
                                        Text(
                                            text = comp.estimatedCostKes,
                                            modifier = Modifier.padding(
                                                horizontal = 8.dp,
                                                vertical = 5.dp
                                            ),
                                            color = SciBluePrimary,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Text(
                                    text = comp.authority,
                                    color = SciRedDark,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Text(
                                    text = comp.description,
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ---------------------------------------------------------
            // M-PESA GUIDANCE
            // ---------------------------------------------------------

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                color = KenyaGreenContainer
            ) {
                Column(
                    modifier = Modifier.padding(15.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = OnKenyaGreenContainer,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "M-PESA cashflow setup",
                            color = OnKenyaGreenContainer,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = idea.mpesaTip,
                        color = OnKenyaGreenContainer,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ---------------------------------------------------------
            // INSIDER TIPS
            // ---------------------------------------------------------

            SheetSectionHeading(
                title = "Siri ya Hustle",
                subtitle = "Practical insights and insider tips",
                icon = Icons.Default.Lightbulb
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    SurfaceBorder
                )
            ) {
                Column(
                    modifier = Modifier.padding(15.dp),
                    verticalArrangement = Arrangement.spacedBy(11.dp)
                ) {
                    if (idea.insiderTips.isEmpty()) {
                        SheetEmptyMessage(
                            "Additional business tips are not available yet."
                        )
                    } else {
                        idea.insiderTips.forEach { tip ->
                            Row(
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TipsAndUpdates,
                                    contentDescription = null,
                                    tint = KenyaGoldDark,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(modifier = Modifier.width(9.dp))

                                Text(
                                    text = tip,
                                    modifier = Modifier.weight(1f),
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    lineHeight = 19.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Last reviewed
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Update,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(14.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Last reviewed: ${idea.lastReviewedDate}",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ---------------------------------------------------------
            // ACTIONS
            // ---------------------------------------------------------

            SheetSectionHeading(
                title = "Take the next step",
                subtitle = "Turn this business idea into an actionable plan",
                icon = Icons.Default.Flag
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    onDismiss()
                    onBuildBusinessPlan()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("sheet_build_plan_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SciRedAccent
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(9.dp))

                Text(
                    text = "Build SCI Business Plan",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onDismiss()
                        onStartJourney()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(51.dp)
                        .testTag("sheet_start_journey_button"),
                    shape = RoundedCornerShape(13.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        SciBluePrimary
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = SciBluePrimary
                    ),
                    contentPadding = PaddingValues(horizontal = 7.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Checklist,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = "6-Stage Plan",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = {
                        onDismiss()
                        onOpenCalculator()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(51.dp)
                        .testTag("sheet_calculator_button"),
                    shape = RoundedCornerShape(13.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        SciRedAccent
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = SciRedAccent
                    ),
                    contentPadding = PaddingValues(horizontal = 7.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = "Profit Calculator",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            DeveloperFooter()

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

// =====================================================================
// REUSABLE COMPONENTS
// =====================================================================

@Composable
private fun SheetIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    tint: Color,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(38.dp)
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
private fun SheetSectionHeading(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SciRedContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SciRedDark,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(modifier = Modifier.width(11.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun OperationalDetail(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = SciBluePrimary,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = TextSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = value,
            color = TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SheetEmptyMessage(text: String) {
    Text(
        text = text,
        color = TextSecondary,
        fontSize = 12.sp,
        lineHeight = 18.sp
    )
}