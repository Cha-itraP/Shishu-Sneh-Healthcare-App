package com.shishusneh.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ── Brand palette ──────────────────────────────────────────────────────────
val PurplePrimary   = Color(0xFFC060E8)
val PurpleLight     = Color(0xFFEAB8FF)
val PurpleLighter   = Color(0xFFF6D6FF)
val PurpleBackground= Color(0xFFFBF0FF)
val PurpleDark      = Color(0xFF7B4FD4)
val AppBackground   = Color(0xFFF7F7F9)
val CardBackground  = Color(0xFFFFFFFF)
val TextPrimary     = Color(0xFF1A1A1A)
val TextSecondary   = Color(0xFF7A7A7A)
val Divider         = Color(0xFFF0F0F2)
val ErrorRed        = Color(0xFFFF8A8A)
val WarningOrange   = Color(0xFFE07800)
val GreenAccent     = Color(0xFF1A7A4A)

// Guide card gradients (start colors)
val GcGreenStart    = Color(0xFFC8F5D8)
val GcPinkStart     = Color(0xFFF5C8E8)
val GcLavenderStart = Color(0xFFD8C8F5)
val GcYellowStart   = Color(0xFFF5F0C8)
val GcBlueStart     = Color(0xFFC8E8F5)
val GcPeachStart    = Color(0xFFF5DCC8)

// ── Color scheme ──────────────────────────────────────────────────────────
private val LightColors = lightColorScheme(
    primary         = PurplePrimary,
    onPrimary       = Color.White,
    primaryContainer= PurpleBackground,
    secondary       = PurpleDark,
    background      = AppBackground,
    surface         = CardBackground,
    onBackground    = TextPrimary,
    onSurface       = TextPrimary,
    error           = ErrorRed
)

@Composable
fun ShishuSnehTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
