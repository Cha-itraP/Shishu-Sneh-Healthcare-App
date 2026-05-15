package com.shishusneh.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shishusneh.app.data.model.Notification
import com.shishusneh.app.ui.theme.*

// ── Gradient background ────────────────────────────────────────────────────

@Composable
fun GradientBox(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(PurpleLighter, PurpleLight),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.background(Brush.linearGradient(colors)),
        content = content
    )
}

// ── Pill / Badge chip ──────────────────────────────────────────────────────

@Composable
fun AgeBadge(text: String) {
    Box(
        modifier = Modifier
            .background(PurplePrimary.copy(alpha = 0.75f), RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 3.dp)
    ) {
        Text(text, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

// ── Section card ──────────────────────────────────────────────────────────

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    tint: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (tint) PurpleBackground else CardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        content = { Column(Modifier.padding(12.dp, 10.dp), content = content) }
    )
}

// ── Segment control ────────────────────────────────────────────────────────

@Composable
fun SegmentControl(
    options: List<String>,
    selected: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Divider, RoundedCornerShape(12.dp))
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        options.forEachIndexed { i, label ->
            val active = i == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (active) CardBackground else Color.Transparent)
                    .clickable { onSelect(i) }
                    .padding(vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    label, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold,
                    color = if (active) PurplePrimary else TextSecondary
                )
            }
        }
    }
}

// ── Progress bar ──────────────────────────────────────────────────────────

@Composable
fun GradientProgressBar(progress: Float, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(7.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Divider)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .fillMaxHeight()
                .background(
                    Brush.horizontalGradient(listOf(PurpleLight, PurplePrimary))
                )
        )
    }
}

// ── Slide-in back header ───────────────────────────────────────────────────

@Composable
fun SlideHeader(
    title: String,
    subtitle: String = "",
    gradientColors: List<Color> = listOf(PurpleDark, Color(0xFF9B6FE4)),
    onBack: () -> Unit,
    trailing: @Composable (() -> Unit)? = null,
    extra: @Composable ColumnScope.() -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(gradientColors))
            .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Column {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack, null,
                        tint = Color.White, modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(title, fontSize = 17.sp, fontWeight = FontWeight.Black, color = Color.White)
                    if (subtitle.isNotEmpty())
                        Text(subtitle, fontSize = 10.sp, color = Color.White.copy(alpha = 0.85f))
                }
                if (trailing != null) {
                    Box(Modifier.size(32.dp)) { trailing() }
                } else {
                    Spacer(Modifier.width(32.dp))
                }
            }
            extra()
        }
    }
}

// ── Notification overlay ──────────────────────────────────────────────────

@Composable
fun NotificationsOverlay(
    notifications: List<Notification>,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.45f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .padding(top = 60.dp)
                .fillMaxWidth(0.92f)
                .clickable { /* consume */ },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column {
                // header
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(listOf(PurplePrimary, PurpleDark)),
                            RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        )
                        .padding(14.dp, 14.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Notifications 🔔", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Box(
                            Modifier
                                .size(24.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                .clickable { onDismiss() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("×", fontSize = 16.sp, color = Color.White)
                        }
                    }
                }
                notifications.forEachIndexed { i, n ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .then(if (i < notifications.lastIndex) Modifier.border(0.dp, Color.Transparent) else Modifier)
                            .padding(12.dp, 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(n.emoji, fontSize = 20.sp)
                        Column(Modifier.weight(1f)) {
                            val (bg, fg) = when (n.tagType) {
                                "vax"      -> Color(0xFFFFE8E8) to Color(0xFFCC2020)
                                "tip"      -> Color(0xFFE8FFE8) to Color(0xFF1A7A4A)
                                else       -> Color(0xFFFFF0D0) to Color(0xFFC07000)
                            }
                            Box(
                                Modifier
                                    .background(bg, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                            ) {
                                Text(n.tag, fontSize = 8.sp, fontWeight = FontWeight.ExtraBold, color = fg)
                            }
                            Spacer(Modifier.height(3.dp))
                            Text(n.text, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(n.subText, fontSize = 9.sp, color = TextSecondary)
                        }
                    }
                    if (i < notifications.lastIndex)
                        Divider(Modifier.padding(horizontal = 14.dp), color = com.shishusneh.app.ui.theme.Divider)
                }
            }
        }
    }
}

