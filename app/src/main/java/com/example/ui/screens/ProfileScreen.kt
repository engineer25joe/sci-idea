package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.BiasharaUiState
import com.example.viewmodel.BiasharaViewModel
import com.example.network.AccountProfileDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(
    uiState: BiasharaUiState,
    viewModel: BiasharaViewModel,
    accountProfile: AccountProfileDto,
    onLogout: () -> Unit,
    onNavigateToSaved: () -> Unit,
    onNavigateToStories: () -> Unit
) {
    var showActivityDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCream)
    ) {

        // ---------------------------------------------------------
        // PROFILE HEADER
        // ---------------------------------------------------------

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SciBluePrimary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 22.dp,
                        vertical = 28.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    border = BorderStroke(
                        4.dp,
                        SciRedAccent
                    )
                ) {
                    Box(
                        modifier = Modifier.size(88.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User Avatar",
                            tint = SciBluePrimary,
                            modifier = Modifier.size(52.dp)
                        )
                    }
                }

                Spacer(Modifier.height(13.dp))

                Text(
                    text = uiState.userProfileName.ifBlank {
                        "SCI IDEA User"
                    },
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = uiState.userProfileEmail,
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(30.dp),
                    color = Color.White.copy(alpha = 0.13f)
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 13.dp,
                            vertical = 7.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = Color(0xFF75E2A4),
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(Modifier.width(6.dp))

                        Text(
                            text = "Registered SCI IDEA Account",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }

            // Decorative red accent
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(SciRedAccent)
            )
        }

        // ---------------------------------------------------------
        // PROFILE CONTENT
        // ---------------------------------------------------------

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 18.dp,
                    vertical = 18.dp
                )
                .padding(bottom = 100.dp)
        ) {

            // Account section
            ProfileSectionTitle(
                title = "ACCOUNT INFORMATION",
                subtitle = "Your registered account details"
            )

            Spacer(Modifier.height(10.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_cloud_account_card"),
                shape = RoundedCornerShape(19.dp),
                color = SurfaceCard,
                border = BorderStroke(1.dp, SurfaceBorder)
            ) {
                Column(
                    modifier = Modifier.padding(17.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SciRedContainer
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = SciRedAccent,
                                modifier = Modifier.padding(11.dp)
                            )
                        }

                        Spacer(Modifier.width(11.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "SCI IDEA Account",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Text(
                                text = "Account profile",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Account registered",
                            tint = KenyaGreenPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 14.dp),
                        color = SurfaceBorder
                    )

                    ProfileInformationRow(
                        label = "Full name",
                        value = listOf(
                            accountProfile.firstName,
                            accountProfile.lastName
                        )
                            .filter { it.isNotBlank() }
                            .joinToString(" ")
                            .ifBlank { "SCI IDEA User" },
                        icon = Icons.Default.Person
                    )

                    Spacer(Modifier.height(13.dp))

                    ProfileInformationRow(
                        label = "Email address",
                        value = accountProfile.email,
                        icon = Icons.Default.Email
                    )

                    accountProfile.phoneNumber?.let { phone ->
                        Spacer(Modifier.height(13.dp))

                        ProfileInformationRow(
                            label = "Phone number",
                            value = phone,
                            icon = Icons.Default.Phone
                        )
                    }
                }
            }

            Spacer(Modifier.height(23.dp))

            // -----------------------------------------------------
            // USER JOURNEY
            // -----------------------------------------------------

            ProfileSectionTitle(
                title = "YOUR PROGRESS",
                subtitle = "Your activity on BiasharaHub"
            )

            Spacer(Modifier.height(10.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_journey_card"),
                shape = RoundedCornerShape(19.dp),
                color = SurfaceCard,
                border = BorderStroke(1.dp, SurfaceBorder)
            ) {
                Column(
                    modifier = Modifier.padding(17.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SciBlueContainer
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = SciBluePrimary,
                                modifier = Modifier.padding(11.dp)
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Ideas Explored",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Text(
                                text = "Your discovery activity",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        Text(
                            text = uiState.ideasExploredCount.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = SciRedAccent
                        )
                    }

                    Spacer(Modifier.height(13.dp))

                    Text(
                        text = "Continue exploring business opportunities, learning resources and new possibilities.",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(Modifier.height(23.dp))

            // -----------------------------------------------------
            // ACCOUNT MENU
            // -----------------------------------------------------

            ProfileSectionTitle(
                title = "MY ACCOUNT",
                subtitle = "Manage your personal workspace"
            )

            Spacer(Modifier.height(10.dp))

            ProfileMenuItem(
                icon = Icons.Outlined.BookmarkBorder,
                title = "My Saved Ideas",
                testTag = "profile_menu_saved",
                onClick = onNavigateToSaved
            )

            Spacer(Modifier.height(10.dp))

            ProfileMenuItem(
                icon = Icons.Outlined.Analytics,
                title = "My Activity & Tracking",
                badgeText = "${uiState.userActivities.size} events",
                testTag = "profile_menu_activity",
                onClick = { showActivityDialog = true }
            )

            Spacer(Modifier.height(10.dp))

            ProfileMenuItem(
                icon = Icons.Outlined.HelpOutline,
                title = "Help & Support",
                testTag = "profile_menu_help",
                onClick = { showHelpDialog = true }
            )

            Spacer(Modifier.height(23.dp))

            // -----------------------------------------------------
            // LOGOUT
            // -----------------------------------------------------

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("profile_logout_button"),
                shape = RoundedCornerShape(15.dp),
                border = BorderStroke(
                    1.dp,
                    Color(0xFFFFCCCC)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = SciRedAccent
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(19.dp)
                )

                Spacer(Modifier.width(9.dp))

                Text(
                    text = "Log Out",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(25.dp))

            // -----------------------------------------------------
            // BRAND FOOTER
            // -----------------------------------------------------

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(SciRedAccent)
                    )

                    Spacer(Modifier.width(7.dp))

                    Text(
                        text = "SCI BASE COLLECTIONS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        color = SciBluePrimary
                    )
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Developed by Engineer Joe",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "© 2026 SCI BASE COLLECTIONS. All rights reserved.",
                    fontSize = 10.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    // =============================================================
    // ACTIVITY & TRACKING DIALOG
    // =============================================================

    if (showActivityDialog) {
        AlertDialog(
            onDismissRequest = {
                showActivityDialog = false
            },
            shape = RoundedCornerShape(22.dp),
            containerColor = SurfaceCard,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = null,
                        tint = SciRedAccent
                    )

                    Spacer(Modifier.width(9.dp))

                    Text(
                        text = "Activity Tracking",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimary
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Your recent activity events are recorded on this device and can be synchronized with the Django backend.",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = TextSecondary
                    )

                    Spacer(Modifier.height(12.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(11.dp),
                        color = SciBlueContainer
                    ) {
                        Column(
                            modifier = Modifier.padding(11.dp)
                        ) {
                            Text(
                                text = "DJANGO BACKEND",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = SciBluePrimary
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = uiState.djangoConnectionStatus,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SciBluePrimary
                            )
                        }
                    }

                    Spacer(Modifier.height(13.dp))

                    Text(
                        text = "Recent Activity (${uiState.userActivities.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(Modifier.height(9.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(230.dp)
                    ) {
                        if (uiState.userActivities.isEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(15.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(35.dp)
                                )

                                Spacer(Modifier.height(9.dp))

                                Text(
                                    text = "No activity recorded yet.",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(7.dp)
                            ) {
                                items(
                                    items = uiState.userActivities.take(25)
                                ) { event ->

                                    val timeStr = SimpleDateFormat(
                                        "HH:mm:ss",
                                        Locale.getDefault()
                                    ).format(Date(event.timestampMs))

                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(11.dp),
                                        color = SurfaceCream
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Surface(
                                                shape = RoundedCornerShape(9.dp),
                                                color = SciBlueContainer
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.History,
                                                    contentDescription = null,
                                                    tint = SciBluePrimary,
                                                    modifier = Modifier.padding(8.dp)
                                                )
                                            }

                                            Spacer(Modifier.width(9.dp))

                                            Column(
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    text = event.targetTitle
                                                        ?: event.eventType,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextPrimary
                                                )

                                                Text(
                                                    text = "${event.eventType} • $timeStr",
                                                    fontSize = 9.sp,
                                                    color = TextSecondary
                                                )
                                            }

                                            val synced = event.isSyncedToDjango

                                            Surface(
                                                shape = RoundedCornerShape(7.dp),
                                                color = if (synced) {
                                                    KenyaGreenContainer
                                                } else {
                                                    KenyaGoldLight
                                                }
                                            ) {
                                                Text(
                                                    text = if (synced) {
                                                        "Synced"
                                                    } else {
                                                        "Queued"
                                                    },
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (synced) {
                                                        KenyaGreenDark
                                                    } else {
                                                        KenyaGoldDark
                                                    },
                                                    modifier = Modifier.padding(
                                                        horizontal = 7.dp,
                                                        vertical = 5.dp
                                                    )
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
                    shape = RoundedCornerShape(11.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SciRedAccent
                    ),
                    modifier = Modifier.testTag("sync_to_django_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(Modifier.width(7.dp))

                    Text("Sync Now")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showActivityDialog = false
                    }
                ) {
                    Text(
                        text = "Close",
                        color = TextSecondary
                    )
                }
            }
        )
    }

    // =============================================================
    // HELP & SUPPORT DIALOG
    // =============================================================

    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = {
                showHelpDialog = false
            },
            shape = RoundedCornerShape(22.dp),
            containerColor = SurfaceCard,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SupportAgent,
                        contentDescription = null,
                        tint = SciRedAccent
                    )

                    Spacer(Modifier.width(9.dp))

                    Text(
                        text = "Help & Support",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimary
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "BiasharaHub KE provides business discovery, capital breakdowns and guidance for aspiring entrepreneurs.",
                        fontSize = 12.sp,
                        lineHeight = 19.sp,
                        color = TextSecondary
                    )

                    Spacer(Modifier.height(15.dp))

                    Text(
                        text = "OFFICIAL SUPPORT CONTACTS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = SciBluePrimary
                    )

                    Spacer(Modifier.height(9.dp))

                    ProfileContactRow(
                        icon = Icons.Default.Business,
                        label = "SCI BASE COLLECTIONS",
                        value = "+254723 534 054"
                    )

                    Spacer(Modifier.height(10.dp))

                    ProfileContactRow(
                        icon = Icons.Default.Person,
                        label = "ENGINEER JOE",
                        value = "+254723 534 054"
                    )

                    Spacer(Modifier.height(10.dp))

                    ProfileContactRow(
                        icon = Icons.Default.Chat,
                        label = "Social / WhatsApp",
                        value = "@engineer25joe • @SciBaseCollections"
                    )

                    Spacer(Modifier.height(17.dp))

                    Button(
                        onClick = {
                            showHelpDialog = false
                            onNavigateToStories()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SciBluePrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoStories,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(Modifier.width(8.dp))

                        Text("Explore Success Stories")
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showHelpDialog = false
                    }
                ) {
                    Text(
                        text = "Close",
                        color = SciRedAccent
                    )
                }
            }
        )
    }
}

// =============================================================
// PROFILE SECTION HEADER
// =============================================================

@Composable
private fun ProfileSectionTitle(
    title: String,
    subtitle: String
) {
    Column {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp,
            color = SciBluePrimary
        )

        Spacer(Modifier.height(3.dp))

        Text(
            text = subtitle,
            fontSize = 11.sp,
            color = TextSecondary
        )
    }
}

// =============================================================
// ACCOUNT INFORMATION ROW
// =============================================================

@Composable
private fun ProfileInformationRow(
    label: String,
    value: String,
    icon: ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(18.dp)
        )

        Spacer(Modifier.width(10.dp))

        Column {
            Text(
                text = label,
                fontSize = 10.sp,
                color = TextSecondary
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }
    }
}

// =============================================================
// SUPPORT CONTACT ROW
// =============================================================

@Composable
private fun ProfileContactRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(9.dp),
            color = SciRedContainer
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SciRedAccent,
                modifier = Modifier.padding(9.dp)
            )
        }

        Spacer(Modifier.width(10.dp))

        Column {
            Text(
                text = label,
                fontSize = 10.sp,
                color = TextSecondary
            )

            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }
    }
}

// =============================================================
// PROFILE MENU ITEM
// =============================================================

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
            .clickable(onClick = onClick)
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, SurfaceBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 14.dp,
                    vertical = 13.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(11.dp),
                color = SciBlueContainer
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = SciBluePrimary,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(Modifier.width(13.dp))

            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )

            if (badgeText != null) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SciRedContainer
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SciRedAccent,
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 5.dp
                        )
                    )
                }

                Spacer(Modifier.width(6.dp))
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(21.dp)
            )
        }
    }
}