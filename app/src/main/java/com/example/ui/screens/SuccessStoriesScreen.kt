package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EntrepreneurProfile
import com.example.model.ProfileVerificationStatus
import com.example.ui.components.DeveloperFooter
import com.example.ui.components.KesTag
import com.example.ui.theme.*
import com.example.viewmodel.BiasharaUiState
import com.example.viewmodel.BiasharaViewModel

@Composable
fun SuccessStoriesScreen(
    uiState: BiasharaUiState,
    viewModel: BiasharaViewModel,
    modifier: Modifier = Modifier
) {
    var showRequestSheet by remember { mutableStateOf(false) }
    var selectedMentor by remember { mutableStateOf<EntrepreneurProfile?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("success_stories_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Header Banner with SCI BASE Accent
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(SciBluePrimary, Color(0xFF0A1854))
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
                            text = "Vijana Waliofaulu",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = KenyaGold,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Kenyan Youth Success Stories",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Authentic and verified case studies of young entrepreneurs across Kenyan counties who bootstrapped with basic skills. All claims are cited with source studies and last-verified dates.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }

        // Active Mentorship Requests Notice
        if (uiState.mentorshipRequests.isNotEmpty()) {
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
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Active",
                                tint = SciBluePrimary
                            )
                            Text(
                                text = "Your Mentorship Applications (${uiState.mentorshipRequests.size})",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = OnSciBlueContainer
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        uiState.mentorshipRequests.takeLast(2).forEach { req ->
                            Text(
                                text = "• Mentor: ${req.mentorName} | Status: ${req.status} via ${req.preferredMode}",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                            )
                        }
                    }
                }
            }
        }

        // Entrepreneur Profiles
        items(uiState.successStories) { profile ->
            VerifiedProfileCard(
                profile = profile,
                onRequestMentorship = {
                    selectedMentor = profile
                    showRequestSheet = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("story_card_${profile.id}")
            )
        }

        // Developer Footer
        item {
            DeveloperFooter()
        }
    }

    // Mentorship Request Sheet
    if (showRequestSheet && selectedMentor != null) {
        MentorshipRequestDialog(
            mentor = selectedMentor!!,
            onDismiss = {
                showRequestSheet = false
                selectedMentor = null
            },
            onSubmit = { name, phone, venture, question, mode ->
                viewModel.openMentorshipDialog(selectedMentor!!)
                viewModel.submitMentorshipRequest(name, phone, venture, question, mode)
                showRequestSheet = false
                selectedMentor = null
            }
        )
    }
}

@Composable
fun VerifiedProfileCard(
    profile: EntrepreneurProfile,
    onRequestMentorship: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Profile Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(SciBluePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = profile.avatarInitials,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Column {
                        Text(
                            text = "${profile.name}, ${profile.age}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "📍 ${profile.county}",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = KenyaGold.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = profile.category,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = KenyaGoldDark
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Verification Status Badge
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(profile.verificationStatus.badgeColorHex).copy(alpha = 0.12f)
            ) {
                Text(
                    text = "✓ ${profile.verificationStatus.label}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(profile.verificationStatus.badgeColorHex),
                        fontSize = 10.sp
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Venture Name Badge
            Text(
                text = "Venture: ${profile.ventureName}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = SciBluePrimary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Capital Started vs Current Revenue
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KesTag(
                    amountText = "KSh ${"%,d".format(profile.startedWithKes)}",
                    label = "Started With",
                    color = Color(0xFF6B7280),
                    modifier = Modifier.weight(1f)
                )

                KesTag(
                    amountText = profile.currentMonthlyRevenueKes,
                    label = "Monthly Revenue",
                    color = KenyaGreenPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // The Journey
            Text(
                text = "The Entrepreneur Journey:",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = profile.journeyStory,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Key Challenges
            Text(
                text = "Challenges Overcome:",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            profile.keyChallenges.forEach { challenge ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("⚠️", fontSize = 12.sp)
                    Text(
                        text = challenge,
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Golden Advice Card
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = SurfaceVariantWarm,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Advice",
                            tint = KenyaGoldDark,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Siri ya Success (Key Advice):",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = KenyaGoldDark
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "\"${profile.keyAdvice}\"",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontStyle = FontStyle.Italic,
                            color = TextPrimary,
                            lineHeight = 18.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Source Citation: ${profile.citationSource} • Verified ${profile.lastVerifiedDate}",
                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 9.sp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Mentorship Request Call To Action
            Button(
                onClick = onRequestMentorship,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SciBluePrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("request_mentorship_btn_${profile.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Handshake,
                    contentDescription = "Mentorship",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Request Mentorship (${profile.mentorshipFocus})",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorshipRequestDialog(
    mentor: EntrepreneurProfile,
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var ventureOrSkill by remember { mutableStateOf(mentor.primarySkillsUsed.firstOrNull() ?: "") }
    var question by remember { mutableStateOf("") }
    var selectedMode by remember { mutableStateOf("WhatsApp Voice/Call") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "Request Advisory & Mentorship",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = SciBluePrimary
                    )
                )
                Text(
                    text = "Advisory Focus: ${mentor.mentorshipFocus}",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SciBlueContainer
                ) {
                    Text(
                        text = "ℹ️ ${mentor.mentorshipDisclaimer}",
                        style = MaterialTheme.typography.bodySmall.copy(color = OnSciBlueContainer, fontSize = 10.sp),
                        modifier = Modifier.padding(8.dp)
                    )
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Your Full Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = contact,
                    onValueChange = { contact = it },
                    label = { Text("WhatsApp Phone or Email") },
                    placeholder = { Text("e.g. 0712 345 678") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = ventureOrSkill,
                    onValueChange = { ventureOrSkill = it },
                    label = { Text("Your Current Skill or Business Idea") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Specific Question / Current Blocker") },
                    placeholder = { Text("e.g. How do I acquire my first 10 customers in my estate?") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Preferred Communication Mode:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("WhatsApp Call", "Google Meet", "Coffee Meet").forEach { mode ->
                        FilterChip(
                            selected = selectedMode == mode,
                            onClick = { selectedMode = mode },
                            label = { Text(mode, fontSize = 11.sp) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && contact.isNotBlank()) {
                        onSubmit(name, contact, ventureOrSkill, question, selectedMode)
                    }
                },
                enabled = name.isNotBlank() && contact.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = SciBluePrimary)
            ) {
                Text("Submit Request")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
