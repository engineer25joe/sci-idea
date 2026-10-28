package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserActivityEvent
import com.example.ui.components.*
import com.example.viewmodel.BiasharaUiState
import com.example.viewmodel.BiasharaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(
    uiState: BiasharaUiState,
    viewModel: BiasharaViewModel,
    onNavigateToSaved: () -> Unit,
    onNavigateToStories: () -> Unit
) {
    var showActivityDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 96.dp)
    ) {
        // Top Green Header Banner with Avatar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(KenyanGreen)
                .padding(top = 40.dp, bottom = 28.dp)
        ) {
            // Settings gear on top right
            IconButton(
                onClick = { showSettingsDialog = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp)
                    .testTag("profile_top_settings_gear")
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Avatar and Profile Info
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Avatar",
                        tint = KenyanGreen,
                        modifier = Modifier.size(60.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = uiState.userProfileName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = uiState.userProfileEmail,
                    fontSize = 13.sp,
                    color = Color(0xFFD1FAE5)
                )
            }
        }

        // Body Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            // Card: "My Journey" (3/10 ideas explored)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_journey_card"),
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "My Journey",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                        Text(
                            text = "3 / 10",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = KenyanGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { 0.3f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = KenyanGreen,
                        trackColor = Color(0xFFE5E7EB)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Ideas explored",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Menu Items List
            ProfileMenuItem(
                icon = Icons.Outlined.BookmarkBorder,
                title = "My Saved Ideas",
                testTag = "profile_menu_saved",
                onClick = onNavigateToSaved
            )

            Spacer(modifier = Modifier.height(10.dp))

            ProfileMenuItem(
                icon = Icons.Outlined.Analytics,
                title = "My Activity & Tracking",
                badgeText = "${uiState.userActivities.size} events",
                testTag = "profile_menu_activity",
                onClick = { showActivityDialog = true }
            )

            Spacer(modifier = Modifier.height(10.dp))

            ProfileMenuItem(
                icon = Icons.Outlined.Settings,
                title = "Settings & Django Backend",
                testTag = "profile_menu_settings",
                onClick = { showSettingsDialog = true }
            )

            Spacer(modifier = Modifier.height(10.dp))

            ProfileMenuItem(
                icon = Icons.Outlined.HelpOutline,
                title = "Help & Support",
                testTag = "profile_menu_help",
                onClick = { showHelpDialog = true }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Log Out Button
            OutlinedButton(
                onClick = {
                    viewModel.resetOnboarding()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("profile_logout_button"),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD1D5DB)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF374151))
            ) {
                Text(
                    text = "Log Out",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Attribution & Copyright Footer
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Transparent
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Developed by 💕🙏Engineer Joe 🙏💕",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F268C),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "© 2026 SCI BASE COLLECTIONS. All rights reserved.",
                        fontSize = 11.sp,
                        color = Color(0xFF6B7280),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    // -------------------------------------------------------------
    // My Activity & Tracking Dialog (Telemetry Viewer & Django Sync)
    // -------------------------------------------------------------
    if (showActivityDialog) {
        AlertDialog(
            onDismissRequest = { showActivityDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Analytics, contentDescription = null, tint = KenyanGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("User Activity Tracking", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Real-time on-device telemetry logs tracked for your session. These events are queued and synced to the remote Django backend.",
                        fontSize = 13.sp,
                        color = Color(0xFF4B5563)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFE8F5E9),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Django Backend: ${uiState.djangoConnectionStatus}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = KenyanGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Recent Activity Logs (${uiState.userActivities.size}):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF374151)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    ) {
                        if (uiState.userActivities.isEmpty()) {
                            Text(
                                "No activity logs yet. Explore some business ideas to generate telemetry!",
                                fontSize = 12.sp,
                                color = Color(0xFF9CA3AF)
                            )
                        } else {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(uiState.userActivities.take(25)) { event ->
                                    val timeStr = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(event.timestampMs))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFF3F4F6),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = event.targetTitle ?: event.eventType,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF111827)
                                                )
                                                Text(
                                                    text = "Type: ${event.eventType} • $timeStr",
                                                    fontSize = 10.sp,
                                                    color = Color(0xFF6B7280)
                                                )
                                            }
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = if (event.isSyncedToDjango) Color(0xFFDCFCE7) else Color(0xFFFEF3C7)
                                            ) {
                                                Text(
                                                    text = if (event.isSyncedToDjango) "Synced" else "Queued",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (event.isSyncedToDjango) Color(0xFF166534) else Color(0xFF92400E),
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.triggerDjangoSync()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = KenyanGreen),
                    modifier = Modifier.testTag("sync_to_django_button")
                ) {
                    Text("Sync to Django Now")
                }
            },
            dismissButton = {
                TextButton(onClick = { showActivityDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    // -------------------------------------------------------------
    // Settings & Django Backend Config Dialog
    // -------------------------------------------------------------
    if (showSettingsDialog) {
        var urlInput by remember { mutableStateOf(uiState.djangoBackendUrl) }
        var nameInput by remember { mutableStateOf(uiState.userProfileName) }
        var emailInput by remember { mutableStateOf(uiState.userProfileEmail) }

        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = {
                Text("Settings & Remote Django", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text("Remote Management Backend URL:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = urlInput,
                        onValueChange = { urlInput = it },
                        modifier = Modifier.fillMaxWidth().testTag("settings_django_url_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = KenyanGreen)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            viewModel.updateDjangoBackendUrl(urlInput)
                            viewModel.testDjangoConnection()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = KenyanGreen),
                        modifier = Modifier.fillMaxWidth().testTag("settings_test_connection_button")
                    ) {
                        Text("Test Connection")
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("User Profile Info:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = {
                            viewModel.resetOnboarding()
                            showSettingsDialog = false
                        },
                        modifier = Modifier.fillMaxWidth().testTag("settings_restart_wizard_button")
                    ) {
                        Text("Restart Onboarding Wizard")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateDjangoBackendUrl(urlInput)
                        viewModel.saveUserProfile(nameInput, emailInput)
                        showSettingsDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = KenyanGreen)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // -------------------------------------------------------------
    // Help & Support Dialog
    // -------------------------------------------------------------
    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = {
                Text("Help, Support & Mentorship", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "BiasharaHub KE provides free guidance, capital breakdowns, and mentorship for Kenyan youth micro-enterprises.",
                        fontSize = 13.sp,
                        color = Color(0xFF4B5563)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Official Youth Support Hotlines:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("• Youth Enterprise Development Fund (YEDF): 0800 724 933", fontSize = 12.sp)
                    Text("• Uwezo Fund Kenya: *254# / uwezo.go.ke", fontSize = 12.sp)
                    Text("• Hustler Fund USSD: Dial *254#", fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            showHelpDialog = false
                            onNavigateToStories()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = KenyanGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View Success Stories & Mentors")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    badgeText: String? = null,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF111827)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (badgeText != null) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFE8F5E9)
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = KenyanGreen,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
