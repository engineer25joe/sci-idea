package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
        HorizontalDivider(
            color = SurfaceBorder,
            thickness = 1.dp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Text(
            text = "Developed by Engineer Joe",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = SciBluePrimary
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "© 2026 SCI BASE COLLECTIONS. All rights reserved.",
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "BiasharaHub KE • Your Business Launchpad",
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextMuted
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
        FeasibilityGroup.READY_NOW ->
            Pair(StatusReadyContainer, StatusReadyGreen)

        FeasibilityGroup.PREPARATION_NEEDED ->
            Pair(StatusPrepContainer, StatusPrepYellow)

        FeasibilityGroup.FUTURE_OPPORTUNITIES ->
            Pair(StatusFutureContainer, StatusFutureBlue)
    }

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = bgColor,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 6.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = group.emoji,
                fontSize = 12.sp
            )

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
        score >= 80 ->
            Pair(StatusReadyContainer, StatusReadyGreen)

        score >= 55 ->
            Pair(StatusPrepContainer, StatusPrepYellow)

        else ->
            Pair(SurfaceVariantWarm, TextSecondary)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(badgeBg)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
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
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.07f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            color.copy(alpha = 0.22f)
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 8.dp
            )
        ) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = color,
                    fontSize = 9.sp
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = amountText,
                style = MaterialTheme.typography.titleSmall.copy(
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
                    fontWeight = if (isSelected) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    }
                )
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = SciRedAccent,
            selectedLabelColor = Color.White,
            containerColor = SurfaceCard,
            labelColor = TextPrimary
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = if (isSelected) {
                SciRedAccent
            } else {
                SurfaceBorder
            },
            selectedBorderColor = SciRedAccent,
            borderWidth = 1.dp
        ),
        modifier = modifier.heightIn(min = 40.dp)
    )
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )

        if (subtitle != null) {
            Spacer(modifier = Modifier.height(4.dp))

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
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BiasharaLogoBadge(sizeDp = 48)

                Column {
                    Text(
                        text = "BiasharaHub KE",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = SciBluePrimary
                        )
                    )

                    Text(
                        text = "SCI BASE COLLECTIONS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SciRedDark,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        },

        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Discover business opportunities that match your skills, interests, available capital and circumstances.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 22.sp,
                        color = TextSecondary
                    )
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceVariantWarm,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "APPLICATION DETAILS",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = SciBluePrimary
                            )
                        )

                        Text(
                            text = "Brand: SCI BASE COLLECTIONS",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = "Developer: Engineer Joe",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = "Currency: Kenyan Shilling (KES)",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = "Platform: BiasharaHub KE",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SciBlueContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Privacy and security",
                            tint = SciBluePrimary,
                            modifier = Modifier.size(22.dp)
                        )

                        Text(
                            text = "Your business matching experience is designed to work offline where supported. Account-based services and online synchronisation may require an internet connection.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = OnSciBlueContainer,
                                lineHeight = 19.sp
                            )
                        )
                    }
                }

                Text(
                    text = "Developed by Engineer Joe\n© 2026 SCI BASE COLLECTIONS",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = SciBluePrimary,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedButton(
                    onClick = onClearData,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = SciRedDark
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        SciRedAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Clear Local User Data",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        },

        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SciRedAccent,
                    contentColor = Color.White
                )
            ) {
                Text("Close")
            }
        },

        containerColor = SurfaceCard,
        shape = RoundedCornerShape(24.dp)
    )
}