// ── Simple line chart with percentile bands (Canvas-based) ─────────────────

@Composable
fun GrowthChart(
    points: List<Float>,
    modifier: Modifier = Modifier
) {
    val purple = PurplePrimary
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .drawBehind {
                val w = size.width
                val h = size.height
                val minV = (points.minOrNull() ?: 0f) * 0.88f
                val maxV = (points.maxOrNull() ?: 1f) * 1.08f
                val range = maxV - minV

                // Percentile bands
                val bandAlphas = listOf(0.08f, 0.12f, 0.16f, 0.20f, 0.16f, 0.12f, 0.08f)
                val bandFractions = listOf(
                    1f to 0.98f, 0.98f to 0.90f, 0.90f to 0.75f, 0.75f to 0.50f,
                    0.50f to 0.25f, 0.25f to 0.10f, 0.10f to 0f
                )
                bandFractions.forEachIndexed { i, (hi, lo) ->
                    val y0 = h - h * hi
                    val y1 = h - h * lo
                    drawRect(
                        color = Color(0xFF8C50FF).copy(alpha = bandAlphas[i]),
                        topLeft = Offset(0f, y0),
                        size = androidx.compose.ui.geometry.Size(w, y1 - y0)
                    )
                }

                if (points.size < 2) return@drawBehind

                fun toX(i: Int) = i * w / (points.size - 1)
                fun toY(v: Float) = h - ((v - minV) / range) * h * 0.9f - h * 0.05f

                // Line
                val path = Path()
                points.forEachIndexed { i, v ->
                    if (i == 0) path.moveTo(toX(i), toY(v))
                    else path.lineTo(toX(i), toY(v))
                }
                drawPath(path, color = Color(0xFF222222), style = Stroke(width = 4f))

                // Dots
                points.forEachIndexed { i, v ->
                    drawCircle(Color(0xFFE83070), radius = 8f, center = Offset(toX(i), toY(v)))
                    drawCircle(Color.White, radius = 4f, center = Offset(toX(i), toY(v)))
                }
            }
    )
}

// ── Feeding spark chart ────────────────────────────────────────────────────

@Composable
fun FeedingSparkChart(modifier: Modifier = Modifier) {
    val pts = listOf(0.5f, 0.35f, 0.6f, 0.3f, 0.72f, 0.28f, 0.65f, 0.38f, 0.7f, 0.32f, 0.55f, 0.4f, 0.58f)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .drawBehind {
                val w = size.width
                val h = size.height
                val xs = pts.mapIndexed { i, _ -> i * w / (pts.size - 1) }
                val ys = pts.map { 0.08f * h + it * 0.78f * h }

                // Fill
                val fill = Path()
                fill.moveTo(xs[0], ys[0])
                for (i in 1 until pts.size) {
                    val cx = (xs[i - 1] + xs[i]) / 2
                    fill.quadraticBezierTo(xs[i - 1], ys[i - 1], cx, (ys[i - 1] + ys[i]) / 2)
                }
                fill.lineTo(w, h); fill.lineTo(0f, h); fill.close()
                drawPath(fill, Brush.verticalGradient(listOf(Color(0xFFC060E8).copy(0.22f), Color.Transparent)))

                // Line
                val line = Path()
                line.moveTo(xs[0], ys[0])
                for (i in 1 until pts.size) {
                    val cx = (xs[i - 1] + xs[i]) / 2
                    line.quadraticBezierTo(xs[i - 1], ys[i - 1], cx, (ys[i - 1] + ys[i]) / 2)
                }
                drawPath(line, color = Color(0xFFC060E8), style = Stroke(4f))

                // Dot
                val pi = 6
                drawCircle(Color(0xFFC060E8), radius = 10f, center = Offset(xs[pi], ys[pi]))
                drawCircle(Color.White, radius = 6f, center = Offset(xs[pi], ys[pi]))
            }
    )
}
