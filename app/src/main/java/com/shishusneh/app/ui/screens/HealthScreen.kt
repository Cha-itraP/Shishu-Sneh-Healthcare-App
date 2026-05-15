package com.shishusneh.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import com.shishusneh.app.data.model.Vaccine
import com.shishusneh.app.data.model.VaccineStatus
import com.shishusneh.app.ui.components.AppCard
import com.shishusneh.app.ui.components.SegmentControl
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.ShishuUiState
import com.shishusneh.app.viewmodel.ShishuViewModel

@Composable
fun HealthScreen(state: ShishuUiState, viewModel: ShishuViewModel) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        // Header
        Box(
            Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(PurpleLighter, Color(0xFFEAB8FF))))
                .padding(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 14.dp)
        ) {
            Column {
                Text("🏥 Health", fontSize = 11.sp, color = Color(0xFF555555), fontWeight = FontWeight.SemiBold)
                Text("Vaccination Calendar", fontSize = 20.sp, fontWeight = FontWeight.Black, color = TextPrimary)
            }
        }

        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            SegmentControl(
                options = listOf("📋 Upcoming", "✅ Completed"),
                selected = if (state.vaccinationShowCompleted) 1 else 0,
                onSelect = { viewModel.setVaccinationTab(it == 1) }
            )

            if (!state.vaccinationShowCompleted) {
                // Upcoming vaccines
                val upcoming = state.vaccines.filter { it.status != VaccineStatus.DONE }
                AppCard {
                    upcoming.forEachIndexed { i, vax ->
                        VaccineItem(vax, i < upcoming.lastIndex) {
                            viewModel.markVaccineDone(vax.id)
                        }
                    }
                }
                Text(
                    "0 / 22 vaccinations completed",
                    fontSize = 10.sp, color = TextSecondary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                // Completed vaccines
                val done = state.vaccines.filter { it.status == VaccineStatus.DONE }
                if (done.isEmpty()) {
                    AppCard {
                        Column(
                            Modifier.fillMaxWidth().padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🌟", fontSize = 28.sp)
                            Spacer(Modifier.height(6.dp))
                            Text("No vaccinations marked yet", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                            Text("Tap \"Mark Done\" to record completed vaccines", fontSize = 10.sp, color = TextSecondary)
                        }
                    }
                } else {
                    AppCard {
                        done.forEachIndexed { i, vax ->
                            VaccineItem(vax, i < done.lastIndex, onMark = {})
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VaccineItem(vaccine: Vaccine, showDivider: Boolean, onMark: () -> Unit) {
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("💉", fontSize = 20.sp, modifier = Modifier.width(32.dp))
            Column(Modifier.weight(1f)) {
                Text(vaccine.name, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                Text("Prevents: ${vaccine.preventsDisease}", fontSize = 9.sp, color = TextSecondary)
                val (dueColor, duePrefix) = when (vaccine.status) {
                    VaccineStatus.OVERDUE  -> ErrorRed to "Overdue"
                    VaccineStatus.UPCOMING -> Color(0xFFE07800) to "Due"
                    VaccineStatus.DONE     -> Color(0xFF1A7A4A) to "Completed"
                }
                Text(
                    "$duePrefix: ${vaccine.dueDate}",
                    fontSize = 9.sp, color = dueColor, fontWeight = FontWeight.Bold
                )
            }
            if (vaccine.status != VaccineStatus.DONE) {
                Box(
                    Modifier
                        .background(PurpleBackground, RoundedCornerShape(8.dp))
                        .clickable { onMark() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("Mark Done", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = PurplePrimary)
                }
            } else {
                Box(
                    Modifier
                        .background(Color(0xFFE8FFE8), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("✓ Done", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1A7A4A))
                }
            }
        }
        if (showDivider) HorizontalDivider(color = Divider, thickness = 1.dp)
    }
}
