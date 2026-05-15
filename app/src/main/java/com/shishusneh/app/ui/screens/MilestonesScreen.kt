package com.shishusneh.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shishusneh.app.data.model.Milestone
import com.shishusneh.app.data.model.MilestoneCategory
import com.shishusneh.app.data.model.MilestoneStatus
import com.shishusneh.app.ui.components.AppCard
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.ShishuUiState
import com.shishusneh.app.viewmodel.ShishuViewModel

@Composable
fun MilestonesScreen(state: ShishuUiState, viewModel: ShishuViewModel) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        // Header
        Box(
            Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(PurpleLighter, Color(0xFFEAB8FF))))
                .padding(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 16.dp)
        ) {
            Column {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("🏆", fontSize = 18.sp)
                        Text("Milestone", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                    }
                }
                Text("Track milestone for ${state.baby.name}", fontSize = 10.sp, color = Color(0xFF666666), fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                // Progress row
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(8.dp, 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DonutChart(progress = 0.25f, label = "25%")
                    Column {
                        Text("Overall Progress", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                        val achieved = state.milestones.count { it.status == MilestoneStatus.ACHIEVED }
                        Text("$achieved of ${state.milestones.size} milestones reached", fontSize = 9.sp, color = Color(0xFF666666))
                    }
                }
            }
        }

        // Age chips
        val ageChips = listOf("✓ All", "👶 1 Month", "👶 2 Months", "🧒 3 Months")
        var selectedAge by remember { mutableIntStateOf(0) }
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ageChips.forEachIndexed { i, label ->
                val active = i == selectedAge
                Box(
                    Modifier
                        .background(
                            if (active) Brush.linearGradient(listOf(Color(0xFFE870FF), PurplePrimary))
                            else Brush.linearGradient(listOf(Color.White, Color.White)),
                            RoundedCornerShape(20.dp)
                        )
                        .border(2.dp, if (active) Color.Transparent else Divider, RoundedCornerShape(20.dp))
                        .clickable { selectedAge = i }
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                        color = if (active) Color.White else TextSecondary)
                }
            }
        }

        // Category filter
        val categories: List<MilestoneCategory?> = listOf(null, MilestoneCategory.MOTOR, MilestoneCategory.SOCIAL, MilestoneCategory.LANGUAGE, MilestoneCategory.COGNITIVE)
        val catLabels = listOf("✓ All", "🏃 Motor", "😊 Social", "💬 Language", "🧠 Cognitive")
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            categories.forEachIndexed { i, cat ->
                val active = state.selectedMilestoneCategory == cat
                Box(
                    Modifier
                        .background(if (active) PurpleBackground else Color.White, RoundedCornerShape(16.dp))
                        .border(1.5.dp, if (active) PurpleLight else Divider, RoundedCornerShape(16.dp))
                        .clickable { viewModel.filterMilestoneCategory(cat) }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(catLabels[i], fontSize = 9.sp, fontWeight = FontWeight.ExtraBold,
                        color = if (active) PurplePrimary else TextSecondary)
                }
            }
        }

        // Milestone list
        Column(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val filtered = if (state.selectedMilestoneCategory == null) state.milestones
            else state.milestones.filter { it.category == state.selectedMilestoneCategory }
            filtered.forEach { ms ->
                MilestoneCard(ms) { viewModel.markMilestoneDone(ms.id) }
            }
        }
    }
}

@Composable
fun DonutChart(progress: Float, label: String) {
    Box(Modifier.size(44.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(44.dp).rotate(-90f)) {
            val strokeWidth = 12f
            drawArc(
                color = Color(0xFFF0D8FF),
                startAngle = 0f, sweepAngle = 360f,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )
            drawArc(
                color = PurplePrimary,
                startAngle = 0f, sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )
        }
        Text(label, fontSize = 9.sp, fontWeight = FontWeight.Black, color = PurplePrimary)
    }
}

@Composable
fun MilestoneCard(milestone: Milestone, onMarkDone: () -> Unit) {
    val achieved = milestone.status == MilestoneStatus.ACHIEVED
    AppCard(
        modifier = Modifier.border(
            width = 3.dp,
            color = if (achieved) Color(0xFFB8F0D0) else Divider,
            shape = RoundedCornerShape(16.dp)
        )
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top) {
            // Badge
            Box(
                Modifier
                    .size(38.dp)
                    .background(
                        if (achieved) Brush.linearGradient(listOf(Color(0xFFC8FFE0), Color(0xFFA8EFC0)))
                        else Brush.linearGradient(listOf(PurpleBackground, PurpleBackground)),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (achieved) {
                    Text("✓", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFF1A7A4A))
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(categoryEmoji(milestone.category), fontSize = 13.sp)
                        Text(milestone.ageLabel, fontSize = 7.sp, fontWeight = FontWeight.Black, color = PurplePrimary)
                    }
                }
            }
            Column(Modifier.weight(1f)) {
                Text(milestone.title, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                Text(milestone.description, fontSize = 10.sp, color = TextSecondary, lineHeight = 14.sp)
                Spacer(Modifier.height(4.dp))
                val (tagBg, tagFg) = categoryColor(milestone.category)
                Box(
                    Modifier.background(tagBg, RoundedCornerShape(10.dp)).padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("${categoryEmoji(milestone.category)} ${categoryLabel(milestone.category)}", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = tagFg)
                }
                if (!achieved) {
                    Spacer(Modifier.height(6.dp))
                    Box(
                        Modifier
                            .background(
                                Brush.linearGradient(listOf(PurpleLight, PurplePrimary)),
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { onMarkDone() }
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Text("YES ✓", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
            }
        }
    }
}

private fun categoryEmoji(cat: MilestoneCategory) = when (cat) {
    MilestoneCategory.COGNITIVE -> "🧠"
    MilestoneCategory.LANGUAGE  -> "💬"
    MilestoneCategory.MOTOR     -> "🏃"
    MilestoneCategory.SOCIAL    -> "😊"
}

private fun categoryLabel(cat: MilestoneCategory) = when (cat) {
    MilestoneCategory.COGNITIVE -> "Cognitive"
    MilestoneCategory.LANGUAGE  -> "Language"
    MilestoneCategory.MOTOR     -> "Motor"
    MilestoneCategory.SOCIAL    -> "Social"
}

private fun categoryColor(cat: MilestoneCategory): Pair<Color, Color> = when (cat) {
    MilestoneCategory.COGNITIVE -> Color(0xFFFFF0E0) to Color(0xFFE07800)
    MilestoneCategory.LANGUAGE  -> Color(0xFFE8F4FF) to Color(0xFF0070C0)
    MilestoneCategory.MOTOR     -> Color(0xFFE8FFE8) to Color(0xFF007830)
    MilestoneCategory.SOCIAL    -> Color(0xFFFFF8E0) to Color(0xFFC07800)
}
