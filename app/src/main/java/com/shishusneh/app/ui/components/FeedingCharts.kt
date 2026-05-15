package com.shishusneh.app.ui.components

import android.graphics.Color as AColor
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.shishusneh.app.data.model.HourlyFeedingBar

// ─────────────────────────────────────────────────────────────────────────────
//  MPAndroidChart — stacked bar chart for hourly feeding data
//  Breast milk = purple  |  Bottle = orange
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun FeedingBarChart(
    bars: List<HourlyFeedingBar>,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                setDrawGridBackground(false)
                setDrawBarShadow(false)
                isHighlightFullBarEnabled = false
                setDrawValueAboveBar(false)
                setPinchZoom(false)
                setScaleEnabled(false)
                isDoubleTapToZoomEnabled = false
                setExtraOffsets(0f, 4f, 0f, 4f)

                // X axis
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 2f
                    setDrawGridLines(false)
                    textColor = AColor.parseColor("#7A7A7A")
                    textSize = 9f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val h = value.toInt()
                            return when (h) {
                                0  -> "12am"
                                6  -> "6am"
                                12 -> "12pm"
                                18 -> "6pm"
                                else -> ""
                            }
                        }
                    }
                }

                // Left Y axis
                axisLeft.apply {
                    setDrawGridLines(true)
                    gridColor = AColor.parseColor("#F0F0F2")
                    textColor = AColor.parseColor("#7A7A7A")
                    textSize = 9f
                    axisMinimum = 0f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float) = "${value.toInt()}ml"
                    }
                }
                axisRight.isEnabled = false

                // Legend
                legend.apply {
                    isEnabled = true
                    form = Legend.LegendForm.SQUARE
                    textSize = 10f
                    textColor = AColor.parseColor("#444444")
                    horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                    orientation = Legend.LegendOrientation.HORIZONTAL
                }
            }
        },
        update = { chart ->
            val breastEntries = bars.map { BarEntry(it.hour.toFloat(), it.breastMl) }
            val bottleEntries = bars.map { BarEntry(it.hour.toFloat(), it.bottleMl) }

            val breastSet = BarDataSet(breastEntries, "Breast milk").apply {
                color = AColor.parseColor("#C060E8")
                setDrawValues(false)
                highLightAlpha = 90
            }
            val bottleSet = BarDataSet(bottleEntries, "Bottle").apply {
                color = AColor.parseColor("#FF9040")
                setDrawValues(false)
                highLightAlpha = 90
            }

            chart.data = BarData(breastSet, bottleSet).apply {
                barWidth = 0.4f
            }
            chart.groupBars(0f, 0.2f, 0f)
            chart.xAxis.axisMinimum = 0f
            chart.xAxis.axisMaximum = 24f
            chart.invalidate()
        }
    )
}

// ─────────────────────────────────────────────────────────────────────────────
//  Line chart for daily feeding totals (MPAndroidChart LineChart)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun FeedingLineChart(
    dailyTotals: List<Float>,   // ml per day, last 7 days
    modifier: Modifier = Modifier
) {
    val purple = AColor.parseColor("#C060E8")
    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
        factory = { context ->
            com.github.mikephil.charting.charts.LineChart(context).apply {
                description.isEnabled = false
                setDrawGridBackground(false)
                setPinchZoom(false)
                setScaleEnabled(false)
                isDoubleTapToZoomEnabled = false
                setExtraOffsets(0f, 4f, 0f, 4f)
                legend.isEnabled = false

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    textColor = AColor.parseColor("#7A7A7A")
                    textSize = 9f
                    granularity = 1f
                    valueFormatter = object : ValueFormatter() {
                        private val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                        override fun getFormattedValue(value: Float) =
                            days.getOrElse(value.toInt()) { "" }
                    }
                }
                axisLeft.apply {
                    setDrawGridLines(true)
                    gridColor = AColor.parseColor("#F0F0F2")
                    textColor = AColor.parseColor("#7A7A7A")
                    textSize = 9f
                    axisMinimum = 0f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(v: Float) = "${v.toInt()}ml"
                    }
                }
                axisRight.isEnabled = false
            }
        },
        update = { chart ->
            val entries = dailyTotals.mapIndexed { i, v -> Entry(i.toFloat(), v) }
            val ds = LineDataSet(entries, "Daily total ml").apply {
                color = purple
                setCircleColor(purple)
                circleRadius = 4f
                lineWidth = 2.5f
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
                fillColor = purple
                fillAlpha = 30
                setDrawFilled(true)
            }
            chart.data = LineData(ds)
            chart.invalidate()
        }
    )
}
