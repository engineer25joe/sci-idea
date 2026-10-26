package com.example.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun CompareSheet(
    ideas: List<BusinessIdea>,
    onRemoveFromCompare: (String) -> Unit,
    onSelectIdea: (BusinessIdea) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = SurfaceCard,
        modifier = Modifier.testTag("compare_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 36.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.CompareArrows, contentDescription = "Compare", tint = SciBluePrimary)
                    Text(
                        text = "Compare Business Ideas (${ideas.size}/3)",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = SciBluePrimary
                        )
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (ideas.isEmpty()) {
                Text(
                    text = "No ideas selected for comparison. Tap 'Compare' on any 2 or 3 business ideas to view them side-by-side.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ideas.forEach { idea ->
                        Card(
                            modifier = Modifier.width(280.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceVariantWarm)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    FeasibilityBadge(group = idea.feasibilityGroup)
                                    IconButton(
                                        onClick = { onRemoveFromCompare(idea.id) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Close, contentDescription = "Remove", tint = TextMuted)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = idea.title,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )

                                Spacer(modifier = Modifier.height(4.dp))
                                MatchScoreBadge(score = idea.matchScore)

                                Spacer(modifier = Modifier.height(10.dp))

                                KesTag(
                                    amountText = "KSh ${"%,d".format(idea.capitalMinKes)} - ${"%,d".format(idea.capitalMaxKes)}",
                                    label = "Required Capital",
                                    color = SciBluePrimary,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                KesTag(
                                    amountText = "KSh ${"%,d".format(idea.estimatedMonthlyProfitKes.first)} - ${"%,d".format(idea.estimatedMonthlyProfitKes.last)}",
                                    label = "Est. Monthly Net",
                                    color = KenyaGreenPrimary,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(text = "• Model: ${idea.businessModel.label}", style = MaterialTheme.typography.bodySmall)
                                Text(text = "• Risk: ${idea.riskLevel.label}", style = MaterialTheme.typography.bodySmall)
                                Text(text = "• Payback: ${idea.paybackPeriod}", style = MaterialTheme.typography.bodySmall)
                                Text(text = "• Time: ${idea.timeCommitment.label}", style = MaterialTheme.typography.bodySmall)

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        onDismiss()
                                        onSelectIdea(idea)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = SciBluePrimary),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Open Blueprint", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            DeveloperFooter()
        }
    }
}
