package com.shishusneh.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shishusneh.app.ui.components.AgeBadge
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.ShishuUiState
import com.shishusneh.app.viewmodel.ShishuViewModel

@Composable
fun ProfilePanel(
    state: ShishuUiState,
    viewModel: ShishuViewModel,
    motherName: String = "Mother",
    babyName: String = state.baby.name,
    babyGender: String = "Girl",
    onLogout: (() -> Unit)? = null
) {
    AnimatedVisibility(
        visible = state.showProfile,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit  = slideOutHorizontally(targetOffsetX = { it })
    ) {
        Column(Modifier.fillMaxSize().background(AppBackground)) {
            // ── Header ────────────────────────────────────────────────────
            Box(
                Modifier.fillMaxWidth()
                    .background(Brush.linearGradient(listOf(PurpleDark, Color(0xFF9B6FE4))))
                    .padding(top = 40.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        IconButton(onClick = { viewModel.toggleProfile() },
                            modifier = Modifier.size(30.dp).background(Color.White.copy(0.2f), CircleShape)) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = {},
                            modifier = Modifier.size(30.dp).background(Color.White.copy(0.2f), CircleShape)) {
                            Icon(Icons.Default.Settings, null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // Baby avatar
                    Box(
                        Modifier.size(64.dp)
                            .background(Brush.linearGradient(listOf(PurpleLight, PurplePrimary)), CircleShape)
                            .border(3.dp, Color.White.copy(0.8f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(if (babyGender == "Boy") "👦" else "👶", fontSize = 28.sp)
                    }

                    Spacer(Modifier.height(8.dp))
                    Text(babyName, fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color.White)
                    Text("${state.baby.ageWeeks} weeks · ${state.baby.ageDays} days old", fontSize = 10.sp, color = Color.White.copy(0.8f))

                    Spacer(Modifier.height(4.dp))
                    // Mother name badge
                    Box(
                        Modifier.background(Color.White.copy(0.2f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                        Text("👩 $motherName", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(Modifier.height(6.dp))
                    AgeBadge("🌱 Week ${state.baby.ageWeeks} of newborn stage")
                }
            }

            Column(
                Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Upgrade banner
                Row(
                    Modifier.fillMaxWidth()
                        .background(Brush.linearGradient(listOf(Color(0xFFFFF0D0), Color(0xFFFFE0A0))), RoundedCornerShape(14.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Upgrade To Pro", fontSize = 12.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                        Text("Unlock all milestones, expert tips, and growth analytics", fontSize = 9.sp, color = Color(0xFF666666))
                        Spacer(Modifier.height(5.dp))
                        Box(
                            Modifier.background(Brush.linearGradient(listOf(PurpleLight, PurplePrimary)), RoundedCornerShape(9.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) { Text("Upgrade Plan", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Color.White) }
                    }
                    Text("🎁", fontSize = 28.sp)
                }

                // 2-column grid
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("👤" to "My Profile" to "Manage your profile", "👶" to "My Babies" to "Add, edit baby profiles").forEach { (pair, sub) ->
                        val (emoji, title) = pair
                        Box(
                            Modifier.weight(1f).background(Color.White, RoundedCornerShape(14.dp)).padding(10.dp)
                        ) {
                            Column {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(emoji, fontSize = 18.sp)
                                    Text("↗", fontSize = 10.sp, color = TextSecondary)
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(title, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                                Text(sub, fontSize = 9.sp, color = TextSecondary, lineHeight = 13.sp)
                            }
                        }
                    }
                }

                // Settings list
                listOf(
                    Triple("🪪", "Baby Identification Cards", "Baby's ID cards"),
                    Triple("🚨", "Emergency Information", "Key contacts for emergency help"),
                    Triple("📅", "Appointments", "Manage ${babyName}'s appointments"),
                    Triple("🎉", "Events", "Manage ${babyName}'s events")
                ).forEach { (emoji, title, sub) ->
                    Row(
                        Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp)).clickable {}.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(Modifier.size(30.dp).background(PurpleBackground, RoundedCornerShape(9.dp)), contentAlignment = Alignment.Center) {
                            Text(emoji, fontSize = 14.sp)
                        }
                        Column(Modifier.weight(1f)) {
                            Text(title, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                            Text(sub, fontSize = 9.sp, color = TextSecondary)
                        }
                        Text("»", fontSize = 16.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    }
                }

                // ── Logout button ─────────────────────────────────────────
                if (onLogout != null) {
                    Spacer(Modifier.height(4.dp))
                    Box(
                        Modifier.fillMaxWidth()
                            .background(Color(0xFFFFEEEE), RoundedCornerShape(14.dp))
                            .clickable { onLogout() }
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("🚪", fontSize = 16.sp)
                            Text("Sign Out", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFCC2020))
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
