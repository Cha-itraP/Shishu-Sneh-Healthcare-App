package com.shishusneh.app.data.db

import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class ShishuDbRepository(private val db: ShishuDatabase) {

    // ── Breastfeed ────────────────────────────────────────────────────────
    val breastfeedFlow: Flow<List<BreastfeedSessionEntity>> = db.breastfeedDao().getAllFlow()

    suspend fun saveBreastfeed(
        leftMin: Int, leftSec: Int,
        rightMin: Int, rightSec: Int,
        noteTag: String
    ) {
        val totalMl = ((leftMin * 60 + leftSec) * 8.5f / 60 +
                       (rightMin * 60 + rightSec) * 8.5f / 60).toInt()
        db.breastfeedDao().insert(
            BreastfeedSessionEntity(
                leftMinutes = leftMin, leftSeconds = leftSec,
                rightMinutes = rightMin, rightSeconds = rightSec,
                estimatedMl = totalMl, noteTag = noteTag
            )
        )
    }

    suspend fun deleteBreastfeed(e: BreastfeedSessionEntity) = db.breastfeedDao().delete(e)

    // ── Bottle ────────────────────────────────────────────────────────────
    val bottleFlow: Flow<List<BottleSessionEntity>> = db.bottleDao().getAllFlow()

    suspend fun saveBottle(contentType: String, oz: Float, durationMin: Int) {
        db.bottleDao().insert(
            BottleSessionEntity(
                contentType = contentType,
                amountOz = oz,
                amountMl = (oz * 29.5735f).toInt(),
                durationMinutes = durationMin
            )
        )
    }

    suspend fun deleteBottle(e: BottleSessionEntity) = db.bottleDao().delete(e)

    // ── Growth ────────────────────────────────────────────────────────────
    fun growthFlowFor(metric: String): Flow<List<GrowthRecordEntity>> =
        db.growthDao().getByMetricFlow(metric)

    val allGrowthFlow: Flow<List<GrowthRecordEntity>> = db.growthDao().getAllFlow()

    suspend fun saveGrowth(metric: String, value: Float, unit: String) {
        db.growthDao().insert(GrowthRecordEntity(metric = metric, value = value, unit = unit))
    }

    suspend fun deleteGrowth(e: GrowthRecordEntity) = db.growthDao().delete(e)

    // ── Helpers ───────────────────────────────────────────────────────────
    companion object {
        private val fmt = SimpleDateFormat("dd.MM.yyyy  HH:mm", Locale.getDefault())
        fun formatTs(ms: Long): String = fmt.format(Date(ms))

        fun formatBreastLabel(e: BreastfeedSessionEntity): String {
            val side = when {
                e.leftMinutes > 0 && e.rightMinutes > 0 -> "Both"
                e.leftMinutes > 0 -> "Left"
                else -> "Right"
            }
            val lStr = if (e.leftMinutes > 0 || e.leftSeconds > 0)
                "L %02d:%02d".format(e.leftMinutes, e.leftSeconds) else ""
            val rStr = if (e.rightMinutes > 0 || e.rightSeconds > 0)
                "R %02d:%02d".format(e.rightMinutes, e.rightSeconds) else ""
            val parts = listOf(lStr, rStr).filter { it.isNotBlank() }.joinToString(" + ")
            return "$side · $parts · ~${e.estimatedMl} ml"
        }
    }
}
