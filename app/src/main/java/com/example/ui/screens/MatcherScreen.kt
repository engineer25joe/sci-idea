package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
            .background(SurfaceCream)
            .testTag("matcher_screen"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {

        // ---------------------------------------------------------
        // HERO HEADER
        // ---------------------------------------------------------

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
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 22.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(7.dp)
                            ) {
                                Surface(
                                    color = SciRedAccent,
                                    shape = RoundedCornerShape(5.dp)
                                ) {
                                    Text(
                                        text = "SCI BASE",
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        ),
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }

                                Text(
                                    text = "🇰🇪 BiasharaHub KE",
                                    color = KenyaGold,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Tafuta Biashara Yako",
                                color = Color.White,
                                fontSize = 27.sp,
                                fontWeight = FontWeight.ExtraBold,
                                lineHeight = 33.sp
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = "Discover opportunities built around your skills, capital and ambitions.",
                                color = Color.White.copy(alpha = 0.84f),
                                style = MaterialTheme.typography.bodyMedium,
                                lineHeight = 20.sp
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (uiState.comparedIdeaIds.isNotEmpty()) {
                                Surface(
                                    shape = CircleShape,
                                    color = KenyaGold,
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clickable {
                                            viewModel.openCompareSheet()
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.CompareArrows,
                                            contentDescription = "Compare ideas",
                                            tint = Color.Black
                                        )

                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .size(17.dp)
                                                .background(
                                                    SciRedAccent,
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = uiState.comparedIdeaIds.size.toString(),
                                                color = Color.White,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            IconButton(
                                onClick = { viewModel.openAboutDialog() }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "About BiasharaHub",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Quick navigation links

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        OutlinedButton(
                            onClick = onNavigateToTrends,
                            border = BorderStroke(
                                1.dp,
                                Color.White.copy(alpha = 0.4f)
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            )
                        ) {
                            Icon(
                                Icons.Default.TrendingUp,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text("Market Trends", fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = onNavigateToStories,
                            border = BorderStroke(
                                1.dp,
                                Color.White.copy(alpha = 0.4f)
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            )
                        ) {
                            Icon(
                                Icons.Default.AutoStories,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text("Success Stories", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // ---------------------------------------------------------
        // DISCOVERY MODE
        // ---------------------------------------------------------

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-8).dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SurfaceCard
                ),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Column(modifier = Modifier.padding(15.dp)) {

                    Text(
                        text = "CHOOSE YOUR DISCOVERY MODE",
                        color = TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                SurfaceVariantWarm,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(4.dp)
                    ) {
                        DiscoveryMode.values().forEach { mode ->

                            val selected = uiState.discoveryMode == mode

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (selected) SciRedAccent
                                        else Color.Transparent,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        viewModel.setDiscoveryMode(mode)
                                    }
                                    .padding(
                                        horizontal = 8.dp,
                                        vertical = 11.dp
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = if (
                                        mode == DiscoveryMode.QUICK
                                    ) {
                                        Icons.Default.Bolt
                                    } else {
                                        Icons.Default.Tune
                                    },
                                    contentDescription = null,
                                    tint = if (selected) {
                                        Color.White
                                    } else {
                                        SciBluePrimary
                                    },
                                    modifier = Modifier.size(20.dp)
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = if (
                                        mode == DiscoveryMode.QUICK
                                    ) "Quick Discovery"
                                    else "Detailed Profile",
                                    color = if (selected) {
                                        Color.White
                                    } else {
                                        TextPrimary
                                    },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )

                                Text(
                                    text = if (
                                        mode == DiscoveryMode.QUICK
                                    ) "Simple matching"
                                    else "More filters",
                                    color = if (selected) {
                                        Color.White.copy(alpha = 0.8f)
                                    } else {
                                        TextSecondary
                                    },
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // ---------------------------------------------------------
        // SECTION 1: SKILLS
        // ---------------------------------------------------------

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 5.dp)
                    .testTag("skills_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SurfaceCard
                ),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    MatcherSectionHeading(
                        number = "01",
                        title = "Your Skills & Talents",
                        subtitle = "What can you already do?",
                        badge = "${uiState.selectedSkillIds.size} selected"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.allSkills.take(18).forEach { skill ->

                            val selected =
                                uiState.selectedSkillIds.contains(skill.id)

                            SkillChip(
                                text = skill.name,
                                isSelected = selected,
                                onClick = {
                                    viewModel.toggleSkill(skill.id)
                                },
                                modifier = Modifier.testTag(
                                    "skill_chip_${skill.id}"
                                )
                            )
                        }
                    }

                    if (uiState.customSkills.isNotEmpty()) {

                        Spacer(modifier = Modifier.height(15.dp))

                        Text(
                            text = "YOUR CUSTOM SKILLS",
                            color = SciBluePrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(7.dp),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            uiState.customSkills.forEach { custom ->

                                Surface(
                                    color = SciBlueContainer,
                                    shape = RoundedCornerShape(20.dp),
                                    border = BorderStroke(
                                        1.dp,
                                        SciBluePrimary.copy(alpha = 0.18f)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(
                                            start = 11.dp,
                                            end = 3.dp,
                                            top = 3.dp,
                                            bottom = 3.dp
                                        ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = custom,
                                            color = SciBluePrimary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium
                                        )

                                        IconButton(
                                            onClick = {
                                                viewModel.removeCustomSkill(custom)
                                            },
                                            modifier = Modifier.size(25.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Remove $custom",
                                                tint = SciRedAccent,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    OutlinedTextField(
                        value = customSkillInput,
                        onValueChange = {
                            customSkillInput = it
                        },
                        placeholder = {
                            Text(
                                "Add another skill...",
                                fontSize = 12.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.AddCircleOutline,
                                contentDescription = null,
                                tint = SciBluePrimary
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_skill_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            if (customSkillInput.isNotBlank()) {
                                viewModel.addCustomSkill(
                                    customSkillInput.trim()
                                )
                                customSkillInput = ""
                            }
                        },
                        enabled = customSkillInput.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .testTag("add_custom_skill_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SciRedAccent
                        )
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(
                            "Add Custom Skill",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // ---------------------------------------------------------
        // SECTION 2: CAPITAL
        // ---------------------------------------------------------

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SurfaceCard
                ),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    MatcherSectionHeading(
                        number = "02",
                        title = "Your Starting Capital",
                        subtitle = "How much can you invest?"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = exactCapitalText,
                        onValueChange = { input ->
                            val cleanInput = input.filter { it.isDigit() }
                            exactCapitalText = cleanInput
                            viewModel.setExactCapital(
                                cleanInput.toIntOrNull()
                            )
                        },
                        label = {
                            Text("Available capital (KSh)")
                        },
                        placeholder = {
                            Text("e.g. 15000")
                        },
                        leadingIcon = {
                            Text(
                                text = "KSh",
                                color = SciRedAccent,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        },
                        trailingIcon = {
                            if (exactCapitalText.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        exactCapitalText = ""
                                        viewModel.setExactCapital(null)
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = "Clear capital"
                                    )
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("exact_capital_input")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "QUICK CAPITAL RANGES",
                        color = TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )

                    Spacer(modifier = Modifier.height(9.dp))

                    Row(
                        modifier = Modifier.horizontalScroll(
                            rememberScrollState()
                        ),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        FilterChip(
                            selected = uiState.selectedCapitalTier == null &&
                                uiState.exactCapitalKes == null,
                            onClick = {
                                exactCapitalText = ""
                                viewModel.setCapitalTier(null)
                            },
                            label = { Text("Any Budget") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SciRedAccent,
                                selectedLabelColor = Color.White
                            )
                        )

                        CapitalTier.values().forEach { tier ->

                            FilterChip(
                                selected = uiState.selectedCapitalTier == tier,
                                onClick = {
                                    exactCapitalText = tier.maxKes.toString()
                                    viewModel.setCapitalTier(tier)
                                },
                                label = {
                                    Text(tier.label)
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SciRedAccent,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = KenyaGoldDark,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "Your recommendations adjust to your available capital.",
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        // ---------------------------------------------------------
        // SECTION 3: INTERESTS
        // ---------------------------------------------------------

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SurfaceCard
                ),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    MatcherSectionHeading(
                        number = "03",
                        title = "Areas of Interest",
                        subtitle = "Which industries excite you?"
                    )

                    Spacer(modifier = Modifier.height(13.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(7.dp),
                        verticalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        InterestArea.values().forEach { interest ->

                            val selected =
                                uiState.selectedInterests.contains(interest)

                            FilterChip(
                                selected = selected,
                                onClick = {
                                    viewModel.toggleInterest(interest)
                                },
                                label = {
                                    Text(
                                        interest.shengLabel,
                                        fontSize = 11.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SciBluePrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }

        // ---------------------------------------------------------
        // DETAILED PROFILE FILTERS
        // ---------------------------------------------------------

        if (uiState.discoveryMode == DiscoveryMode.DETAILED) {

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SurfaceCard
                    ),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = SciBlueContainer,
                                shape = RoundedCornerShape(9.dp)
                            ) {
                                Icon(
                                    Icons.Default.Tune,
                                    contentDescription = null,
                                    tint = SciBluePrimary,
                                    modifier = Modifier
                                        .padding(9.dp)
                                        .size(19.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    "Advanced Preferences",
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    "Fine-tune your business matches",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 14.dp),
                            color = SurfaceBorder
                        )

                        // Equipment

                        Text(
                            "EQUIPMENT & RESOURCES",
                            color = SciBluePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 0.8.sp
                        )

                        Spacer(modifier = Modifier.height(9.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(7.dp),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            EquipmentResource.values().forEach { equip ->

                                FilterChip(
                                    selected = uiState.selectedEquipmentIds
                                        .contains(equip.id),
                                    onClick = {
                                        viewModel.toggleEquipment(equip.id)
                                    },
                                    label = {
                                        Text(
                                            equip.label,
                                            fontSize = 10.sp
                                        )
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Available time

                        Text(
                            "TIME AVAILABLE PER WEEK",
                            color = SciBluePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 0.8.sp
                        )

                        Spacer(modifier = Modifier.height(9.dp))

                        Row(
                            modifier = Modifier.horizontalScroll(
                                rememberScrollState()
                            ),
                            horizontalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            TimeCommitment.values().forEach { time ->

                                FilterChip(
                                    selected = uiState.availableTime == time,
                                    onClick = {
                                        viewModel.setTimeCommitment(
                                            if (uiState.availableTime == time) {
                                                null
                                            } else {
                                                time
                                            }
                                        )
                                    },
                                    label = {
                                        Text(time.label, fontSize = 10.sp)
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Business model

                        Text(
                            "PREFERRED BUSINESS MODEL",
                            color = SciBluePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 0.8.sp
                        )

                        Spacer(modifier = Modifier.height(9.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(7.dp),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            BusinessModelType.values().forEach { model ->

                                FilterChip(
                                    selected = uiState.preferredBusinessModel == model,
                                    onClick = {
                                        viewModel.setPreferredBusinessModel(
                                            if (
                                                uiState.preferredBusinessModel == model
                                            ) null else model
                                        )
                                    },
                                    label = {
                                        Text(model.label, fontSize = 10.sp)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // ---------------------------------------------------------
        // RESULTS INTRODUCTION
        // ---------------------------------------------------------

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 18.dp
                    )
            ) {
                Text(
                    text = "Your Business Matches",
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Opportunities ranked using your skills, interests and available capital.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    color = SciBlueContainer,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = SciBluePrimary,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(7.dp))

                        Text(
                            text = "Match percentages indicate profile compatibility, not guaranteed profit or investment returns.",
                            color = SciBluePrimary,
                            fontSize = 10.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // ---------------------------------------------------------
        // READY NOW
        // ---------------------------------------------------------

        if (uiState.readyNowIdeas.isNotEmpty()) {

            item {
                MatcherResultHeading(
                    title = "Ready Now",
                    subtitle = "Opportunities that align with your current resources",
                    count = uiState.readyNowIdeas.size,
                    color = StatusReadyGreen,
                    icon = Icons.Default.CheckCircle
                )
            }

            val displayedReadyNow = if (showAllReadyNow) {
                uiState.readyNowIdeas
            } else {
                uiState.readyNowIdeas.take(3)
            }

            items(displayedReadyNow) { idea ->

                EnhancedBusinessIdeaCard(
                    idea = idea,
                    isSaved = uiState.savedIdeaIds.contains(idea.id),
                    isCompared = uiState.comparedIdeaIds.contains(idea.id),
                    onCardClick = {
                        viewModel.selectDetailIdea(idea)
                    },
                    onToggleSave = {
                        viewModel.toggleSaveIdea(idea.id)
                    },
                    onToggleCompare = {
                        viewModel.toggleCompareIdea(idea.id)
                    },
                    onLoadIntoCalculator = {
                        viewModel.loadIdeaIntoCalculator(idea)
                    },
                    onStartJourney = {
                        viewModel.openJourneyForIdea(idea)
                    },
                    onBuildBusinessPlan = {
                        viewModel.openBusinessPlanDraft(idea)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("matched_idea_${idea.id}")
                )
            }

            if (uiState.readyNowIdeas.size > 3) {
                item {
                    TextButton(
                        onClick = {
                            showAllReadyNow = !showAllReadyNow
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            if (showAllReadyNow) {
                                "Show Top 3"
                            } else {
                                "Explore All ${uiState.readyNowIdeas.size} Ready Ideas"
                            },
                            color = SciRedAccent,
                            fontWeight = FontWeight.Bold
                        )

                        Icon(
                            if (showAllReadyNow) {
                                Icons.Default.KeyboardArrowUp
                            } else {
                                Icons.Default.KeyboardArrowDown
                            },
                            contentDescription = null,
                            tint = SciRedAccent
                        )
                    }
                }
            }
        }

        // ---------------------------------------------------------
        // PREPARATION NEEDED
        // ---------------------------------------------------------

        if (uiState.prepNeededIdeas.isNotEmpty()) {

            item {
                MatcherResultHeading(
                    title = "Preparation Needed",
                    subtitle = "Good opportunities that may require extra preparation",
                    count = uiState.prepNeededIdeas.size,
                    color = StatusPrepYellow,
                    icon = Icons.Default.Construction
                )
            }

            items(uiState.prepNeededIdeas) { idea ->

                EnhancedBusinessIdeaCard(
                    idea = idea,
                    isSaved = uiState.savedIdeaIds.contains(idea.id),
                    isCompared = uiState.comparedIdeaIds.contains(idea.id),
                    onCardClick = {
                        viewModel.selectDetailIdea(idea)
                    },
                    onToggleSave = {
                        viewModel.toggleSaveIdea(idea.id)
                    },
                    onToggleCompare = {
                        viewModel.toggleCompareIdea(idea.id)
                    },
                    onLoadIntoCalculator = {
                        viewModel.loadIdeaIntoCalculator(idea)
                    },
                    onStartJourney = {
                        viewModel.openJourneyForIdea(idea)
                    },
                    onBuildBusinessPlan = {
                        viewModel.openBusinessPlanDraft(idea)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }

        // ---------------------------------------------------------
        // FUTURE OPPORTUNITIES
        // ---------------------------------------------------------

        if (uiState.futureIdeas.isNotEmpty()) {

            item {
                MatcherResultHeading(
                    title = "Future Opportunities",
                    subtitle = "Ideas to work towards as you grow",
                    count = uiState.futureIdeas.size,
                    color = StatusFutureBlue,
                    icon = Icons.Default.TrendingUp
                )
            }

            items(uiState.futureIdeas) { idea ->

                EnhancedBusinessIdeaCard(
                    idea = idea,
                    isSaved = uiState.savedIdeaIds.contains(idea.id),
                    isCompared = uiState.comparedIdeaIds.contains(idea.id),
                    onCardClick = {
                        viewModel.selectDetailIdea(idea)
                    },
                    onToggleSave = {
                        viewModel.toggleSaveIdea(idea.id)
                    },
                    onToggleCompare = {
                        viewModel.toggleCompareIdea(idea.id)
                    },
                    onLoadIntoCalculator = {
                        viewModel.loadIdeaIntoCalculator(idea)
                    },
                    onStartJourney = {
                        viewModel.openJourneyForIdea(idea)
                    },
                    onBuildBusinessPlan = {
                        viewModel.openBusinessPlanDraft(idea)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }

        // ---------------------------------------------------------
        // EMPTY RESULTS
        // ---------------------------------------------------------

        if (
            uiState.readyNowIdeas.isEmpty() &&
            uiState.prepNeededIdeas.isEmpty() &&
            uiState.futureIdeas.isEmpty()
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SurfaceCard
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            color = SciRedContainer,
                            shape = CircleShape
                        ) {
                            Icon(
                                Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = SciRedAccent,
                                modifier = Modifier
                                    .padding(18.dp)
                                    .size(34.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            "Let's find your opportunity",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            "Try selecting more skills, adjusting your capital or exploring different interests.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        item {
            DeveloperFooter()
        }
    }
}

// =====================================================================
// MATCHER SECTION HEADING
// =====================================================================

@Composable
private fun MatcherSectionHeading(
    number: String,
    title: String,
    subtitle: String,
    badge: String? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = SciRedContainer,
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = number,
                modifier = Modifier.padding(
                    horizontal = 11.dp,
                    vertical = 9.dp
                ),
                color = SciRedAccent,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.width(11.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 11.sp
            )
        }

        if (badge != null) {
            Surface(
                color = SciBlueContainer,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = badge,
                    modifier = Modifier.padding(
                        horizontal = 9.dp,
                        vertical = 5.dp
                    ),
                    color = SciBluePrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// =====================================================================
// MATCHER RESULT HEADING
// =====================================================================

@Composable
private fun MatcherResultHeading(
    title: String,
    subtitle: String,
    count: Int,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 18.dp,
                bottom = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = color.copy(alpha = 0.12f),
            shape = RoundedCornerShape(11.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .padding(10.dp)
                    .size(21.dp)
            )
        }

        Spacer(modifier = Modifier.width(11.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = color,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )

            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 10.sp
            )
        }

        Surface(
            color = color.copy(alpha = 0.12f),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = count.toString(),
                modifier = Modifier.padding(
                    horizontal = 11.dp,
                    vertical = 6.dp
                ),
                color = color,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp
            )
        }
    }
}

// =====================================================================
// BUSINESS IDEA CARD
// Keep this public API stable: ExploreScreen and BrowseIdeasScreen use it.
// =====================================================================

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
        modifier = modifier.clickable {
            onCardClick()
        },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceCard
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {

            // -----------------------------------------------------
            // CARD HEADER
            // -----------------------------------------------------

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FeasibilityBadge(
                        group = idea.feasibilityGroup
                    )

                    Surface(
                        shape = RoundedCornerShape(7.dp),
                        color = SciBlueContainer
                    ) {
                        Text(
                            text = idea.shengTitle,
                            modifier = Modifier.padding(
                                horizontal = 8.dp,
                                vertical = 5.dp
                            ),
                            color = SciBluePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onToggleCompare,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CompareArrows,
                            contentDescription = "Compare",
                            tint = if (isCompared) {
                                SciRedAccent
                            } else {
                                TextMuted
                            },
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onToggleSave,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isSaved) {
                                Icons.Filled.Bookmark
                            } else {
                                Icons.Outlined.BookmarkBorder
                            },
                            contentDescription = if (isSaved) {
                                "Remove from saved"
                            } else {
                                "Save idea"
                            },
                            tint = if (isSaved) {
                                SciRedAccent
                            } else {
                                TextMuted
                            },
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // -----------------------------------------------------
            // TITLE AND MATCH SCORE
            // -----------------------------------------------------

            Text(
                text = idea.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(7.dp))

            MatchScoreBadge(
                score = idea.matchScore
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = idea.summary,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    lineHeight = 19.sp
                ),
                maxLines = 3
            )

            // -----------------------------------------------------
            // RECOMMENDATION EXPLANATION
            // -----------------------------------------------------

            idea.explanation?.let { exp ->

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = SciBlueContainer.copy(alpha = 0.65f)
                ) {
                    Column(
                        modifier = Modifier.padding(11.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = SciBluePrimary,
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = "WHY THIS MATCH?",
                                color = SciBluePrimary,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 10.sp,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(7.dp))

                        if (exp.matchedSkillNames.isNotEmpty()) {
                            Text(
                                text = "Matching skills: ${exp.matchedSkillNames.joinToString(", ")}",
                                color = TextPrimary,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }

                        Text(
                            text = "Capital analysis: ${exp.capitalStatusText}",
                            color = TextPrimary,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Next step: ${exp.recommendedNextStep}",
                            color = KenyaGreenDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // -----------------------------------------------------
            // FINANCIAL METRICS
            // -----------------------------------------------------

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
                    label = "Estimated Monthly Net",
                    color = KenyaGreenPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // -----------------------------------------------------
            // ACTIONS
            // -----------------------------------------------------

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onLoadIntoCalculator,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(
                        horizontal = 8.dp,
                        vertical = 7.dp
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Calculate,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        "Calculator",
                        fontSize = 10.sp
                    )
                }

                OutlinedButton(
                    onClick = onStartJourney,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(
                        horizontal = 7.dp,
                        vertical = 7.dp
                    ),
                    modifier = Modifier.weight(1.15f)
                ) {
                    Icon(
                        Icons.Default.Checklist,
                        contentDescription = null,
                        tint = KenyaGreenPrimary,
                        modifier = Modifier.size(15.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        "6-Stage Plan",
                        color = KenyaGreenPrimary,
                        fontSize = 10.sp
                    )
                }

                Button(
                    onClick = onBuildBusinessPlan,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SciRedAccent
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 8.dp,
                        vertical = 7.dp
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Blueprint",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(3.dp))

                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}