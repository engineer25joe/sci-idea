package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Skill
import com.example.ui.components.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun OnboardingFlow(
    availableSkills: List<Skill>,
    initialSelectedSkillIds: Set<String> = emptySet(),
    onFinishOnboarding: (
        name: String,
        email: String,
        selectedSkills: Set<String>,
        capital: Int?
    ) -> Unit
) {
    // Stage: 1 = Skills, 2 = Capital, 3 = Guidance
    var currentStep by remember { mutableStateOf(1) }

    // Restore previously saved selections from the backend.
    var selectedSkillIds by remember(initialSelectedSkillIds) {
        mutableStateOf(initialSelectedSkillIds)
    }

    var selectedCapitalKes by remember {
        mutableStateOf<Int?>(25000)
    }

    var userName by remember {
        mutableStateOf("Joe Dev")
    }

    var userEmail by remember {
        mutableStateOf("joe.dev@example.com")
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when (currentStep) {
            0 -> SplashScreen(
                onGetStarted = { currentStep = 1 }
            )
            1 -> OnboardingStepOne(
                availableSkills = availableSkills,
                selectedSkillIds = selectedSkillIds,
                onToggleSkill = { id ->
                    selectedSkillIds = if (selectedSkillIds.contains(id)) {
                        selectedSkillIds - id
                    } else {
                        selectedSkillIds + id
                    }
                },
                onSkip = {
                    onFinishOnboarding(userName, userEmail, selectedSkillIds, selectedCapitalKes)
                },
                onNext = { currentStep = 2 }
            )
            2 -> OnboardingStepTwo(
                selectedCapital = selectedCapitalKes,
                onSelectCapital = { capital -> selectedCapitalKes = capital },
                onSkip = {
                    onFinishOnboarding(userName, userEmail, selectedSkillIds, selectedCapitalKes)
                },
                onNext = { currentStep = 3 }
            )
            3 -> OnboardingStepThree(
                onSkip = {
                    onFinishOnboarding(userName, userEmail, selectedSkillIds, selectedCapitalKes)
                },
                onGetStarted = {
                    onFinishOnboarding(userName, userEmail, selectedSkillIds, selectedCapitalKes)
                }
            )
        }
    }
}

// -------------------------------------------------------------
// Screen 1: Splash Screen
// -------------------------------------------------------------
@Composable
fun SplashScreen(
    onGetStarted: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Kenyan flag banner at top
        KenyanFlagRibbon(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Circular Sprout Logo Badge
        BiasharaLogoBadge(sizeDp = 92)

        Spacer(modifier = Modifier.height(16.dp))

        // Title and Subtitle
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "BiasharaHub ",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF111827)
            )
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = KenyanGreen
            ) {
                Text(
                    text = "KE",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Discover Ideas. Build Skills.\nExplore Opportunities.",
            fontSize = 14.sp,
            color = Color(0xFF6B7280),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        // Nairobi Skyline Illustration
        NairobiSkylineIllustration(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your gateway to business opportunities\nin Kenya",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF374151),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Pagination 3 Dots
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(KenyanGreen)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD1D5DB))
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD1D5DB))
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Get Started Button
        Button(
            onClick = onGetStarted,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
                .height(52.dp)
                .testTag("splash_get_started_button"),
            colors = ButtonDefaults.buttonColors(containerColor = KenyanGreen),
            shape = RoundedCornerShape(26.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))
    }
}

// -------------------------------------------------------------
// Screen 2: Onboarding 1 - Skills & Interests
// -------------------------------------------------------------

