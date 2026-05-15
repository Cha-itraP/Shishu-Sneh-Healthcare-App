package com.shishusneh.app.data

import com.shishusneh.app.data.model.*
import java.time.LocalTime

object FeedingRepository {

    // ── Seed breastfeed logs ───────────────────────────────────────────────
    val breastfeedLogs = mutableListOf(
        BreastfeedLog(1, BreastSide.BOTH,  leftMinutes = 12, rightMinutes = 10, LocalTime.of(1,  0),  "Today",   "Strong latch"),
        BreastfeedLog(2, BreastSide.LEFT,  leftMinutes = 15, rightMinutes = 0,  LocalTime.of(4,  30), "Today",   ""),
        BreastfeedLog(3, BreastSide.BOTH,  leftMinutes = 10, rightMinutes = 12, LocalTime.of(8,  0),  "Today",   "Cluster feed"),
        BreastfeedLog(4, BreastSide.RIGHT, leftMinutes = 0,  rightMinutes = 18, LocalTime.of(11, 0),  "Today",   ""),
        BreastfeedLog(5, BreastSide.BOTH,  leftMinutes = 11, rightMinutes = 9,  LocalTime.of(14, 30), "Today",   ""),
        BreastfeedLog(6, BreastSide.LEFT,  leftMinutes = 14, rightMinutes = 0,  LocalTime.of(18, 0),  "Today",   ""),
        BreastfeedLog(7, BreastSide.BOTH,  leftMinutes = 10, rightMinutes = 8,  LocalTime.of(22, 0),  "Today",   "")
    )

    // ── Seed bottle logs ───────────────────────────────────────────────────
    val bottleLogs = mutableListOf(
        BottleLog(1, BottleContent.FORMULA,     amountOz = 2.0f, durationMinutes = 12, LocalTime.of(6,  0),  "Today"),
        BottleLog(2, BottleContent.BREAST_MILK, amountOz = 2.5f, durationMinutes = 15, LocalTime.of(13, 0),  "Today"),
        BottleLog(3, BottleContent.FORMULA,     amountOz = 2.0f, durationMinutes = 10, LocalTime.of(20, 0),  "Today")
    )

    // ── Aggregation ────────────────────────────────────────────────────────

    fun buildHourlyBars(): List<HourlyFeedingBar> {
        val bars = Array(24) { HourlyFeedingBar(it, 0f, 0f) }
        breastfeedLogs.forEach { log ->
            val h = log.timeOfDay.hour
            bars[h] = bars[h].copy(breastMl = bars[h].breastMl + log.totalEstimatedMl)
        }
        bottleLogs.forEach { log ->
            val h = log.timeOfDay.hour
            bars[h] = bars[h].copy(bottleMl = bars[h].bottleMl + log.amountMl)
        }
        return bars.toList()
    }

    fun buildSummary(): FeedingSummary {
        val allTimes = (breastfeedLogs.map { it.timeOfDay } + bottleLogs.map { it.timeOfDay })
            .sortedBy { it.toSecondOfDay() }
        val intervals = if (allTimes.size > 1)
            allTimes.zipWithNext { a, b -> (b.toSecondOfDay() - a.toSecondOfDay()) / 60 }
        else listOf(0)
        val breastMl = breastfeedLogs.sumOf { it.totalEstimatedMl }
        val bottleMl = bottleLogs.sumOf { it.amountMl }
        val longestGap = intervals.maxOrNull()?.toFloat()?.div(60f) ?: 0f
        return FeedingSummary(
            totalFeedings = breastfeedLogs.size + bottleLogs.size,
            totalMl = breastMl + bottleMl,
            avgIntervalMinutes = if (intervals.isNotEmpty()) intervals.average().toInt() else 0,
            breastMl = breastMl,
            bottleMl = bottleMl,
            longestSleepGapHours = longestGap
        )
    }

    // ── Nutrition tips ─────────────────────────────────────────────────────
    val nutritionTips = listOf(
        NutritionTip(1,  NutritionFor.MOTHER, "🥛", "Dairy for Calcium",
            "Drink 3 cups of milk or eat yogurt/cheese daily. Calcium in your milk comes from your own bones if you don't get enough.", "blue"),
        NutritionTip(2,  NutritionFor.MOTHER, "🫘", "Iron-Rich Lentils",
            "Eat rajma, chana, or masoor dal daily. Pair with vitamin-C foods (tomato, lemon) to triple iron absorption.", "green"),
        NutritionTip(3,  NutritionFor.MOTHER, "💧", "Hydrate Before Feeds",
            "Drink a glass of water before every breastfeed. Breast milk is 88% water — dehydration drops supply fast.", "blue"),
        NutritionTip(4,  NutritionFor.MOTHER, "🌿", "Galactagogues",
            "Methi seeds, moringa leaves, oats, fennel water, and jeera boost milk supply. Include one at every meal.", "green"),
        NutritionTip(5,  NutritionFor.MOTHER, "🥚", "Protein at Every Meal",
            "Target 71 g protein/day. Eggs, paneer, dal, chicken, and nuts keep your energy up through cluster-feed nights.", "yellow"),
        NutritionTip(6,  NutritionFor.MOTHER, "🐟", "Omega-3 for Baby's Brain",
            "Eat fatty fish (rohu, salmon) 2×/week or take a DHA supplement. DHA passes through breast milk and builds baby's brain.", "pink"),
        NutritionTip(7,  NutritionFor.BABY,   "🤱", "Colostrum is Gold",
            "If within the first 5 days, every drop of colostrum counts. It's packed with antibodies that protect for months.", "yellow"),
        NutritionTip(8,  NutritionFor.BABY,   "⏱️", "Feed Every 2–3 Hours",
            "Newborns have tiny stomachs (~60 ml). Frequent feeds prevent jaundice, maintain blood sugar, and build your supply.", "green"),
        NutritionTip(9,  NutritionFor.BABY,   "⚖️", "Watch for Wet Diapers",
            "6+ wet diapers per day means baby is getting enough milk. Fewer diapers = possible low intake — check latch first.", "blue"),
        NutritionTip(10, NutritionFor.BABY,   "🍼", "Bottle Pacing",
            "Hold the bottle horizontally and pause every 20–30 seconds. Paced feeding prevents overfeeding and colic.", "pink"),
        NutritionTip(11, NutritionFor.BOTH,   "🌙", "Night Feeds Matter",
            "Prolactin peaks between 2–5 am. Night feeds are the most powerful for maintaining long-term milk supply.", "lavender" ),
        NutritionTip(12, NutritionFor.BOTH,   "🧘", "Stress Lowers Supply",
            "Cortisol blocks oxytocin. 5 deep breaths before a feed, skin-to-skin, and letting go of the phone helps letdown.", "pink")
    )
}
