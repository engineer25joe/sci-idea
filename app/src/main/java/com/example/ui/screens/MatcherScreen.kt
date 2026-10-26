package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.BiasharaUiState
import com.example.viewmodel.BiasharaViewModel
import com.example.viewmodel.DiscoveryMode

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MatcherScreen(
    uiState: BiasharaUiState,
    viewModel: BiasharaViewModel,
    onNavigateToTrends: () -> Unit,
    onNavigateToStories: () -> Unit,
    modifier: Modifier = Modifier
) {
    var customSkillInput by remember { mutableStateOf("") }
    var exactCapitalText by remember(uiState.exactCapitalKes) {
        mutableStateOf(uiState.exactCapitalKes?.toString() ?: "")
    }
    var showAllReadyNow by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("matcher_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Hero Header with SCI BASE Ecosystem Brand Bar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                SciBluePrimary,
                                SciBlueDark
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 22.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
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
                                    text = "🇰🇪 BiasharaHub KE",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = KenyaGold,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Tafuta Biashara Yako",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Compare button (if items selected)
                            if (uiState.comparedIdeaIds.isNotEmpty()) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = KenyaGold,
                                    modifier = Modifier.clickable { viewModel.openCompareSheet() }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CompareArrows,
                                            contentDescription = "Compare",
                                            tint = Color.Black,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "${uiState.comparedIdeaIds.size}",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                            }

                            // About App Info Button
                            IconButton(
                                onClick = { viewModel.openAboutDialog() },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "About App",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Deterministic, 100% offline recommendation engine matching your personal skills and exact starting capital in KSh to viable Kenyan businesses.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }

        // Discovery Mode Selector Tabs (Quick Discovery vs Detailed Discovery)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                TabRow(
                    selectedTabIndex = if (uiState.discoveryMode == DiscoveryMode.QUICK) 0 else 1,
                    containerColor = SurfaceCard,
                    contentColor = SciBluePrimary
                ) {
                    Tab(
                        selected = uiState.discoveryMode == DiscoveryMode.QUICK,
                        onClick = { viewModel.setDiscoveryMode(DiscoveryMode.QUICK) },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(imageVector = Icons.Default.Bolt, contentDescription = "Quick", modifier = Modifier.size(16.dp))
                                Text("Quick Discovery", fontWeight = FontWeight.Bold)
                            }
                        }
                    )
                    Tab(
                        selected = uiState.discoveryMode == DiscoveryMode.DETAILED,
                        onClick = { viewModel.setDiscoveryMode(DiscoveryMode.DETAILED) },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(imageVector = Icons.Default.Tune, contentDescription = "Detailed", modifier = Modifier.size(16.dp))
                                Text("Detailed Profile", fontWeight = FontWeight.Bold)
                            }
                        }
                    )
                }
            }
        }

        // Step 1: Skills Selection
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("skills_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionHeader(
                        title = "1. Your Skills & Talents",
                        subtitle = "Select what you already know or do best (${uiState.selectedSkillIds.size} selected)"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.allSkills.take(18).forEach { skill ->
                            val isSelected = uiState.selectedSkillIds.contains(skill.id)
                            SkillChip(
                                text = skill.name,
                                isSelected = isSelected,
                                onClick = { viewModel.toggleSkill(skill.id) },
                                modifier = Modifier.testTag("skill_chip_${skill.id}")
                            )
                        }
                    }

                    // Custom user skill tags display
                    if (uiState.customSkills.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Custom Skills Added:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = SciBluePrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            uiState.customSkills.forEach { custom ->
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = SciBlueContainer,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, SciBluePrimary.copy(alpha = 0.3f))
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(start = 10.dp, end = 4.dp, top = 2.dp, bottom = 2.dp)
                                    ) {
                                        Text(
                                            text = custom,
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                                        )
                                        IconButton(
                                            onClick = { viewModel.removeCustomSkill(custom) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Remove skill",
                                                tint = SciRedAccent,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Custom Skill Input Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = customSkillInput,
                            onValueChange = { customSkillInput = it },
                            placeholder = { Text("Add custom skill (e.g. Beadwork, Drone, Coding)") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("custom_skill_input"),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Button(
                            onClick = {
                                if (customSkillInput.isNotBlank()) {
                                    viewModel.addCustomSkill(customSkillInput)
                                    customSkillInput = ""
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SciBluePrimary),
                            modifier = Modifier.testTag("add_custom_skill_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                }
            }
        }

        // Step 2: Capital & Budget (Exact + Brackets)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionHeader(
                        title = "2. Available Startup Capital",
                        subtitle = "Enter your exact cash in KSh or choose a budget bracket"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = exactCapitalText,
                        onValueChange = {
                            exactCapitalText = it
                            val parsed = it.toIntOrNull()
                            viewModel.setExactCapital(parsed)
                        },
                        label = { Text("Exact Capital Available (KSh)") },
                        placeholder = { Text("e.g. 15,000") },
                        leadingIcon = {
                            Text(
                                text = "KSh",
                                fontWeight = FontWeight.Bold,
                                color = SciBluePrimary,
                                modifier = Modifier.padding(start = 12.dp, end = 6.dp)
                            )
                        },
                        trailingIcon = {
                            if (exactCapitalText.isNotEmpty()) {
                                IconButton(onClick = {
                                    exactCapitalText = ""
                                    viewModel.setExactCapital(null)
                                }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("exact_capital_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Or choose a quick budget tier:",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = uiState.selectedCapitalTier == null && uiState.exactCapitalKes == null,
                            onClick = {
                                exactCapitalText = ""
                                viewModel.setCapitalTier(null)
                            },
                            label = { Text("Any Budget") }
                        )
                        CapitalTier.values().forEach { tier ->
                            FilterChip(
                                selected = uiState.selectedCapitalTier == tier,
                                onClick = {
                                    exactCapitalText = tier.maxKes.toString()
                                    viewModel.setCapitalTier(tier)
                                },
                                label = { Text(tier.label) }
                            )
                        }
                    }
                }
            }
        }

        // Step 3: Interests & Detailed Profile Criteria (if in Detailed Mode)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionHeader(
                        title = "3. Areas of Interest & Setting",
                        subtitle = "Align opportunities with your passion and county environment"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        InterestArea.values().forEach { interest ->
                            val isSelected = uiState.selectedInterests.contains(interest)
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.toggleInterest(interest) },
                                label = { Text(interest.shengLabel, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = KenyaGold,
                                    selectedLabelColor = Color.Black
                                )
                            )
                        }
                    }

                    if (uiState.discoveryMode == DiscoveryMode.DETAILED) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(12.dp))

                        // Equipment
                        Text(
                            text = "Existing Equipment & Resources You Own:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            EquipmentResource.values().forEach { equip ->
                                val isSelected = uiState.selectedEquipmentIds.contains(equip.id)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.toggleEquipment(equip.id) },
                                    label = { Text(equip.label, fontSize = 11.sp) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Time Commitment
                        Text(
                            text = "Available Weekly Time:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TimeCommitment.values().forEach { time ->
                                FilterChip(
                                    selected = uiState.availableTime == time,
                                    onClick = { viewModel.setTimeCommitment(if (uiState.availableTime == time) null else time) },
                                    label = { Text(time.label, fontSize = 11.sp) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Preferred Business Model
                        Text(
                            text = "Business Model Preference:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            BusinessModelType.values().forEach { model ->
                                FilterChip(
                                    selected = uiState.preferredBusinessModel == model,
                                    onClick = { viewModel.setPreferredBusinessModel(if (uiState.preferredBusinessModel == model) null else model) },
                                    label = { Text(model.label, fontSize = 11.sp) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section: Results Header & Explanation Disclaimer
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 4.dp)
            ) {
                Text(
                    text = "Personalized Recommendation Results",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "*Percentages represent compatibility with your skills & capital profile, not guaranteed profit or investment return.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                )
            }
        }

        // Section: 🟢 Ready Now Group
        if (uiState.readyNowIdeas.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("🟢", fontSize = 14.sp)
                        Text(
                            text = "Ready Now (${uiState.readyNowIdeas.size})",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = StatusReadyGreen
                            )
                        )
                    }

                    if (uiState.readyNowIdeas.size > 3) {
                        TextButton(onClick = { showAllReadyNow = !showAllReadyNow }) {
                            Text(if (showAllReadyNow) "Show Top 3" else "Explore All (${uiState.readyNowIdeas.size})")
                        }
                    }
                }
            }

            val displayedReadyNow = if (showAllReadyNow) uiState.readyNowIdeas else uiState.readyNowIdeas.take(3)
            items(displayedReadyNow) { idea ->
                EnhancedBusinessIdeaCard(
                    idea = idea,
                    isSaved = uiState.savedIdeaIds.contains(idea.id),
                    isCompared = uiState.comparedIdeaIds.contains(idea.id),
                    onCardClick = { viewModel.selectDetailIdea(idea) },
                    onToggleSave = { viewModel.toggleSaveIdea(idea.id) },
                    onToggleCompare = { viewModel.toggleCompareIdea(idea.id) },
                    onLoadIntoCalculator = { viewModel.loadIdeaIntoCalculator(idea) },
                    onStartJourney = { viewModel.openJourneyForIdea(idea) },
                    onBuildBusinessPlan = { viewModel.openBusinessPlanDraft(idea) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("matched_idea_${idea.id}")
                )
            }
        }

        // Section: 🟡 Preparation Needed Group
        if (uiState.prepNeededIdeas.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("🟡", fontSize = 14.sp)
                    Text(
                        text = "Preparation Needed (${uiState.prepNeededIdeas.size})",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = StatusPrepYellow
                        )
                    )
                }
            }

            items(uiState.prepNeededIdeas) { idea ->
                EnhancedBusinessIdeaCard(
                    idea = idea,
                    isSaved = uiState.savedIdeaIds.contains(idea.id),
                    isCompared = uiState.comparedIdeaIds.contains(idea.id),
                    onCardClick = { viewModel.selectDetailIdea(idea) },
                    onToggleSave = { viewModel.toggleSaveIdea(idea.id) },
                    onToggleCompare = { viewModel.toggleCompareIdea(idea.id) },
                    onLoadIntoCalculator = { viewModel.loadIdeaIntoCalculator(idea) },
                    onStartJourney = { viewModel.openJourneyForIdea(idea) },
                    onBuildBusinessPlan = { viewModel.openBusinessPlanDraft(idea) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }

        // Section: 🔵 Future Opportunities Group
        if (uiState.futureIdeas.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("🔵", fontSize = 14.sp)
                    Text(
                        text = "Future Opportunities (${uiState.futureIdeas.size})",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = StatusFutureBlue
                        )
                    )
                }
            }

            items(uiState.futureIdeas) { idea ->
                EnhancedBusinessIdeaCard(
                    idea = idea,
                    isSaved = uiState.savedIdeaIds.contains(idea.id),
                    isCompared = uiState.comparedIdeaIds.contains(idea.id),
                    onCardClick = { viewModel.selectDetailIdea(idea) },
                    onToggleSave = { viewModel.toggleSaveIdea(idea.id) },
                    onToggleCompare = { viewModel.toggleCompareIdea(idea.id) },
                    onLoadIntoCalculator = { viewModel.loadIdeaIntoCalculator(idea) },
                    onStartJourney = { viewModel.openJourneyForIdea(idea) },
                    onBuildBusinessPlan = { viewModel.openBusinessPlanDraft(idea) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }

        // Developer attribution & Copyright footer
        item {
            DeveloperFooter()
        }
    }
}

@Composable
fun EnhancedBusinessIdeaCard(
    idea: BusinessIdea,
    isSaved: Boolean,
    isCompared: Boolean,
    onCardClick: () -> Unit,
    onToggleSave: () -> Unit,
    onToggleCompare: () -> Unit,
    onLoadIntoCalculator: () -> Unit,
    onStartJourney: () -> Unit,
    onBuildBusinessPlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Sheng Title, Match Score & Bookmark
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
                        onClick = onToggleSave,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = if (isSaved) "Remove from saved" else "Save idea",
                            tint = if (isSaved) KenyaGoldDark else TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Title
            Text(
                text = idea.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(4.dp))
            MatchScoreBadge(score = idea.matchScore)

            Spacer(modifier = Modifier.height(6.dp))

            // Summary
            Text(
                text = idea.summary,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    lineHeight = 19.sp
                ),
                maxLines = 3
            )

            // Explainability Section
            idea.explanation?.let { exp ->
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SurfaceVariantWarm,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "💡 Why This Was Recommended For You:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = SciBluePrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (exp.matchedSkillNames.isNotEmpty()) {
                            Text(
                                text = "• Matching Skills: ${exp.matchedSkillNames.joinToString(", ")}",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                            )
                        }
                        Text(
                            text = "• Capital Analysis: ${exp.capitalStatusText}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                        )
                        Text(
                            text = "• Next Step: ${exp.recommendedNextStep}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = KenyaGreenDark
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Metrics Row: Capital & Monthly Profit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KesTag(
                    amountText = "KSh ${"%,d".format(idea.capitalMinKes)} - ${"%,d".format(idea.capitalMaxKes)}",
                    label = "Startup Capital",
                    color = SciBluePrimary,
                    modifier = Modifier.weight(1f)
                )

                KesTag(
                    amountText = "KSh ${"%,d".format(idea.estimatedMonthlyProfitKes.first)} - ${"%,d".format(idea.estimatedMonthlyProfitKes.last)}",
                    label = "Monthly Net",
                    color = KenyaGreenPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onLoadIntoCalculator,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = "Calc",
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Calculator", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = onStartJourney,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Checklist,
                        contentDescription = "Journey",
                        modifier = Modifier.size(15.dp),
                        tint = KenyaGreenPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "6-Stage Plan", fontSize = 11.sp, color = KenyaGreenPrimary)
                }

                Button(
                    onClick = onCardClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SciBluePrimary),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Blueprint",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Open",
                        modifier = Modifier.size(14.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}