@Composable
fun OnboardingStepOne(
    availableSkills: List<Skill>,
    selectedSkillIds: Set<String>,
    onToggleSkill: (String) -> Unit,
    onSkip: () -> Unit,
    onNext: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredSkills = availableSkills.filter { skill ->
        skill.name.contains(searchQuery, ignoreCase = true) ||
        skill.description.contains(searchQuery, ignoreCase = true) ||
        skill.category.displayName.contains(searchQuery, ignoreCase = true)
    }

    val groupedSkills = filteredSkills
        .groupBy { it.category }
        .toList()
        .sortedBy { it.first.displayName }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .testTag("discover_talents_screen")
    ) {

        Spacer(Modifier.height(22.dp))

        // HERO SECTION
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = KenyanGreen
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                KenyanGreen,
                                Color(0xFF064E3B)
                            )
                        )
                    )
                    .padding(22.dp)
            ) {
                Column {

                    Text(
                        text = "SCI IDEA",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Discover Your Talents!",
                        color = Color.White,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 33.sp
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = "Your skills could be the beginning of your next business opportunity. Tell us what you can do.",
                        color = Color.White.copy(alpha = 0.92f),
                        fontSize = 14.sp,
                        lineHeight = 21.sp
                    )

                    Spacer(Modifier.height(18.dp))

                    Surface(
                        color = Color.White.copy(alpha = 0.16f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "STEP 1 OF 3  •  SKILLS DISCOVERY",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            )
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // SECTION TITLE
        Text(
            text = "What are you good at?",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )

        Spacer(Modifier.height(5.dp))

        Text(
            text = "Select the skills you already have or would like to use.",
            fontSize = 13.sp,
            color = Color(0xFF6B7280)
        )

        Spacer(Modifier.height(16.dp))

        // SEARCH
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("skill_search_field"),
            placeholder = {
                Text("Search skills or categories...")
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search skills"
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { searchQuery = "" }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Clear search"
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = KenyanGreen,
                cursorColor = KenyanGreen
            )
        )

        Spacer(Modifier.height(18.dp))

        // SELECTED SKILLS COUNTER
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFE8F5E9),
            shape = RoundedCornerShape(14.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = KenyanGreen
                )

                Spacer(Modifier.width(10.dp))

                Column {
                    Text(
                        text = "${selectedSkillIds.size} skills selected",
                        fontWeight = FontWeight.Bold,
                        color = KenyanGreen,
                        fontSize = 14.sp
                    )

                    Text(
                        text = "Your selections help personalize your opportunities.",
                        color = Color(0xFF4B5563),
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Explore Categories",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )

        Spacer(Modifier.height(5.dp))

        Text(
            text = "Explore skills from the available catalogue.",
            fontSize = 12.sp,
            color = Color(0xFF6B7280)
        )

        Spacer(Modifier.height(14.dp))

        if (availableSkills.isEmpty()) {

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFF9FAFB),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.CloudOff,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(36.dp)
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = "Skills are not available yet.",
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Connect to the internet and try loading the catalogue again.",
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                }
            }

        } else if (groupedSkills.isEmpty()) {

            Text(
                text = "No matching skills found.",
                color = Color.Gray,
                fontSize = 14.sp
            )

        } else {

            groupedSkills.forEach { (category, skills) ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF8FAFC)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color(0xFFE5E7EB)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = category.displayName,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = KenyanGreen,
                                modifier = Modifier.weight(1f)
                            )

                            Text(
                                text = "${skills.size} skills",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }

                        // Dynamic category overview based on catalogue content
                        val categoryDescription = skills
                            .map { it.description.trim() }
                            .filter { it.isNotBlank() }
                            .distinct()
                            .firstOrNull()

                        if (!categoryDescription.isNullOrBlank()) {
                            Spacer(Modifier.height(6.dp))

                            Text(
                                text = categoryDescription,
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280),
                                lineHeight = 18.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        skills.forEach { skill ->

                            val selected =
                                skill.id in selectedSkillIds

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        onToggleSkill(skill.id)
                                    }
                                    .testTag("skill_item_${skill.id}"),
                                shape = RoundedCornerShape(13.dp),
                                color = if (selected) {
                                    Color(0xFFE8F5E9)
                                } else {
                                    Color.White
                                },
                                border = androidx.compose.foundation.BorderStroke(
                                    if (selected) 1.5.dp else 1.dp,
                                    if (selected) KenyanGreen
                                    else Color(0xFFE5E7EB)
                                )
                            ) {

                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Icon(
                                        imageVector = if (selected) {
                                            Icons.Default.CheckCircle
                                        } else {
                                            Icons.Default.AddCircleOutline
                                        },
                                        contentDescription = null,
                                        tint = if (selected) {
                                            KenyanGreen
                                        } else {
                                            Color.Gray
                                        }
                                    )

                                    Spacer(Modifier.width(11.dp))

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        Text(
                                            text = skill.name,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF111827)
                                        )

                                        if (skill.description.isNotBlank()) {
                                            Spacer(Modifier.height(3.dp))

                                            Text(
                                                text = skill.description,
                                                fontSize = 11.sp,
                                                color = Color(0xFF6B7280),
                                                lineHeight = 16.sp,
                                                maxLines = 3,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        DotsProgressIndicator(activeIndex = 0)

        Spacer(Modifier.height(18.dp))

        BottomNavigationRow(
            onSkip = onSkip,
            onNext = onNext,
            nextText = "Explore Opportunities",
            nextTestTag = "onboarding_1_next"
        )

        Spacer(Modifier.height(25.dp))
    }
}

