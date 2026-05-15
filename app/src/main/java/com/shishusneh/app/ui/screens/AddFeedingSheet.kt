package com.shishusneh.app.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shishusneh.app.data.model.*
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.ShishuUiState
import com.shishusneh.app.viewmodel.ShishuViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddFeedingSheet(
    state: ShishuUiState,
    viewModel: ShishuViewModel,
    onClose: () -> Unit
) {
    var tab by remember { mutableIntStateOf(0) }

    // ── Breastfeed timer state ─────────────────────────────────────────────
    var leftMin  by remember { mutableIntStateOf(0) }
    var leftSec  by remember { mutableIntStateOf(0) }
    var rightMin by remember { mutableIntStateOf(0) }
    var rightSec by remember { mutableIntStateOf(0) }
    var leftRunning  by remember { mutableStateOf(false) }
    var rightRunning by remember { mutableStateOf(false) }

    // Manual entry fields (shown alongside timer)
    var leftMinInput  by remember { mutableStateOf("") }
    var leftSecInput  by remember { mutableStateOf("") }
    var rightMinInput by remember { mutableStateOf("") }
    var rightSecInput by remember { mutableStateOf("") }

    var selectedNote by remember { mutableStateOf("") }

    LaunchedEffect(leftRunning) {
        while (leftRunning) {
            delay(1000L); leftSec++
            if (leftSec == 60) { leftSec = 0; leftMin++ }
            leftMinInput = if (leftMin > 0) leftMin.toString() else ""
            leftSecInput = if (leftSec > 0) leftSec.toString() else ""
        }
    }
    LaunchedEffect(rightRunning) {
        while (rightRunning) {
            delay(1000L); rightSec++
            if (rightSec == 60) { rightSec = 0; rightMin++ }
            rightMinInput = if (rightMin > 0) rightMin.toString() else ""
            rightSecInput = if (rightSec > 0) rightSec.toString() else ""
        }
    }

    // ── Bottle state ──────────────────────────────────────────────────────
    var bottleOz       by remember { mutableStateOf(2.0f) }
    var bottleDuration by remember { mutableIntStateOf(10) }
    var bottleContent  by remember { mutableStateOf("FORMULA") }

    Column(Modifier.fillMaxSize().background(AppBackground)) {
        // ── Header ────────────────────────────────────────────────────────
        Box(
            Modifier.fillMaxWidth()
                .background(Brush.linearGradient(listOf(PurpleDark, Color(0xFF9B6FE4))))
                .padding(top = 44.dp, bottom = 0.dp)
        ) {
            Column {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.size(32.dp).background(Color.White.copy(0.2f), CircleShape)
                            .clickable { onClose() },
                        contentAlignment = Alignment.Center
                    ) { Text("✕", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold) }

                    Text("Log Feeding 🤱", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)

                    Box(
                        Modifier.clip(RoundedCornerShape(10.dp))
                            .background(Color.White.copy(0.2f))
                            .clickable {
                                if (tab == 0) {
                                    val lm = leftMinInput.toIntOrNull() ?: leftMin
                                    val rm = rightMinInput.toIntOrNull() ?: rightMin
                                    val side = when {
                                        lm > 0 && rm > 0 -> BreastSide.BOTH
                                        lm > 0 -> BreastSide.LEFT
                                        else -> BreastSide.RIGHT
                                    }
                                    viewModel.addBreastfeedLog(side, lm, rm, selectedNote)
                                } else {
                                    val content = try { BottleContent.valueOf(bottleContent) } catch(e: Exception) { BottleContent.FORMULA }
                                    viewModel.addBottleLog(content, bottleOz, bottleDuration)
                                }
                                onClose()
                            }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) { Text("Save ✓", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold) }
                }
                Spacer(Modifier.height(14.dp))
                Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    listOf("🤱 Breastfeed", "🍼 Bottle").forEachIndexed { i, label ->
                        Box(
                            Modifier.weight(1f)
                                .background(
                                    if (i == tab) Color.White else Color.Transparent,
                                    RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                                )
                                .clickable { tab = i }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(label, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold,
                                color = if (i == tab) PurpleDark else Color.White.copy(0.8f))
                        }
                    }
                }
            }
        }

        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (tab == 0) {
                // ── BREASTFEED ──
                BreastTimerCard(
                    side = "LEFT", emoji = "⬅️",
                    timerMin = leftMin, timerSec = leftSec,
                    manualMin = leftMinInput, manualSec = leftSecInput,
                    running = leftRunning,
                    onToggle = { leftRunning = !leftRunning; if (leftRunning) rightRunning = false },
                    onReset  = { leftMin = 0; leftSec = 0; leftRunning = false; leftMinInput = ""; leftSecInput = "" },
                    onManualMinChange = { leftMinInput = it; leftMin = it.toIntOrNull() ?: 0 },
                    onManualSecChange = { leftSecInput = it; leftSec = it.toIntOrNull() ?: 0 }
                )
                BreastTimerCard(
                    side = "RIGHT", emoji = "➡️",
                    timerMin = rightMin, timerSec = rightSec,
                    manualMin = rightMinInput, manualSec = rightSecInput,
                    running = rightRunning,
                    onToggle = { rightRunning = !rightRunning; if (rightRunning) leftRunning = false },
                    onReset  = { rightMin = 0; rightSec = 0; rightRunning = false; rightMinInput = ""; rightSecInput = "" },
                    onManualMinChange = { rightMinInput = it; rightMin = it.toIntOrNull() ?: 0 },
                    onManualSecChange = { rightSecInput = it; rightSec = it.toIntOrNull() ?: 0 }
                )

                // Summary pill
                val totalMin = (leftMinInput.toIntOrNull() ?: leftMin) + (rightMinInput.toIntOrNull() ?: rightMin)
                val estL = ((leftMinInput.toIntOrNull() ?: leftMin) * 60 + (leftSecInput.toIntOrNull() ?: leftSec)) * 8.5f / 60
                val estR = ((rightMinInput.toIntOrNull() ?: rightMin) * 60 + (rightSecInput.toIntOrNull() ?: rightSec)) * 8.5f / 60
                if (totalMin > 0 || estL > 0 || estR > 0) {
                    Row(
                        Modifier.fillMaxWidth().background(PurpleBackground, RoundedCornerShape(14.dp)).padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        SummaryPill("Total", "$totalMin min")
                        SummaryPill("Est. Left", "${estL.toInt()} ml")
                        SummaryPill("Est. Right", "${estR.toInt()} ml")
                        SummaryPill("Combined", "${(estL + estR).toInt()} ml")
                    }
                }

                // Note tags
                val notes = listOf("Strong latch", "Cluster feed", "Sleepy feeder", "Fussy", "Pump")
                Column {
                    Text("Session Note (optional)", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        notes.forEach { note ->
                            val active = note == selectedNote
                            Box(
                                Modifier.background(if (active) PurplePrimary else Color.White, RoundedCornerShape(20.dp))
                                    .border(1.5.dp, if (active) Color.Transparent else Divider, RoundedCornerShape(20.dp))
                                    .clickable { selectedNote = if (active) "" else note }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) { Text(note, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = if (active) Color.White else TextSecondary) }
                        }
                    }
                }

                Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E8)), modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("💡", fontSize = 16.sp)
                        Text("Estimate based on ~8.5 ml/min average. Actual volume varies. Track weight gain weekly for accuracy.", fontSize = 9.sp, color = Color(0xFF885500), lineHeight = 13.sp)
                    }
                }

                // ── Recent history ──
                if (state.breastfeedLogs.isNotEmpty()) {
                    Text("Recent Sessions", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                    state.breastfeedLogs.take(5).forEach { e ->
                        HistoryRow(
                            emoji = "🤱",
                            title = "${e.side.name.lowercase().capitalize()} · ${e.leftMinutes + e.rightMinutes} min · ~${e.totalEstimatedMl} ml",
                            subtitle = "${e.dateLabel} ${e.timeOfDay}",
                            noteTag = e.noteTag.ifBlank { null },
                            onDelete = { viewModel.deleteBreastfeedLog(e) }
                        )
                    }
                }
            } else {
                // ── BOTTLE ──
                Text("What's in the bottle?", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("FORMULA" to "🥛 Formula", "BREAST_MILK" to "🤱 Breast Milk", "WATER" to "💧 Water").forEach { (key, label) ->
                        val active = key == bottleContent
                        Box(
                            Modifier.weight(1f)
                                .background(if (active) Brush.linearGradient(listOf(PurpleLight, PurplePrimary)) else Brush.linearGradient(listOf(Color.White, Color.White)), RoundedCornerShape(12.dp))
                                .border(1.5.dp, if (active) Color.Transparent else Divider, RoundedCornerShape(12.dp))
                                .clickable { bottleContent = key }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) { Text(label, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = if (active) Color.White else TextSecondary, textAlign = TextAlign.Center) }
                    }
                }
                // Oz slider + manual input
                Column {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Amount", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                        Text("%.1f oz  (${(bottleOz * 29.57f).toInt()} ml)".format(bottleOz), fontSize = 11.sp, fontWeight = FontWeight.Black, color = PurplePrimary)
                    }
                    Slider(value = bottleOz, onValueChange = { bottleOz = it }, valueRange = 0.5f..8f, steps = 14,
                        colors = SliderDefaults.colors(thumbColor = PurplePrimary, activeTrackColor = PurplePrimary, inactiveTrackColor = PurpleBackground))
                    // Manual oz text field
                    OutlinedTextField(
                        value = if (bottleOz == 0f) "" else "%.1f".format(bottleOz),
                        onValueChange = { bottleOz = it.toFloatOrNull()?.coerceIn(0.5f, 8f) ?: bottleOz },
                        label = { Text("Enter oz manually", fontSize = 10.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }
                // Duration stepper
                Column {
                    Text("Duration", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(8.dp))
                    Row(
                        Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(14.dp)).padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                    ) {
                        StepperButton("-") { if (bottleDuration > 1) bottleDuration-- }
                        Text("$bottleDuration min", fontSize = 20.sp, fontWeight = FontWeight.Black)
                        StepperButton("+") { bottleDuration++ }
                    }
                }
                // Age guide card
                BottleAgeGuideCard()

                // ── Recent bottle history ──
                if (state.bottleLogs.isNotEmpty()) {
                    Text("Recent Bottle Sessions", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                    state.bottleLogs.take(5).forEach { e ->
                        HistoryRow(
                            emoji = "🍼",
                            title = "%.1f oz  (${e.amountMl} ml)  ·  ${e.contentType.name.replace("_", " ").lowercase().capitalize()}  ·  ${e.durationMinutes} min".format(e.amountOz),
                            subtitle = "${e.dateLabel} ${e.timeOfDay}",
                            onDelete = { viewModel.deleteBottleLog(e) }
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Breast timer card with integrated manual entry
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun BreastTimerCard(
    side: String, emoji: String,
    timerMin: Int, timerSec: Int,
    manualMin: String, manualSec: String,
    running: Boolean,
    onToggle: () -> Unit, onReset: () -> Unit,
    onManualMinChange: (String) -> Unit,
    onManualSecChange: (String) -> Unit
) {
    val bgColor by animateColorAsState(if (running) Color(0xFFEEDDFF) else Color.White, label = "bg")
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = bgColor), elevation = CardDefaults.cardElevation(2.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Side badge
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(52.dp)) {
                    Text(emoji, fontSize = 22.sp)
                    Text(side, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = PurplePrimary)
                }
                // Timer display
                Column(Modifier.weight(1f)) {
                    Text("%02d:%02d".format(timerMin, timerSec), fontSize = 32.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                    val est = (timerMin * 60 + timerSec) * 8.5f / 60
                    Text("${est.toInt()} ml estimated", fontSize = 10.sp, color = TextSecondary)
                }
                // Controls
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        Modifier.size(44.dp).background(
                            if (running) Brush.linearGradient(listOf(Color(0xFFFF8A50), Color(0xFFFF5020)))
                            else Brush.linearGradient(listOf(PurpleLight, PurplePrimary)), CircleShape
                        ).clickable { onToggle() },
                        contentAlignment = Alignment.Center
                    ) { Text(if (running) "⏸" else "▶", fontSize = 18.sp) }
                    Box(Modifier.size(32.dp).background(Color(0xFFF0F0F4), CircleShape).clickable { onReset() }, contentAlignment = Alignment.Center) {
                        Text("↺", fontSize = 14.sp, color = TextSecondary)
                    }
                }
            }

            // ── Manual time entry row ──────────────────────────────────────
            Spacer(Modifier.height(10.dp))
            Text("Or enter time manually:", fontSize = 9.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = manualMin,
                    onValueChange = { if (it.length <= 3) onManualMinChange(it) },
                    label = { Text("Min", fontSize = 10.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary, unfocusedBorderColor = Divider)
                )
                Text(":", fontSize = 20.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                OutlinedTextField(
                    value = manualSec,
                    onValueChange = { v ->
                        val n = v.toIntOrNull()
                        if (v.isEmpty() || (n != null && n < 60)) onManualSecChange(v)
                    },
                    label = { Text("Sec", fontSize = 10.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurplePrimary, unfocusedBorderColor = Divider)
                )
                val est = ((manualMin.toIntOrNull() ?: 0) * 60 + (manualSec.toIntOrNull() ?: 0)) * 8.5f / 60
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("~${est.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Black, color = PurplePrimary)
                    Text("ml", fontSize = 8.sp, color = TextSecondary)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  History row with swipe-to-delete button
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun HistoryRow(emoji: String, title: String, subtitle: String, noteTag: String? = null, onDelete: () -> Unit) {
    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 18.sp)
            Column(Modifier.weight(1f)) {
                Text(title, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                Text(subtitle, fontSize = 8.sp, color = TextSecondary)
                if (!noteTag.isNullOrBlank()) {
                    Box(Modifier.background(PurpleBackground, RoundedCornerShape(6.dp)).padding(horizontal = 6.dp, vertical = 1.dp)) {
                        Text(noteTag, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = PurplePrimary)
                    }
                }
            }
            Box(
                Modifier.size(26.dp).background(Color(0xFFFFEEEE), RoundedCornerShape(8.dp)).clickable { onDelete() },
                contentAlignment = Alignment.Center
            ) { Text("🗑", fontSize = 12.sp) }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Reusable micro-composables
// ─────────────────────────────────────────────────────────────────────────────
@Composable fun SummaryPill(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Black, color = PurplePrimary)
        Text(label, fontSize = 8.sp, color = TextSecondary)
    }
}

@Composable fun StepperButton(label: String, onClick: () -> Unit) {
    Box(Modifier.size(40.dp).background(PurpleBackground, RoundedCornerShape(10.dp)).clickable { onClick() }, contentAlignment = Alignment.Center) {
        Text(label, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PurplePrimary)
    }
}

@Composable fun BottleAgeGuideCard() {
    Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F8F0)), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("📊", fontSize = 18.sp)
            Column {
                Text("Age-based bottle guide", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1A6040))
                Spacer(Modifier.height(4.dp))
                listOf("0–1 month" to "1.5–3 oz / feed  (8–12×/day)", "1–2 months" to "2–4 oz / feed  (7–8×/day)", "2–4 months" to "4–6 oz / feed  (6–7×/day)", "4–6 months" to "4–8 oz / feed  (5–6×/day)").forEach { (age, amt) ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(age, fontSize = 9.sp, color = Color(0xFF1A6040), fontWeight = FontWeight.Bold)
                        Text(amt, fontSize = 9.sp, color = Color(0xFF2A7050))
                    }
                }
            }
        }
    }
}
