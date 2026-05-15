package com.shishusneh.app.data.model

import java.time.LocalTime

// ── Feeding Log ────────────────────────────────────────────────────────────

enum class BreastSide { LEFT, RIGHT, BOTH }

/**
 * A logged breastfeed session.
 * leftMinutes / rightMinutes are the actual timed durations.
 */
data class BreastfeedLog(
    val id: Int,
    val side: BreastSide,
    val leftMinutes: Int,      // 0 if not used
    val rightMinutes: Int,     // 0 if not used
    val timeOfDay: LocalTime = LocalTime.now(),
    val dateLabel: String = "Today",
    val noteTag: String = ""   // e.g. "Strong latch", "Cluster feed"
) {
    val totalMinutes: Int get() = leftMinutes + rightMinutes
    val estimatedMlLeft: Int get() = (leftMinutes * 8.5f).toInt()   // ~8.5 ml/min avg
    val estimatedMlRight: Int get() = (rightMinutes * 8.5f).toInt()
    val totalEstimatedMl: Int get() = estimatedMlLeft + estimatedMlRight
}

/**
 * A logged bottle / formula session.
 * amountOz is entered by user; amountMl is calculated.
 */
data class BottleLog(
    val id: Int,
    val contentType: BottleContent,
    val amountOz: Float,
    val durationMinutes: Int,
    val timeOfDay: LocalTime = LocalTime.now(),
    val dateLabel: String = "Today"
) {
    val amountMl: Int get() = (amountOz * 29.5735f).toInt()
}

enum class BottleContent { BREAST_MILK, FORMULA, WATER }

// ── Chart aggregation ──────────────────────────────────────────────────────

/** One bar in the hourly feeding chart (MPAndroidChart BarEntry). */
data class HourlyFeedingBar(
    val hour: Int,           // 0–23
    val breastMl: Float,
    val bottleMl: Float
) {
    val totalMl: Float get() = breastMl + bottleMl
}

/** Summary stats shown above the chart. */
data class FeedingSummary(
    val totalFeedings: Int,
    val totalMl: Int,
    val avgIntervalMinutes: Int,
    val breastMl: Int,
    val bottleMl: Int,
    val longestSleepGapHours: Float
)

// ── Nutrition tip ──────────────────────────────────────────────────────────

data class NutritionTip(
    val id: Int,
    val forWho: NutritionFor,
    val emoji: String,
    val title: String,
    val body: String,
    val colorKey: String  // "green" | "pink" | "yellow" | "blue"
)

enum class NutritionFor { MOTHER, BABY, BOTH }
