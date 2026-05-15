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
import com.shishusneh.app.data.model.NutritionFor
import com.shishusneh.app.data.model.NutritionTip
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.ShishuUiState
import com.shishusneh.app.viewmodel.ShishuViewModel

@Composable
fun NutritionTipsScreen(state: ShishuUiState, viewModel: ShishuViewModel, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(AppBackground)) {

        // Header
        Box(
            Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(Color(0xFF1A7A4A), Color(0xFF2DA868))))
                .padding(start = 16.dp, end = 16.dp, top = 44.dp, bottom = 16.dp)
        ) {
            Column {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            Modifier
                                .size(30.dp)
                                .background(Color.White.copy(0.2f), RoundedCornerShape(10.dp))
                                .clickable { onBack() },
                            contentAlignment = Alignment.Center
                        ) { Text("←", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold) }
                        Column {
                            Text("Nutrition Tips 🥗", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                            Text("For mother & baby · ${state.nutritionTips.size} tips", fontSize = 10.sp, color = Color.White.copy(0.8f))
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Filter chips
                Row(
                    Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val filters: List<Pair<NutritionFor?, String>> = listOf(
                        null to "✓ All",
                        NutritionFor.MOTHER to "👩 Mother",
                        NutritionFor.BABY   to "👶 Baby",
                        NutritionFor.BOTH   to "🌙 Both"
                    )
                    filters.forEach { (filter, label) ->
                        val active = state.nutritionFilter == filter
                        Box(
                            Modifier
                                .background(
                                    if (active) Color.White else Color.White.copy(0.2f),
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { viewModel.setNutritionFilter(filter) }
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text(label, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold,
                                color = if (active) Color(0xFF1A7A4A) else Color.White)
                        }
                    }
                }
            }
        }

        // Tip cards
        val filtered = if (state.nutritionFilter == null) state.nutritionTips
        else state.nutritionTips.filter { it.forWho == state.nutritionFilter || it.forWho == NutritionFor.BOTH }

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            filtered.forEach { tip -> NutritionTipCard(tip) }
        }
    }
}

@Composable
fun NutritionTipCard(tip: NutritionTip) {
    val (bg1, bg2) = when (tip.colorKey) {
        "green"    -> Color(0xFFF0FFF4) to Color(0xFFD4F7E4)
        "pink"     -> Color(0xFFFFF0F4) to Color(0xFFFFD4DF)
        "yellow"   -> Color(0xFFFFFDF0) to Color(0xFFFFF3C8)
        "lavender" -> Color(0xFFF4F0FF) to Color(0xFFE4D8FF)
        else       -> Color(0xFFF0F8FF) to Color(0xFFD4ECFF)
    }
    val forLabel = when (tip.forWho) {
        NutritionFor.MOTHER -> "👩 Mother"
        NutritionFor.BABY   -> "👶 Baby"
        NutritionFor.BOTH   -> "🌙 Both"
    }
    val forColor = when (tip.forWho) {
        NutritionFor.MOTHER -> Color(0xFF9B4FE4)
        NutritionFor.BABY   -> Color(0xFF1A7AB4)
        NutritionFor.BOTH   -> Color(0xFF1A7A4A)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(bg1, bg2)))
                .padding(14.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(tip.emoji, fontSize = 26.sp, modifier = Modifier.padding(top = 2.dp))
                Column(Modifier.weight(1f)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(tip.title, fontSize = 12.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                        Box(
                            Modifier
                                .background(forColor.copy(0.12f), RoundedCornerShape(10.dp))
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(forLabel, fontSize = 8.sp, fontWeight = FontWeight.ExtraBold, color = forColor)
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(tip.body, fontSize = 10.sp, color = Color(0xFF333333), lineHeight = 15.sp)
                }
            }
        }
    }
}