// -------------------------------------------------------------
// Screen 3: Onboarding 2 - Budget / Capital
// -------------------------------------------------------------
@Composable
fun OnboardingStepTwo(
    selectedCapital: Int?,
    onSelectCapital: (Int) -> Unit,
    onSkip: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Young Kenyan man with laptop illustration
        OnboardingAvatarIllustration(step = 2)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Explore Opportunities\nBased on Your Budget",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827),
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Find ideas you can start with limited capital, and get practical guidance to grow.",
            fontSize = 14.sp,
            color = Color(0xFF6B7280),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Capital Tiers Selection
        Text(
            text = "What is your target starting capital?",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = KenyanGreen
        )

        Spacer(modifier = Modifier.height(10.dp))

        val capitalOptions = listOf(
            Triple(5000, "Under KSh 10,000", "Micro-capital / Hustler Fund"),
            Triple(25000, "KSh 10,000 - 50,000", "Low capital (Urban Farming, Fast Food)"),
            Triple(80000, "KSh 50,000 - 200,000", "Medium capital (Accessories, Bakery)"),
            Triple(200000, "KSh 200,000+", "High Growth (Clean Energy, Full Shop)")
        )

        capitalOptions.forEach { (amount, title, sub) ->
            val isSelected = selectedCapital == amount
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onSelectCapital(amount) }
                    .testTag("capital_option_$amount"),
                shape = RoundedCornerShape(14.dp),
                color = if (isSelected) Color(0xFFE8F5E9) else Color(0xFFF9FAFB),
                border = androidx.compose.foundation.BorderStroke(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) KenyanGreen else Color(0xFFE5E7EB)
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelectCapital(amount) },
                        colors = RadioButtonDefaults.colors(selectedColor = KenyanGreen)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                        Text(
                            text = sub,
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4 dots indicator (dot 2 active)
        DotsProgressIndicator(activeIndex = 1)

        Spacer(modifier = Modifier.height(24.dp))

        // Bottom row
        BottomNavigationRow(
            onSkip = onSkip,
            onNext = onNext,
            nextText = "Next",
            nextTestTag = "onboarding_2_next"
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// -------------------------------------------------------------
// Screen 4: Onboarding 3 - Complete Business Guidance
// -------------------------------------------------------------
@Composable
fun OnboardingStepThree(
    onSkip: () -> Unit,
    onGetStarted: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Female entrepreneur illustration with checklist
        OnboardingAvatarIllustration(step = 3)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Get Complete\nBusiness Guidance",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827),
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Learn about startup requirements, potential customers, marketing strategies and steps to get started.",
            fontSize = 14.sp,
            color = Color(0xFF6B7280),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        // 4 Guidance Cards
        val guidancePillars = listOf(
            Triple(Icons.Default.Inventory2, "Startup Requirements", "Capital breakdown, equipment & county permits"),
            Triple(Icons.Default.People, "Potential Customers", "Exact target segments & neighbourhood buyers"),
            Triple(Icons.Default.Campaign, "Marketing Strategies", "TikTok reels, estate WhatsApp & M-PESA tactics"),
            Triple(Icons.Default.TrendingUp, "Next Steps", "From day 1 trial to full weekly revenue")
        )

        guidancePillars.forEach { (icon, title, desc) ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF9FAFB),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F5E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = KenyanGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                        Text(
                            text = desc,
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4 dots indicator (dot 3 active)
        DotsProgressIndicator(activeIndex = 2)

        Spacer(modifier = Modifier.height(24.dp))

        // Bottom row
        BottomNavigationRow(
            onSkip = onSkip,
            onNext = onGetStarted,
            nextText = "Next",
            nextTestTag = "onboarding_3_next"
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// -------------------------------------------------------------
// Helper UI: Dots Indicator & Bottom Row
// -------------------------------------------------------------
@Composable
private fun DotsProgressIndicator(activeIndex: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0..3) {
            val isActive = i == activeIndex
            Box(
                modifier = Modifier
                    .size(if (isActive) 8.dp else 6.dp)
                    .clip(CircleShape)
                    .background(if (isActive) KenyanGreen else Color(0xFFD1D5DB))
            )
            if (i < 3) {
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}

@Composable
private fun BottomNavigationRow(
    onSkip: () -> Unit,
    onNext: () -> Unit,
    nextText: String,
    nextTestTag: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = onSkip,
            modifier = Modifier.testTag("onboarding_skip_button")
        ) {
            Text(
                text = "Skip",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4B5563)
            )
        }

        Button(
            onClick = onNext,
            modifier = Modifier
                .width(120.dp)
                .height(46.dp)
                .testTag(nextTestTag),
            colors = ButtonDefaults.buttonColors(containerColor = KenyanGreen),
            shape = RoundedCornerShape(23.dp)
        ) {
            Text(
                text = nextText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
