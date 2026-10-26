package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MarketSectorTrend
import com.example.ui.components.DeveloperFooter
import com.example.ui.components.KesTag
import com.example.ui.theme.*
import com.example.viewmodel.BiasharaUiState
import com.example.viewmodel.BiasharaViewModel

@Composable
fun TrendsScreen(
    uiState: BiasharaUiState,
    viewModel: BiasharaViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("trends_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Hero Header with SCI BASE Branding
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(SciBluePrimary, Color(0xFF0F172A))
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 22.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = SciRedAccent,
                            modifier = Modifier.padding(end = 6.dp)
                        ) {
                            Text(
                                text = "SCI BASE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = "Hali ya Soko KE",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = KenyaGold,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Current Kenyan Business Trends",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Verified market intelligence on high-demand sectors across Kenyan counties: who the customers are, why these areas are promising for youth, and official source citations.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }

        // Summary highlight chips
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("5", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = SciBluePrimary))
                        Text("Hot Sectors", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                    }
                    Divider(modifier = Modifier.height(36.dp).width(1.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("KSh 2.5K", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = KenyaGoldDark))
                        Text("Min Entry Cap", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                    }
                    Divider(modifier = Modifier.height(36.dp).width(1.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("M-PESA", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = KenyaGreenPrimary))
                        Text("Fast Cashflow", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                    }
                }
            }
        }

        // Sector Cards
        items(uiState.marketSectorTrends) { trend ->
            EnhancedSectorTrendCard(
                trend = trend,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("trend_card_${trend.id}")
            )
        }

        // Developer Footer
        item {
            DeveloperFooter()
        }
    }
}

@Composable
fun EnhancedSectorTrendCard(
    trend: MarketSectorTrend,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Sector Name & Growth Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = trend.sectorName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SciBluePrimary.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = trend.growthTag,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SciBluePrimary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = SurfaceVariantWarm
            ) {
                Text(
                    text = "📊 ${trend.evidenceType.label}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = trend.summary,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    lineHeight = 19.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            KesTag(
                amountText = trend.averageStartupCapitalRange,
                label = "Typical Youth Startup Capital",
                color = SciBluePrimary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Why Promising For Youth
            Text(
                text = "Why It's Promising For Young Kenyans:",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = trend.whyPromisingForYouth,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Potential Customer Base
            Text(
                text = "Potential Customer Base:",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            trend.potentialCustomerBase.forEach { customer ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("👥", fontSize = 12.sp)
                    Text(
                        text = customer,
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Citation Information
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SurfaceVariantWarm,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Source Citation: ${trend.sourceCitation}",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 10.sp)
                    )
                    Text(
                        text = "Geographic Scope: ${trend.geographicScope} (${trend.periodCovered})",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, fontSize = 9.sp)
                    )
                }
            }
        }
    }
}
