package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = SurfaceCard,
        modifier = Modifier.testTag("idea_detail_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 36.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FeasibilityBadge(group = idea.feasibilityGroup)

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SciBluePrimary.copy(alpha = 0.08f)
                    ) {
                        Text(
                            text = idea.shengTitle,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SciBluePrimary,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onToggleCompare,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CompareArrows,
                            contentDescription = "Compare",
                            tint = if (isCompared) KenyaGoldDark else TextMuted
                        )
                    }

                    IconButton(
                        onClick = {
                            val shareText = "Check out this Kenyan youth business blueprint for '${idea.title}' (${idea.shengTitle})! Startup capital: KSh ${idea.capitalMinKes} - ${idea.capitalMaxKes}, estimated monthly profit: KSh ${idea.estimatedMonthlyProfitKes.first} - ${idea.estimatedMonthlyProfitKes.last}. Powered by BiasharaHub KE (SCI BASE COLLECTIONS)."
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Hustle Blueprint"))
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = SciBluePrimary)
                    }

                    IconButton(
                        onClick = onToggleSave,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Save",
                            tint = if (isSaved) KenyaGoldDark else TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Full Title
            Text(
                text = idea.title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(4.dp))
            MatchScoreBadge(score = idea.matchScore)

            Spacer(modifier = Modifier.height(8.dp))

            // Summary
            Text(
                text = idea.summary,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    lineHeight = 20.sp
                )
            )

            // Problem & Proposed Solution
            if (idea.problemBeingSolved.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SurfaceVariantWarm,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "🎯 Problem in Local Market:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = SciRedAccent)
                        )
                        Text(
                            text = idea.problemBeingSolved,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "💡 Proposed Solution:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = KenyaGreenPrimary)
                        )
                        Text(
                            text = idea.proposedSolution.ifBlank { idea.summary },
                            style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Metrics row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KesTag(
                    amountText = "KSh ${"%,d".format(idea.capitalMinKes)} - ${"%,d".format(idea.capitalMaxKes)}",
                    label = "Required Capital",
                    color = SciBluePrimary,
                    modifier = Modifier.weight(1f)
                )

                KesTag(
                    amountText = "KSh ${"%,d".format(idea.estimatedMonthlyProfitKes.first)} - ${"%,d".format(idea.estimatedMonthlyProfitKes.last)}",
                    label = "Est. Monthly Net",
                    color = KenyaGreenPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Operational Parameters
            Text(
                text = "⚙️ Operations & Sourcing:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceVariantWarm),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "• Business Model: ${idea.businessModel.label}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "• Risk Profile: ${idea.riskLevel.label}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "• Time Commitment: ${idea.timeCommitment.label}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "• Payback Period: ${idea.paybackPeriod}", style = MaterialTheme.typography.bodySmall)
                    if (idea.supplierAndSourcingGuidance.isNotBlank()) {
                        Text(
                            text = "• Wholesale Sourcing: ${idea.supplierAndSourcingGuidance}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = SciBluePrimary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Capital Breakdown Table
            Text(
                text = "💰 Startup Capital Breakdown:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SurfaceVariantWarm,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    idea.capitalBreakdown.forEach { (item, cost) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "KSh ${"%,d".format(cost)}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = SciBluePrimary)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5-Step Launch Roadmap
            Text(
                text = "🚀 Step-by-Step Launch Blueprint:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            idea.launchSteps.forEachIndexed { index, step ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SciBluePrimary,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${index + 1}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextPrimary,
                            lineHeight = 18.sp
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Kenya Compliance & Permits
            Text(
                text = "📋 Compliance & County Requirements:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            idea.compliance.forEach { comp ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceVariantWarm)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = comp.title,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = comp.estimatedCostKes,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = SciBluePrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Text(
                            text = "${comp.authority} • ${comp.description}",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // M-Pesa & Financial Tips
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = KenyaGreenContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "📱 M-PESA Cashflow Setup:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnKenyaGreenContainer
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = idea.mpesaTip,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = OnKenyaGreenContainer,
                            lineHeight = 18.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Siri ya Hustle & Risks
            Text(
                text = "💡 Siri ya Hustle (Insider Secrets):",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            idea.insiderTips.forEach { tip ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("•", color = KenyaGoldDark, fontWeight = FontWeight.Bold)
                    Text(
                        text = tip,
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Last verified date: ${idea.lastReviewedDate}",
                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons: Build Plan, 6-Stage Journey, Calculator
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        onDismiss()
                        onBuildBusinessPlan()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SciBluePrimary)
                ) {
                    Icon(imageVector = Icons.Default.Description, contentDescription = "Plan")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Build Business Plan (SCI BUSINESS PLAN)")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onDismiss()
                            onStartJourney()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Checklist, contentDescription = "Journey")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("6-Stage Plan", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            onDismiss()
                            onOpenCalculator()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Calculate, contentDescription = "Calculate")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Profit Calc", fontSize = 12.sp)
                    }
                }
            }

            DeveloperFooter()
        }
    }
}
