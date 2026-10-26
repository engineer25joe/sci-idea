package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FeasibilityGroup
import com.example.ui.theme.*

@Composable
fun DeveloperFooter(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(
            color = Color(0xFFE2E8F0),
            thickness = 1.dp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Text(
            text = "Developed by 💕🙏Engineer Joe 🙏💕",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = SciBluePrimary,
                fontSize = 13.sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "© 2026 SCI BASE COLLECTIONS. All rights reserved.",
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary,
                fontSize = 11.sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "BiasharaHub KE • Offline-First Kenyan Youth Launchpad",
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextMuted,
                fontSize = 10.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FeasibilityBadge(
    group: FeasibilityGroup,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (group) {
        FeasibilityGroup.READY_NOW -> Pair(StatusReadyContainer, StatusReadyGreen)
        FeasibilityGroup.PREPARATION_NEEDED -> Pair(StatusPrepContainer, StatusPrepYellow)
        FeasibilityGroup.FUTURE_OPPORTUNITIES -> Pair(StatusFutureContainer, StatusFutureBlue)
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bgColor,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = group.emoji, fontSize = 11.sp)
            Text(
                text = group.label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            )
        }
    }
}

@Composable
fun MatchScoreBadge(
    score: Int,
    modifier: Modifier = Modifier
) {
    val (badgeBg, textCol) = when {
        score >= 80 -> Pair(Color(0xFFDCFCE7), Color(0xFF166534)) // green
        score >= 55 -> Pair(Color(0xFFFEF3C7), Color(0xFF92400E)) // amber
        else -> Pair(Color(0xFFF3F4F6), Color(0xFF4B5563))
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(badgeBg)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(textCol)
            )
            Text(
                text = "$score% Profile Match",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = textCol
                )
            )
        }
    }
}

@Composable
fun KesTag(
    amountText: String,
    label: String = "Capital",
    modifier: Modifier = Modifier,
    color: Color = SciBluePrimary
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.25f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = color
                )
            )
            Text(
                text = amountText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
        }
    }
}

@Composable
fun SkillChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = SciBluePrimary,
            selectedLabelColor = Color.White,
            containerColor = Color.White,
            labelColor = TextPrimary
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = if (isSelected) SciBluePrimary else Color(0xFFD1D5DB),
            selectedBorderColor = SciBluePrimary,
            borderWidth = 1.dp
        ),
        modifier = modifier.heightIn(min = 36.dp)
    )
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary
                )
            )
        }
    }
}

@Composable
fun AboutAppDialog(
    onDismiss: () -> Unit,
    onClearData: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SciBluePrimary,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🇰🇪", fontSize = 18.sp)
                    }
                }
                Column {
                    Text(
                        text = "BiasharaHub KE",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = SciBluePrimary
                        )
                    )
                    Text(
                        text = "Part of SCI BASE COLLECTIONS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = KenyaGoldDark,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "A deterministic, offline-first entrepreneurship launchpad designed to match Kenyan youths with viable business opportunities based on their actual skills, exact capital in KES, and local county market demand.",
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp)
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SurfaceVariantWarm,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Ecosystem Details",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = SciBluePrimary)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "• Brand: SCI BASE COLLECTIONS", style = MaterialTheme.typography.bodySmall)
                        Text(text = "• Lead Engineer: Engineer Joe", style = MaterialTheme.typography.bodySmall)
                        Text(text = "• Version: 1.1.0-Production (Offline-Ready)", style = MaterialTheme.typography.bodySmall)
                        Text(text = "• Target Currency: Kenyan Shilling (KES / KSh)", style = MaterialTheme.typography.bodySmall)
                        Text(text = "• Destination: SCI BUSINESS PLAN Integration Ready", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFDCFCE7),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = "Privacy", tint = Color(0xFF166534))
                        Text(
                            text = "Privacy Guarantee: 100% of recommendation logic, notes, and financial models run on your device without internet or registration.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF166534), fontSize = 11.sp)
                        )
                    }
                }

                Text(
                    text = "Developed by 💕🙏Engineer Joe 🙏💕\n© 2026 SCI BASE COLLECTIONS. All rights reserved.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = SciBlueDark,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedButton(
                    onClick = onClearData,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SciRedAccent),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Clear Local User Data (Privacy Reset)", fontSize = 11.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = SciBluePrimary)
            ) {
                Text("Close")
            }
        }
    )
}
