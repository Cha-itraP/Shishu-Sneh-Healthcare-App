package com.shishusneh.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shishusneh.app.data.model.GrowthMetric
import com.shishusneh.app.ui.components.*
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.ShishuUiState
import com.shishusneh.app.viewmodel.ShishuViewModel

@Composable
fun LogScreen(state: ShishuUiState, viewModel: ShishuViewModel) {
    var tab by remember { mutableIntStateOf(0) }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            // ── Header ────────────────────────────────────────────────────
            Box(
                Modifier.fillMaxWidth()
                    .background(Brush.linearGradient(listOf(PurpleLighter, Color(0xFFEAB8FF))))
                    .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 0.dp)
            ) {
                Column {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("📋 Log Center", fontSize = 10.sp, color = Color(0xFF555555), fontWeight = FontWeight.SemiBold)
                            Text("Track & Monitor", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                        }
                        // ── TOP-RIGHT + button → FEEDING only ─────────────
                        Box(
                            Modifier.size(38.dp)
                                .background(Brush.linearGradient(listOf(PurpleLight, PurplePrimary)), CircleShape)
                                .clickable { viewModel.openAddFeedingSheet() },
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(20.dp)) }
                    }
                    Spacer(Modifier.height(12.dp))
                    // Tab strip
                    Row(Modifier.fillMaxWidth()) {
                        listOf("🤱 Feeding", "📈 Growth", "🥗 Nutrition").forEachIndexed { i, label ->
                            Box(
                                Modifier.weight(1f)
                                    .background(if (tab == i) Color.White else Color.Transparent, RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                                    .clickable { tab = i }
                                    .padding(vertical = 9.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(label, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold,
                                    color = if (tab == i) PurplePrimary else Color(0xFF555555))
                            }
                        }
                    }
                }
            }

            when (tab) {
                0 -> FeedingTab(state, viewModel)
                1 -> GrowthTab(state, viewModel)
                2 -> NutritionTipsScreen(state, viewModel, onBack = { tab = 0 })
            }
        }

        // ── Growth detail slide ───────────────────────────────────────────
        AnimatedVisibility(
            visible = state.showGrowthDetail,
            enter = slideInHorizontally(initialOffsetX = { it }),
            exit  = slideOutHorizontally(targetOffsetX = { it })
        ) { GrowthDetailPanel(state, viewModel) { viewModel.closeGrowthDetail() } }

        // ── Add Feeding sheet (slides up) ─────────────────────────────────
        AnimatedVisibility(
            visible = state.showAddFeedingSheet,
            enter = slideInVertically(initialOffsetY = { it }),
            exit  = slideOutVertically(targetOffsetY = { it })
        ) {
            AddFeedingSheet(state = state, viewModel = viewModel) { viewModel.closeAddFeedingSheet() }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Feeding Tab
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun FeedingTab(state: ShishuUiState, viewModel: ShishuViewModel) {
    val fmt = remember { java.text.SimpleDateFormat("h:mm a", java.util.Locale.getDefault()) }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Log button at top
        Box(
            Modifier.fillMaxWidth()
                .background(Brush.linearGradient(listOf(PurpleLight, PurplePrimary)), RoundedCornerShape(14.dp))
                .clickable { viewModel.openAddFeedingSheet() }
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Text("Log new feeding", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            }
        }

        // ── Breastfeed history ────────────────────────────────────────────
        if (state.breastfeedLogs.isNotEmpty()) {
            Text("Breastfeed sessions", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
            state.breastfeedLogs.take(10).forEach { e ->
                AppCard {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(38.dp).background(PurpleBackground, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                                Text("🤱", fontSize = 18.sp)
                            }
                            Column {
                                val side = e.side.name.lowercase().replaceFirstChar { it.uppercase() }
                                val detail = buildString {
                                    if (e.leftMinutes > 0) append("L ${e.leftMinutes}m")
                                    if (e.rightMinutes > 0) {
                                        if (isNotEmpty()) append(" + ")
                                        append("R ${e.rightMinutes}m")
                                    }
                                }
                                Text("$side · $detail · ~${e.totalEstimatedMl} ml", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                                Text("${e.dateLabel} · ${e.timeOfDay}", fontSize = 9.sp, color = TextSecondary)
                                if (e.noteTag.isNotBlank()) {
                                    Box(Modifier.background(PurpleBackground, RoundedCornerShape(6.dp)).padding(horizontal = 6.dp, vertical = 1.dp)) {
                                        Text(e.noteTag, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = PurplePrimary)
                                    }
                                }
                            }
                        }
                        // Side pill
                        val (bg, fg, txt) = when (e.side) {
                            com.shishusneh.app.data.model.BreastSide.BOTH -> Triple(PurpleBackground, PurplePrimary, "L+R")
                            com.shishusneh.app.data.model.BreastSide.LEFT -> Triple(Color(0xFFE8EEFF), Color(0xFF2040CC), "L")
                            com.shishusneh.app.data.model.BreastSide.RIGHT -> Triple(Color(0xFFFFE8F8), Color(0xFFCC2090), "R")
                        }
                        Box(Modifier.background(bg, RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                            Text(txt, fontSize = 10.sp, fontWeight = FontWeight.Black, color = fg)
                        }
                    }
                }
            }
        } else {
            EmptyHistoryCard("🤱", "No breastfeed sessions yet", "Tap 'Log new feeding' to add your first session")
        }

        // ── Bottle history ────────────────────────────────────────────────
        if (state.bottleLogs.isNotEmpty()) {
            Text("Bottle sessions", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
            state.bottleLogs.take(10).forEach { e ->
                AppCard {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(38.dp).background(Color(0xFFFFF0E8), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                                Text("🍼", fontSize = 18.sp)
                            }
                            Column {
                                Text("%.1f oz  (${e.amountMl} ml)  ·  ${e.contentType.name.replace("_"," ").lowercase().replaceFirstChar{it.uppercase()}}".format(e.amountOz),
                                    fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                                Text("${e.durationMinutes} min  ·  ${e.dateLabel} ${e.timeOfDay}", fontSize = 9.sp, color = TextSecondary)
                            }
                        }
                        Box(Modifier.background(Color(0xFFFFF0E8), RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 3.dp)) {
                            Text("%.1foz".format(e.amountOz), fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFFCC6020))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Growth Tab — summary cards + open detail
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun GrowthTab(state: ShishuUiState, viewModel: ShishuViewModel) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LogBigButton(
            emoji = "📈", title = "Growth Chart",
            subtitle = "Weight, height & head with percentile trend",
            gradientColors = listOf(Color(0xFFC8E8F5), Color(0xFFA8D0E8))
        ) { viewModel.openGrowthDetail() }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                GrowthMetric.WEIGHT to Triple("⚖️", state.growthDataMap[GrowthMetric.WEIGHT]?.currentValue ?: "—", state.growthDataMap[GrowthMetric.WEIGHT]?.percentile ?: ""),
                GrowthMetric.HEIGHT to Triple("📏", state.growthDataMap[GrowthMetric.HEIGHT]?.currentValue ?: "—", state.growthDataMap[GrowthMetric.HEIGHT]?.percentile ?: ""),
                GrowthMetric.HEAD   to Triple("🔵", state.growthDataMap[GrowthMetric.HEAD]?.currentValue ?: "—", state.growthDataMap[GrowthMetric.HEAD]?.percentile ?: "")
            ).forEach { (metric, info) ->
                val (emoji, value, pct) = info
                Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = CardBackground), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(
                        Modifier.padding(10.dp).clickable { viewModel.selectGrowthMetric(metric); viewModel.openGrowthDetail() },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(emoji, fontSize = 18.sp)
                        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Black)
                        Text(metric.name.lowercase().replaceFirstChar { it.uppercase() }, fontSize = 8.sp, color = TextSecondary)
                        if (pct.isNotEmpty()) {
                            Box(Modifier.background(PurpleBackground, RoundedCornerShape(6.dp)).padding(horizontal = 5.dp, vertical = 2.dp)) {
                                Text(pct, fontSize = 8.sp, fontWeight = FontWeight.ExtraBold, color = PurplePrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Helpers
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun EmptyHistoryCard(emoji: String, title: String, subtitle: String) {
    Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(emoji, fontSize = 28.sp)
            Spacer(Modifier.height(6.dp))
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
            Text(subtitle, fontSize = 10.sp, color = TextSecondary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@Composable
fun LogBigButton(emoji: String, title: String, subtitle: String, gradientColors: List<Color>, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Brush.linearGradient(gradientColors), RoundedCornerShape(18.dp)).clickable { onClick() }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(Modifier.size(48.dp).background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center) { Text(emoji, fontSize = 26.sp) }
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Black, color = TextPrimary)
            Text(subtitle, fontSize = 10.sp, color = Color(0xFF555555), lineHeight = 14.sp)
        }
        Text("›", fontSize = 22.sp, color = Color(0xFF666666), fontWeight = FontWeight.Bold)
    }
}
