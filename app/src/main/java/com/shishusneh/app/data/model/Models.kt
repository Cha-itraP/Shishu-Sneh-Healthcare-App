package com.shishusneh.app.data.model

// ── Domain models ──────────────────────────────────────────────────────────

data class Baby(
    val id: Int = 1,
    val name: String = "Krishna",
    val ageWeeks: Int = 5,
    val ageDays: Int = 35,
    val birthWeight: String = "3350g",
    val birthHeight: String = "49 cm"
)

enum class VaccineStatus { OVERDUE, UPCOMING, DONE }

data class Vaccine(
    val id: Int,
    val name: String,
    val preventsDisease: String,
    val dueDate: String,
    val status: VaccineStatus
)

enum class MilestoneCategory { COGNITIVE, LANGUAGE, MOTOR, SOCIAL }
enum class MilestoneStatus { ACHIEVED, PENDING }

data class Milestone(
    val id: Int,
    val title: String,
    val description: String,
    val ageLabel: String,
    val category: MilestoneCategory,
    var status: MilestoneStatus
)

enum class FeedingType { BREASTFEED, BOTTLE }

data class FeedingEntry(
    val id: Int,
    val type: FeedingType,
    val detail: String,
    val amount: String,
    val duration: String,
    val timeLabel: String
)

enum class GrowthMetric { WEIGHT, HEIGHT, HEAD }

data class GrowthEntry(
    val id: Int,
    val metric: GrowthMetric,
    val value: String,
    val dateLabel: String
)

data class GrowthData(
    val metric: GrowthMetric,
    val birthValue: String,
    val currentValue: String,
    val percentile: String,
    val chartPoints: List<Float>,
    val entries: List<GrowthEntry>
)

data class GuideCard(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val colorKey: String
)

data class GuideTip(
    val label: String,
    val title: String,
    val body: String
)

data class GuideDetail(
    val id: String,
    val title: String,
    val subtitle: String,
    val bannerTitle: String,
    val bannerBody: String,
    val bannerEmoji: String,
    val tips: List<GuideTip>
)

data class Notification(
    val id: Int,
    val tag: String,
    val tagType: String, // "tip" | "vax" | "reminder"
    val text: String,
    val subText: String,
    val emoji: String
)
