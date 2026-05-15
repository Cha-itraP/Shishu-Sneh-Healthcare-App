package com.shishusneh.app.viewmodel

import androidx.lifecycle.ViewModel
import com.shishusneh.app.data.FeedingRepository
import com.shishusneh.app.data.ShishuRepository
import com.shishusneh.app.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalTime

// ── UI State ───────────────────────────────────────────────────────────────

enum class Screen { HOME, GUIDE, LOG, MILESTONES, HEALTH }

data class ShishuUiState(
    val baby: Baby = ShishuRepository.baby,
    val currentScreen: Screen = Screen.HOME,
    val vaccines: List<Vaccine> = ShishuRepository.vaccines,
    val milestones: List<Milestone> = ShishuRepository.milestones.toList(),
    val feedingEntries: List<FeedingEntry> = ShishuRepository.feedingEntries,
    val selectedGrowthMetric: GrowthMetric = GrowthMetric.WEIGHT,
    val growthDataMap: Map<GrowthMetric, GrowthData> = ShishuRepository.growthDataMap,
    val guideCards: List<GuideCard> = ShishuRepository.guideCards,
    val guideDetails: Map<String, GuideDetail> = ShishuRepository.guideDetails,
    val notifications: List<Notification> = ShishuRepository.notifications,
    val showNotifications: Boolean = false,
    val selectedGuideId: String? = null,
    val showFeedingDetail: Boolean = false,
    val showGrowthDetail: Boolean = false,
    val showProfile: Boolean = false,
    val calendarDate: LocalDate = LocalDate.now(),
    val vaccinationShowCompleted: Boolean = false,
    val todayTip: String = ShishuRepository.todayTip,
    val eventDays: List<Int> = ShishuRepository.vaccinationEventDays,
    val selectedMilestoneCategory: MilestoneCategory? = null,
    val guideSearchQuery: String = "",

    // ── Feeding Logger ──
    val showAddFeedingSheet: Boolean = false,
    val breastfeedLogs: List<BreastfeedLog> = FeedingRepository.breastfeedLogs.toList(),
    val bottleLogs: List<BottleLog> = FeedingRepository.bottleLogs.toList(),
    val hourlyBars: List<HourlyFeedingBar> = FeedingRepository.buildHourlyBars(),
    val feedingSummary: FeedingSummary = FeedingRepository.buildSummary(),
    val nutritionTips: List<NutritionTip> = FeedingRepository.nutritionTips,
    val nutritionFilter: NutritionFor? = null,

    // ── Growth Records (UI State) ──
    val weightRecords: List<GrowthEntry> = emptyList(),
    val heightRecords: List<GrowthEntry> = emptyList(),
    val headRecords: List<GrowthEntry> = emptyList(),
    val showAddGrowthDialog: Boolean = false,
    val growthDialogMetric: GrowthMetric = GrowthMetric.WEIGHT
)

// ── ViewModel ─────────────────────────────────────────────────────────────

class ShishuViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ShishuUiState())
    val uiState: StateFlow<ShishuUiState> = _uiState.asStateFlow()

    fun navigate(screen: Screen) {
        _uiState.value = _uiState.value.copy(currentScreen = screen)
    }

    /** Called once after login/signup with real DOB-derived age */
    fun setBabyProfile(name: String, ageWeeks: Int, ageDays: Int, gender: String) {
        _uiState.value = _uiState.value.copy(
            baby = _uiState.value.baby.copy(
                name      = name.ifBlank { "Baby" },
                ageWeeks  = ageWeeks,
                ageDays   = ageDays
            )
        )
    }

    fun toggleNotifications() {
        _uiState.value = _uiState.value.copy(
            showNotifications = !_uiState.value.showNotifications
        )
    }

    fun openGuide(id: String) {
        _uiState.value = _uiState.value.copy(selectedGuideId = id)
    }

    fun closeGuide() {
        _uiState.value = _uiState.value.copy(selectedGuideId = null)
    }

    fun openFeedingDetail() {
        _uiState.value = _uiState.value.copy(showFeedingDetail = true)
    }

    fun closeFeedingDetail() {
        _uiState.value = _uiState.value.copy(showFeedingDetail = false)
    }

    fun openGrowthDetail() {
        _uiState.value = _uiState.value.copy(showGrowthDetail = true)
    }

    fun closeGrowthDetail() {
        _uiState.value = _uiState.value.copy(showGrowthDetail = false)
    }

    fun toggleProfile() {
        _uiState.value = _uiState.value.copy(showProfile = !_uiState.value.showProfile)
    }

    fun prevMonth() {
        _uiState.value = _uiState.value.copy(
            calendarDate = _uiState.value.calendarDate.minusMonths(1)
        )
    }

    fun nextMonth() {
        _uiState.value = _uiState.value.copy(
            calendarDate = _uiState.value.calendarDate.plusMonths(1)
        )
    }

    fun markVaccineDone(id: Int) {
        val updated = _uiState.value.vaccines.map {
            if (it.id == id) it.copy(status = VaccineStatus.DONE) else it
        }
        _uiState.value = _uiState.value.copy(vaccines = updated)
    }

    fun markMilestoneDone(id: Int) {
        val updated = ShishuRepository.milestones.map {
            if (it.id == id) it.copy(status = MilestoneStatus.ACHIEVED) else it
        }
        _uiState.value = _uiState.value.copy(milestones = updated)
    }

    fun selectGrowthMetric(metric: GrowthMetric) {
        _uiState.value = _uiState.value.copy(selectedGrowthMetric = metric)
    }

    fun filterMilestoneCategory(category: MilestoneCategory?) {
        _uiState.value = _uiState.value.copy(selectedMilestoneCategory = category)
    }

    fun setVaccinationTab(showCompleted: Boolean) {
        _uiState.value = _uiState.value.copy(vaccinationShowCompleted = showCompleted)
    }

    fun updateGuideSearch(query: String) {
        _uiState.value = _uiState.value.copy(guideSearchQuery = query)
    }

    // ── Feeding logger ────────────────────────────────────────────────────

    fun openAddFeedingSheet() {
        _uiState.value = _uiState.value.copy(showAddFeedingSheet = true)
    }

    fun closeAddFeedingSheet() {
        _uiState.value = _uiState.value.copy(showAddFeedingSheet = false)
    }

    fun addBreastfeedLog(side: BreastSide, leftMin: Int, rightMin: Int, note: String) {
        val newLog = BreastfeedLog(
            id = (_uiState.value.breastfeedLogs.maxOfOrNull { it.id } ?: 0) + 1,
            side = side,
            leftMinutes = leftMin,
            rightMinutes = rightMin,
            timeOfDay = LocalTime.now(),
            dateLabel = "Today",
            noteTag = note
        )
        FeedingRepository.breastfeedLogs.add(newLog)
        refreshFeedingState()
    }

    fun addBottleLog(content: BottleContent, oz: Float, durationMin: Int) {
        val newLog = BottleLog(
            id = (_uiState.value.bottleLogs.maxOfOrNull { it.id } ?: 0) + 1,
            contentType = content,
            amountOz = oz,
            durationMinutes = durationMin,
            timeOfDay = LocalTime.now(),
            dateLabel = "Today"
        )
        FeedingRepository.bottleLogs.add(newLog)
        refreshFeedingState()
    }

    private fun refreshFeedingState() {
        _uiState.value = _uiState.value.copy(
            breastfeedLogs = FeedingRepository.breastfeedLogs.toList(),
            bottleLogs = FeedingRepository.bottleLogs.toList(),
            hourlyBars = FeedingRepository.buildHourlyBars(),
            feedingSummary = FeedingRepository.buildSummary(),
            showAddFeedingSheet = false
        )
    }

    fun setNutritionFilter(filter: NutritionFor?) {
        _uiState.value = _uiState.value.copy(nutritionFilter = filter)
    }

    // ── Growth Records (Actions) ──

    fun openAddGrowthDialog(metric: GrowthMetric) {
        _uiState.value = _uiState.value.copy(showAddGrowthDialog = true, growthDialogMetric = metric)
    }

    fun closeAddGrowthDialog() {
        _uiState.value = _uiState.value.copy(showAddGrowthDialog = false)
    }

    fun saveGrowthRecord(metric: GrowthMetric, value: Float) {
        // In a real app, this would save to Room. For now, we update local state or just close.
        _uiState.value = _uiState.value.copy(showAddGrowthDialog = false)
    }

    fun deleteGrowthRecord(record: GrowthEntry) {
        // Stub for deletion
    }

    // ── Feeding Log (Deletions) ──

    fun deleteBreastfeedLog(log: BreastfeedLog) {
        FeedingRepository.breastfeedLogs.remove(log)
        refreshFeedingState()
    }

    fun deleteBottleLog(log: BottleLog) {
        FeedingRepository.bottleLogs.remove(log)
        refreshFeedingState()
    }
}