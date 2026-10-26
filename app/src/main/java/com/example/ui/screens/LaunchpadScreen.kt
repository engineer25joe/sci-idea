package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BusinessDocument
import com.example.model.BusinessIdea
import com.example.model.DownloadStatus
import com.example.model.YouthFundGuide
import com.example.ui.components.DeveloperFooter
import com.example.ui.components.KesTag
import com.example.ui.theme.*
import com.example.viewmodel.BiasharaUiState
import com.example.viewmodel.BiasharaViewModel

@Composable
fun LaunchpadScreen(
    uiState: BiasharaUiState,
    viewModel: BiasharaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) } // 0 = Saved Hustles, 1 = Document Library (R2), 2 = Youth Funds, 3 = Legal & Permits

    val savedIdeas = uiState.allIdeas.filter { uiState.savedIdeaIds.contains(it.id) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("launchpad_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Hero Header with SCI BASE Branding
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(SciBluePrimary, SciBlueDark)
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
                            text = "Mpango Wangu",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = KenyaGold,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "My Hustle Launchpad",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Track your 6-stage journey, draft business plans for SCI BUSINESS PLAN, access offline guides, and explore verified Kenyan youth funding.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }

        // Tab Selector Row
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = SurfaceCard,
                contentColor = SciBluePrimary
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Saved (${savedIdeas.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Doc Library", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Youth Funds", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text("Legal & Permits", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
            }
        }

        // Tab 0: Saved Hustles & 6-Stage Journey
        if (selectedTab == 0) {
            if (savedIdeas.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.BookmarkBorder,
                            contentDescription = "No saved ideas",
                            tint = TextMuted,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No saved business plans yet",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tap the bookmark icon on any idea in the Matcher to shortlist your favorite Kenyan ventures here.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                items(savedIdeas) { idea ->
                    EnhancedSavedIdeaCard(
                        idea = idea,
                        onOpenBlueprint = { viewModel.selectDetailIdea(idea) },
                        onOpenJourney = { viewModel.openJourneyForIdea(idea) },
                        onBuildBusinessPlan = { viewModel.openBusinessPlanDraft(idea) },
                        onRemove = { viewModel.toggleSaveIdea(idea.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }

        // Tab 1: Document Library (Cloudflare R2 Integration Architecture)
        if (selectedTab == 1) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SciBlueContainer)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CloudDownload, contentDescription = "R2", tint = SciBluePrimary)
                            Text(
                                text = "Cloudflare R2 Document Library",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = OnSciBlueContainer
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Download business planning templates, budget ledgers, and compliance guides once to keep them accessible 100% offline without mobile data consumption.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                        )
                    }
                }
            }

            items(uiState.documentLibrary) { doc ->
                DocumentLibraryCard(
                    document = doc,
                    onToggleDownload = { viewModel.toggleDocumentDownload(doc.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }

        // Tab 2: Youth Funding Guides (Verified)
        if (selectedTab == 2) {
            items(uiState.youthFundGuides) { fund ->
                EnhancedYouthFundCard(
                    fund = fund,
                    onOpenUrl = { url ->
                        if (url.isNotBlank()) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        // Tab 3: Legal & Permits Guide
        if (selectedTab == 3) {
            item {
                LegalComplianceGuide(
                    onOpenUrl = { url ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }

        // Developer Footer
        item {
            DeveloperFooter()
        }
    }
}

@Composable
fun EnhancedSavedIdeaCard(
    idea: BusinessIdea,
    onOpenBlueprint: () -> Unit,
    onOpenJourney: () -> Unit,
    onBuildBusinessPlan: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
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

                IconButton(onClick = onRemove) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Remove",
                        tint = SciRedAccent
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = idea.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KesTag(
                    amountText = "KSh ${"%,d".format(idea.capitalMinKes)}",
                    label = "Target Capital",
                    color = SciBluePrimary,
                    modifier = Modifier.weight(1f)
                )

                KesTag(
                    amountText = idea.paybackPeriod,
                    label = "Break-Even",
                    color = KenyaGreenPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onOpenJourney,
                    colors = ButtonDefaults.buttonColors(containerColor = SciBluePrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Checklist, contentDescription = "Journey", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("6-Stage Plan", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = onBuildBusinessPlan,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Description, contentDescription = "Plan", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Business Plan", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun DocumentLibraryCard(
    document: BusinessDocument,
    onToggleDownload: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDownloaded = document.downloadStatus == DownloadStatus.DOWNLOADED

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = document.title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${document.category.displayName} • ${document.fileType} (${document.formattedSize()})",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isDownloaded) Color(0xFFDCFCE7) else SurfaceVariantWarm
                ) {
                    Text(
                        text = if (isDownloaded) "OFFLINE READY" else "CLOUD R2",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isDownloaded) Color(0xFF166534) else TextSecondary,
                            fontSize = 9.sp
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = document.description,
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, lineHeight = 16.sp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Key: ${document.remoteR2ObjectKey}",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 9.sp),
                    modifier = Modifier.weight(1f)
                )

                TextButton(onClick = onToggleDownload) {
                    Icon(
                        imageVector = if (isDownloaded) Icons.Default.DeleteOutline else Icons.Default.Download,
                        contentDescription = "Download",
                        modifier = Modifier.size(16.dp),
                        tint = if (isDownloaded) SciRedAccent else SciBluePrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isDownloaded) "Remove" else "Save Offline",
                        fontSize = 11.sp,
                        color = if (isDownloaded) SciRedAccent else SciBluePrimary
                    )
                }
            }
        }
    }
}

@Composable
fun EnhancedYouthFundCard(
    fund: YouthFundGuide,
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = fund.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SciBluePrimary.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = fund.ussdOrPortal,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = SciBluePrimary
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Provider: ${fund.provider} • Verified: ${fund.lastVerifiedDate}",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KesTag(
                    amountText = fund.maxAmountKes,
                    label = "Max Allocation",
                    color = KenyaGoldDark,
                    modifier = Modifier.weight(1f)
                )

                KesTag(
                    amountText = fund.interestRate,
                    label = "Interest Rate",
                    color = KenyaGreenPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Eligibility Requirements:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            fund.eligibilityChecklist.forEach { item ->
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text("✓ ", color = KenyaGreenPrimary, fontWeight = FontWeight.Bold)
                    Text(text = item, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Application Instructions:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = fund.applicationProcess,
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "⚠️ ${fund.approvalDisclaimer}",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, fontSize = 10.sp)
            )

            if (fund.officialSourceUrl.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { onOpenUrl(fund.officialSourceUrl) },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.OpenInNew, contentDescription = "Open", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Visit Official Portal", fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun LegalComplianceGuide(
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Official Kenyan Business Formalization Guide",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = SciBluePrimary
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Follow these 4 official steps to legitimize your hustle, avoid county askari fines, and access business bank accounts.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Step 1: eCitizen BRS
            Text(
                text = "Step 1: Business Name Search & Registration",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Cost: KSh 950 (KSh 100 name reservation + KSh 850 registration). Log onto ecitizen.go.ke under BRS (Business Registration Service). Receive your official Certificate of Registration within 48 hours.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Step 2: KRA Tax PIN
            Text(
                text = "Step 2: KRA PIN & Turnover Tax (TOT)",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Apply for a business PIN on itax.kra.go.ke. Small businesses with turnover under KSh 25M qualify for Turnover Tax (TOT) at 3% or file Nil Returns if just getting started.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Step 3: Single Business Permit
            Text(
                text = "Step 3: County Single Business Permit",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Obtain from your local Sub-County revenue office (e.g. Nairobi City County Portal or County revenue offices). Small kiosks typically range KSh 3,500 - 7,000 annually.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Step 4: Safaricom Lipa Na M-PESA
            Text(
                text = "Step 4: M-PESA Till / Pochi La Biashara",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Sole proprietors can dial *334# to activate Pochi instantly. For a Buy Goods Till, submit your ID copy and Business Registration certificate on m-pesaforbusiness.co.ke.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
            )
        }
    }
}

