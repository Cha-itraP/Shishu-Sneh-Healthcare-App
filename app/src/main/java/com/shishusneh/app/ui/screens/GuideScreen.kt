package com.shishusneh.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shishusneh.app.data.model.GuideCard
import com.shishusneh.app.data.model.GuideDetail
import com.shishusneh.app.ui.components.AppCard
import com.shishusneh.app.ui.components.SlideHeader
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.ShishuUiState
import com.shishusneh.app.viewmodel.ShishuViewModel

@Composable
fun GuideScreen(state: ShishuUiState, viewModel: ShishuViewModel) {
    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 12.dp)
            ) {
                Text("Tips & Guidance 🔔", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = state.guideSearchQuery,
                    onValueChange = { viewModel.updateGuideSearch(it) },
                    placeholder = { Text("Search tips and topics…", fontSize = 11.sp, color = TextSecondary) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = TextSecondary, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Divider,
                        focusedBorderColor = PurplePrimary
                    )
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "Quick, thoughtful tips to support your first ",
                    fontSize = 11.sp, color = TextSecondary, lineHeight = 16.sp
                )
            }

            // Cards
            val filtered = if (state.guideSearchQuery.isBlank()) state.guideCards
            else state.guideCards.filter {
                it.title.contains(state.guideSearchQuery, ignoreCase = true) ||
                        it.description.contains(state.guideSearchQuery, ignoreCase = true)
            }

            Column(
                Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                filtered.forEach { card ->
                    GuideCardItem(card) { viewModel.openGuide(card.id) }
                }
            }
        }

        // Slide-in detail panel
        AnimatedVisibility(
            visible = state.selectedGuideId != null,
            enter = slideInHorizontally(initialOffsetX = { it }),
            exit = slideOutHorizontally(targetOffsetX = { it })
        ) {
            val detail = state.selectedGuideId?.let { state.guideDetails[it] }
            if (detail != null) {
                GuideDetailPanel(detail) { viewModel.closeGuide() }
            }
        }
    }
}

@Composable
fun GuideCardItem(card: GuideCard, onClick: () -> Unit) {
    val gradientColors = when (card.colorKey) {
        "green"    -> listOf(Color(0xFFC8F5D8), Color(0xFFA8E8C0))
        "pink"     -> listOf(Color(0xFFF5C8E8), Color(0xFFEAA8D8))
        "lavender" -> listOf(Color(0xFFD8C8F5), Color(0xFFC0A8EA))
        "yellow"   -> listOf(Color(0xFFF5F0C8), Color(0xFFEAE0A0))
        "blue"     -> listOf(Color(0xFFC8E8F5), Color(0xFFA8D0E8))
        else       -> listOf(Color(0xFFF5DCC8), Color(0xFFE8C4A0))
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(Brush.linearGradient(gradientColors), RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(16.dp, 14.dp),
    ) {
        Column(Modifier.fillMaxWidth(0.72f)) {
            Text(card.title, fontSize = 14.sp, fontWeight = FontWeight.Black, color = TextPrimary)
            Spacer(Modifier.height(4.dp))
            Text(card.description, fontSize = 10.sp, color = Color(0xFF444444), lineHeight = 14.sp)
        }
        Text(
            card.emoji, fontSize = 52.sp,
            modifier = Modifier.align(Alignment.BottomEnd).offset(x = 4.dp, y = 4.dp)
        )
    }
}

@Composable
fun GuideDetailPanel(detail: GuideDetail, onBack: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        SlideHeader(
            title = detail.title,
            subtitle = detail.subtitle,
            onBack = onBack
        )
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Banner
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(listOf(Color(0xFFEEE8FF), Color(0xFFE0D4F8))),
                        RoundedCornerShape(14.dp)
                    )
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(Modifier.weight(1f)) {
                    Text(detail.bannerTitle, fontSize = 13.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                    Spacer(Modifier.height(3.dp))
                    Text(detail.bannerBody, fontSize = 10.sp, color = Color(0xFF444444), lineHeight = 14.sp)
                }
                Text(detail.bannerEmoji, fontSize = 36.sp)
            }
            // Tips
            detail.tips.forEach { tip ->
                AppCard {
                    Box(
                        Modifier
                            .background(PurpleBackground, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(tip.label, fontSize = 8.sp, fontWeight = FontWeight.Black, color = PurplePrimary)
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(tip.title, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
                    Spacer(Modifier.height(2.dp))
                    Text(tip.body, fontSize = 10.sp, color = TextSecondary, lineHeight = 15.sp)
                }
            }
        }
    }
}
