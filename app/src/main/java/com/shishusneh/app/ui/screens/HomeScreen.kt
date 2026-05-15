package com.shishusneh.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shishusneh.app.ui.components.*
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.Screen
import com.shishusneh.app.viewmodel.ShishuUiState
import com.shishusneh.app.viewmodel.ShishuViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeScreen(
    state: ShishuUiState,
    viewModel: ShishuViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        HomeHeader(state, onBellClick = { viewModel.toggleNotifications() }, onAvatarClick = { viewModel.toggleProfile() })
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalendarCard(state, viewModel)
            // Today's Tip
            AppCard(tint = true) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        Modifier.size(22.dp).background(PurpleLighter, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) { Text("💡", fontSize = 12.sp) }
                    Text("Today's Tip", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                }
                Spacer(Modifier.height(6.dp))
                Text(state.todayTip, fontSize = 11.sp, color = TextSecondary, lineHeight = 16.sp)
            }
            // Next vaccination
            AppCard {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        Modifier.size(22.dp).background(PurpleLighter, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) { Text("💉", fontSize = 12.sp) }
                    Text("Next Vaccination", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                }
                Spacer(Modifier.height(4.dp))
                Text("BCG — Overdue: 01 Apr 2026", fontSize = 11.sp, color = ErrorRed, fontWeight = FontWeight.Bold)
                Text("Prevents: Tuberculosis · Visit nearest clinic", fontSize = 10.sp, color = TextSecondary)
            }
            // Milestone progress
            Text("Milestones this week ⭐", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
            AppCard(modifier = Modifier.clickable { viewModel.navigate(Screen.MILESTONES) }) {
                Text("4 of 6 completed · Week 5", fontSize = 11.sp, color = TextSecondary)
                Spacer(Modifier.height(6.dp))
                GradientProgressBar(0.67f)
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Motor 2/3", "Social 1/2", "Language 1/1").forEach {
                        Text(it, fontSize = 9.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text("View All →", fontSize = 9.sp, color = PurplePrimary, fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.align(Alignment.End))
            }
        }
    }
}

@Composable
fun HomeHeader(
    state: ShishuUiState,
    onBellClick: () -> Unit,
    onAvatarClick: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(listOf(PurpleLighter, Color(0xFFEAB8FF))))
            .padding(start = 18.dp, end = 18.dp, top = 32.dp, bottom = 14.dp)
    ) {
        Column {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text("Good morning 🌤", fontSize = 11.sp, color = Color(0xFF555555), fontWeight = FontWeight.SemiBold)
                    Text("Baby ${state.baby.name}", fontSize = 22.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    // Bell
                    Box(
                        Modifier
                            .size(32.dp)
                            .background(Color.White.copy(alpha = 0.6f), CircleShape)
                            .clickable { onBellClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🔔", fontSize = 14.sp)
                        Box(
                            Modifier
                                .size(8.dp)
                                .background(Color(0xFFFF4444), CircleShape)
                                .align(Alignment.TopEnd)
                        )
                    }
                    // Avatar
                    Box(
                        Modifier
                            .size(32.dp)
                            .background(Color.White.copy(alpha = 0.6f), CircleShape)
                            .clickable { onAvatarClick() },
                        contentAlignment = Alignment.Center
                    ) { Text("👤", fontSize = 14.sp) }
                }
            }
            Spacer(Modifier.height(8.dp))
            AgeBadge("🌱 ${state.baby.ageWeeks} weeks old  •  Week ${state.baby.ageWeeks}")
        }
    }
}

@Composable
fun CalendarCard(state: ShishuUiState, viewModel: ShishuViewModel) {
    val date = state.calendarDate
    val today = LocalDate.now()
    val daysInMonth = date.lengthOfMonth()
    val firstDayOfWeek = date.withDayOfMonth(1).dayOfWeek.value % 7 // Sun=0

    AppCard {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("📅", fontSize = 12.sp)
                Text(
                    "${date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${date.year}",
                    fontSize = 12.sp, fontWeight = FontWeight.ExtraBold
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf("‹" to { viewModel.prevMonth() }, "›" to { viewModel.nextMonth() }).forEach { (label, action) ->
                    Box(
                        Modifier
                            .size(24.dp)
                            .background(PurpleBackground, RoundedCornerShape(8.dp))
                            .clickable { action() },
                        contentAlignment = Alignment.Center
                    ) { Text(label, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = PurplePrimary) }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        // Day labels
        val dayLabels = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
        Row(Modifier.fillMaxWidth()) {
            dayLabels.forEach {
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(it, fontSize = 8.sp, fontWeight = FontWeight.ExtraBold, color = TextSecondary)
                }
            }
        }
        Spacer(Modifier.height(4.dp))
        // Grid
        val totalCells = firstDayOfWeek + daysInMonth
        val rows = (totalCells + 6) / 7
        for (row in 0 until rows) {
            Row(Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val cellIndex = row * 7 + col
                    val day = cellIndex - firstDayOfWeek + 1
                    Box(Modifier.weight(1f).padding(1.dp), contentAlignment = Alignment.Center) {
                        when {
                            day < 1 || day > daysInMonth -> Spacer(Modifier.size(26.dp))
                            day == today.dayOfMonth && date.month == today.month && date.year == today.year -> {
                                Box(
                                    Modifier
                                        .size(26.dp)
                                        .background(
                                            Brush.linearGradient(listOf(Color(0xFFE870FF), PurplePrimary)),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) { Text("$day", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White) }
                            }
                            state.eventDays.contains(day) -> {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("$day", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Box(Modifier.size(4.dp).background(PurpleLight, CircleShape))
                                }
                            }
                            else -> Text("$day", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                    }
                }
            }
        }
    }
}
