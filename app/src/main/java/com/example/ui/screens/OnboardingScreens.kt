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

@Composable
fun OnboardingFlow(
    availableSkills: List<Skill>,
    onFinishOnboarding: (name: String, email: String, selectedSkills: Set<String>, capital: Int?) -> Unit
) {
    // Stage: 0 = Splash Screen, 1 = Onboarding 1 (Skills), 2 = Onboarding 2 (Capital), 3 = Onboarding 3 (Guidance)
    var currentStep by remember { mutableStateOf(0) }
    var selectedSkillIds by remember { mutableStateOf(setOf("horticulture", "cooking_baking", "graphic_design")) }
    var selectedCapitalKes by remember { mutableStateOf<Int?>(25000) }
    var userName by remember { mutableStateOf("John Doe") }
    var userEmail by remember { mutableStateOf("john.doe@example.com") }

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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Female Kenyan entrepreneur illustration with floating idea bubbles
        OnboardingAvatarIllustration(step = 1)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Find Business\nIdeas That Fit You",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827),
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Discover business opportunities based on your skills, interests and available capital.",
            fontSize = 14.sp,
            color = Color(0xFF6B7280),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Interactive Skill Selector
        Text(
            text = "Tap skills you have or want to learn:",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = KenyanGreen
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Quick Skills Flow Row
        val displaySkills = if (availableSkills.isNotEmpty()) availableSkills.take(8) else listOf(
            Skill("horticulture", "Urban Farming", com.example.model.SkillCategory.AGRIBUSINESS),
            Skill("cooking_baking", "Food & Bakery", com.example.model.SkillCategory.FOOD_HOSPITALITY),
            Skill("graphic_design", "Graphic Design", com.example.model.SkillCategory.DIGITAL_TECH),
            Skill("social_media", "Social Media", com.example.model.SkillCategory.DIGITAL_TECH),
            Skill("phone_repair", "Phone Repair", com.example.model.SkillCategory.DIGITAL_TECH),
            Skill("carpentry", "Carpentry", com.example.model.SkillCategory.VOCATIONAL_TRADES),
            Skill("tailoring", "Tailoring", com.example.model.SkillCategory.VOCATIONAL_TRADES),
            Skill("detergent_making", "Liquid Soap", com.example.model.SkillCategory.RETAIL_COMMERCE)
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            displaySkills.chunked(2).forEach { rowPair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowPair.forEach { skill ->
                        val isSelected = selectedSkillIds.contains(skill.id)
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onToggleSkill(skill.id) }
                                .testTag("onboarding_skill_${skill.id}"),
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) KenyanGreen else Color(0xFFF3F4F6),
                            border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.AddCircleOutline,
                                    contentDescription = null,
                                    tint = if (isSelected) Color.White else Color(0xFF6B7280),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = skill.name,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Color(0xFF374151),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4 dots indicator (dot 1 active)
        DotsProgressIndicator(activeIndex = 0)

        Spacer(modifier = Modifier.height(24.dp))

        // Bottom row: Skip & Next
        BottomNavigationRow(
            onSkip = onSkip,
            onNext = onNext,
            nextText = "Next",
            nextTestTag = "onboarding_1_next"
        )

        Spacer(modifier = Modifier.height(24.dp))
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
