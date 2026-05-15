package com.shishusneh.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.shishusneh.app.data.model.GrowthEntry
import com.shishusneh.app.data.model.GrowthMetric
import com.shishusneh.app.ui.components.AppCard
import com.shishusneh.app.ui.components.GrowthChart
import com.shishusneh.app.ui.components.SegmentControl
import com.shishusneh.app.ui.components.SlideHeader
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.ShishuUiState
import com.shishusneh.app.viewmodel.ShishuViewModel

// ─────────────────────────────────────────────────────────────────────────────
//  Add Growth Measurement Dialog
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun AddGrowthDialog(
    metric: GrowthMetric,
    onSave: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    var input by remember { mutableStateOf("") }
    val (unit, label, placeholder, hint) = when (metric) {
        GrowthMetric.WEIGHT -> listOf("g",  "Weight",         "e.g. 5600", "Enter weight in grams")
        GrowthMetric.HEIGHT -> listOf("cm", "Height",         "e.g. 60.5", "Enter height in cm")
        GrowthMetric.HEAD   -> listOf("cm", "Head Circumference", "e.g. 39", "Enter head circ. in cm")
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(8.dp)) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Header
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Add $label 📏", fontSize = 15.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                    Box(Modifier.size(26.dp).background(Color(0xFFF0F0F4), CircleShape).clickable { onDismiss() }, contentAlignment = Alignment.Center) {
                        Text("✕", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    }
                }

                Text(hint, fontSize = 10.sp, color = TextSecondary)

                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    label = { Text("$label ($unit)", fontSize = 11.sp) },
                    placeholder = { Text(placeholder, fontSize = 11.sp, color = TextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PurplePrimary,
                        unfocusedBorderColor = Divider
                    ),
                    trailingIcon = {
                        Text(unit, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PurplePrimary,
                            modifier = Modifier.padding(end = 12.dp))
                    }
                )

                // Preview estimated value
                val preview = input.toFloatOrNull()
                if (preview != null) {
                    Box(Modifier.fillMaxWidth().background(PurpleBackground, RoundedCornerShape(10.dp)).padding(10.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("✓", fontSize = 14.sp, color = PurplePrimary, fontWeight = FontWeight.Black)
                            Text("Will save: $preview $unit", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = PurplePrimary)
                        }
                    }
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), shape = RoundedCornerShape(10.dp)) {
                        Text("Cancel", fontSize = 12.sp)
                    }
                    Button(
                        onClick = {
                            val v = input.toFloatOrNull()
                            if (v != null && v > 0) onSave(v)
                        },
                        enabled = input.toFloatOrNull() != null && input.toFloatOrNull()!! > 0,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
                    ) {
                        Text("Save", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Growth Detail Panel — uses Room-backed records
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun GrowthDetailPanel(
    state: ShishuUiState,
    viewModel: ShishuViewModel,
    onBack: () -> Unit
) {
    val metrics = listOf(GrowthMetric.WEIGHT, GrowthMetric.HEIGHT, GrowthMetric.HEAD)
    val labels  = listOf("Weight", "Height", "Head")
    val selectedIndex = metrics.indexOf(state.selectedGrowthMetric).coerceAtLeast(0)

    // Get Room records for selected metric
    val dbRecords: List<GrowthEntry> = when (state.selectedGrowthMetric) {
        GrowthMetric.WEIGHT -> state.weightRecords
        GrowthMetric.HEIGHT -> state.heightRecords
        GrowthMetric.HEAD   -> state.headRecords
    }

    // Fallback to static data when DB is empty
    val staticData = state.growthDataMap[state.selectedGrowthMetric]

    // Chart points: prefer DB records if we have any
    val chartPoints: List<Float> = if (dbRecords.isNotEmpty())
        dbRecords.reversed().map { it.value.replace(Regex("[^0-9.]"), "").toFloatOrNull() ?: 0f }.takeLast(10)
    else staticData?.chartPoints ?: emptyList()

    val unit = when (state.selectedGrowthMetric) {
        GrowthMetric.WEIGHT -> "g"
        else                -> "cm"
    }

    // Current value = latest record
    val currentValue = dbRecords.firstOrNull()?.value ?: staticData?.currentValue ?: "—"
    val birthValue   = staticData?.birthValue ?: "—"
    val percentile   = staticData?.percentile ?: "—"

    // Compute gain
    val gain = if (dbRecords.size >= 2) {
        val v1 = dbRecords.first().value.replace(Regex("[^0-9.]"), "").toFloatOrNull() ?: 0f
        val v2 = dbRecords.last().value.replace(Regex("[^0-9.]"), "").toFloatOrNull() ?: 0f
        val diff = v1 - v2
        if (diff >= 0) "+${"%.1f".format(diff)} $unit" else "${"%.1f".format(diff)} $unit"
    } else staticData?.let { "+?" } ?: "—"

    val metricLabel = when (state.selectedGrowthMetric) {
        GrowthMetric.WEIGHT -> "Current weight"
        GrowthMetric.HEIGHT -> "Current height"
        GrowthMetric.HEAD   -> "Current head"
    }

    Column(Modifier.fillMaxSize().background(AppBackground)) {
        SlideHeader(
            title = "Growth Details",
            subtitle = "Tracking ${labels[selectedIndex].lowercase()} over time",
            onBack = onBack,
            trailing = {
                Box(
                    Modifier.size(30.dp).background(Color.White.copy(0.2f), CircleShape)
                        .clickable { viewModel.openAddGrowthDialog(state.selectedGrowthMetric) },
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(16.dp)) }
            },
            extra = {
                Spacer(Modifier.height(10.dp))
                SegmentControl(
                    options = labels,
                    selected = selectedIndex,
                    onSelect = { viewModel.selectGrowthMetric(metrics[it]) },
                    modifier = Modifier.fillMaxWidth().background(Color.White.copy(0.2f), RoundedCornerShape(10.dp))
                )
            }
        )

        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ── Current + percentile ─────────────────────────────────────
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column {
                    Text(metricLabel, fontSize = 10.sp, color = PurplePrimary, fontWeight = FontWeight.Bold)
                    Text(currentValue, fontSize = 26.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                }
                Box(Modifier.background(PurplePrimary, RoundedCornerShape(10.dp)).padding(horizontal = 12.dp, vertical = 6.dp)) {
                    Text(percentile, fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
                }
            }

            // ── Birth / Target / Gain row ────────────────────────────────
            Row(
                Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(14.dp)).padding(14.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                GrowthStatColumn("Birth", birthValue)
                Box(Modifier.width(1.dp).height(36.dp).background(Divider))
                GrowthStatColumn("Target (Avg)", staticData?.let { computeTarget(it.birthValue, unit) } ?: "—")
                Box(Modifier.width(1.dp).height(36.dp).background(Divider))
                GrowthStatColumn("Gain", gain)
            }

            // ── Chart ────────────────────────────────────────────────────
            AppCard {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Growth Trend", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Last ${chartPoints.size} readings", fontSize = 9.sp, color = TextSecondary)
                }
                Spacer(Modifier.height(8.dp))
                if (chartPoints.isNotEmpty()) {
                    GrowthChart(chartPoints)
                } else {
                    Box(Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                        Text("No data yet — add your first measurement ↗", fontSize = 10.sp, color = TextSecondary)
                    }
                }
            }

            // ── Measurement History ──────────────────────────────────────
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Measurement History", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                Box(
                    Modifier.size(28.dp).background(PurpleBackground, RoundedCornerShape(8.dp))
                        .clickable { viewModel.openAddGrowthDialog(state.selectedGrowthMetric) },
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Default.Add, null, tint = PurplePrimary, modifier = Modifier.size(16.dp)) }
            }

            if (dbRecords.isEmpty() && staticData != null) {
                // Show static seed entries when DB is empty
                staticData.entries.forEach { entry ->
                    AppCard {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(36.dp).background(PurpleBackground, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                                    Text(metricEmoji(state.selectedGrowthMetric), fontSize = 16.sp)
                                }
                                Column {
                                    Text(entry.value, fontSize = 13.sp, fontWeight = FontWeight.Black)
                                    Text(entry.dateLabel, fontSize = 9.sp, color = TextSecondary)
                                }
                            }
                            Text("↗", fontSize = 14.sp, color = TextSecondary)
                        }
                    }
                }
            } else {
                // Show real DB records
                dbRecords.forEach { record ->
                    AppCard {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(36.dp).background(PurpleBackground, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                                    Text(metricEmoji(state.selectedGrowthMetric), fontSize = 16.sp)
                                }
                                Column {
                                    Text(record.value, fontSize = 13.sp, fontWeight = FontWeight.Black)
                                    Text(record.dateLabel, fontSize = 9.sp, color = TextSecondary)
                                }
                            }
                            Box(
                                Modifier.size(26.dp).background(Color(0xFFFFEEEE), RoundedCornerShape(7.dp))
                                    .clickable { viewModel.deleteGrowthRecord(record) },
                                contentAlignment = Alignment.Center
                            ) { Text("🗑", fontSize = 11.sp) }
                        }
                    }
                }
            }

            if (dbRecords.isEmpty()) {
                Box(
                    Modifier.fillMaxWidth()
                        .background(Brush.linearGradient(listOf(PurpleLight, PurplePrimary)), RoundedCornerShape(14.dp))
                        .clickable { viewModel.openAddGrowthDialog(state.selectedGrowthMetric) }
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Text("Add first measurement", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
            }
        }
    }

    // Add growth dialog
    if (state.showAddGrowthDialog) {
        AddGrowthDialog(
            metric = state.growthDialogMetric,
            onSave = { value -> viewModel.saveGrowthRecord(state.growthDialogMetric, value) },
            onDismiss = { viewModel.closeAddGrowthDialog() }
        )
    }
}

private fun metricEmoji(m: GrowthMetric) = when (m) {
    GrowthMetric.WEIGHT -> "⚖️"
    GrowthMetric.HEIGHT -> "📏"
    GrowthMetric.HEAD   -> "🔵"
}

private fun computeTarget(birthValue: String, unit: String): String {
    val num = birthValue.replace(Regex("[^0-9.]"), "").toFloatOrNull() ?: return "—"
    return if (unit == "g") "${(num * 1.5f).toInt()} g" else "${"%.1f".format(num + 8)} cm"
}

@Composable
fun GrowthStatColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 8.sp, color = TextSecondary)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Black, color = TextPrimary)
    }
}
