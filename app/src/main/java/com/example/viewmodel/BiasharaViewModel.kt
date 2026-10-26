package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.HustleRepository
import com.example.domain.UserProfileQuery
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

enum class DiscoveryMode {
    QUICK,
    DETAILED,
    EXPLORE
}

data class BiasharaUiState(
    val allSkills: List<Skill> = emptyList(),
    val selectedSkillIds: Set<String> = setOf("graphic_design", "social_media"),
    val customSkills: List<String> = emptyList(),
    val selectedInterests: Set<InterestArea> = emptySet(),
    val exactCapitalKes: Int? = null,
    val selectedCapitalTier: CapitalTier? = null,
    val selectedEnvironment: EnvironmentType? = null,
    val availableTime: TimeCommitment? = null,
    val selectedEquipmentIds: Set<String> = emptySet(),
    val riskTolerance: RiskLevel? = null,
    val preferredBusinessModel: BusinessModelType? = null,
    val discoveryMode: DiscoveryMode = DiscoveryMode.QUICK,

    // Match Results
    val matchedIdeas: List<BusinessIdea> = emptyList(),
    val readyNowIdeas: List<BusinessIdea> = emptyList(),
    val prepNeededIdeas: List<BusinessIdea> = emptyList(),
    val futureIdeas: List<BusinessIdea> = emptyList(),
    val allIdeas: List<BusinessIdea> = emptyList(),

    // Filtering & Search
    val searchQuery: String = "",
    val categoryFilter: SkillCategory? = null,
    val savedIdeaIds: Set<String> = emptySet(),
    val comparedIdeaIds: Set<String> = emptySet(),
    val selectedDetailIdea: BusinessIdea? = null,

    // Journey & Plans
    val defaultJourneyTasks: List<JourneyTask> = emptyList(),
    val activeJourneyIdea: BusinessIdea? = null,
    val activeJourneyProgress: HustleJourneyProgress? = null,
    val activeBusinessPlanDraft: SciBusinessPlanDraft? = null,

    // Stories & Trends
    val successStories: List<EntrepreneurProfile> = emptyList(),
    val marketSectorTrends: List<MarketSectorTrend> = emptyList(),
    val youthFundGuides: List<YouthFundGuide> = emptyList(),
    val documentLibrary: List<BusinessDocument> = emptyList(),
    val mentorshipRequests: List<MentorshipRequest> = emptyList(),
    val mentorshipTargetProfile: EntrepreneurProfile? = null,
    val isMentorshipSubmittedSuccess: Boolean = false,

    // Dialogs
    val showAboutDialog: Boolean = false,
    val showCompareSheet: Boolean = false,
    val showPlanDraftSheet: Boolean = false,
    val showJourneySheet: Boolean = false,

    // Calculator State (with startup item list and low/mod/high scenarios)
    val calcDailyUnits: Int = 30,
    val calcUnitPriceKes: Int = 120,
    val calcUnitCostKes: Int = 60,
    val calcDailyOverheadKes: Int = 400,
    val calcInitialCapitalKes: Int = 15000,
    val calcScenarioMultiplier: Float = 1.0f, // 0.7 = Low, 1.0 = Moderate, 1.4 = High
    val userNotification: String? = null
)

class BiasharaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = HustleRepository(application.applicationContext)

    private val _uiState = MutableStateFlow(BiasharaUiState())
    val uiState: StateFlow<BiasharaUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val saved = repository.getSavedIdeaIds()
        val compared = repository.getComparedIdeaIds()
        val mentorshipReqs = repository.getMentorshipRequests()
        val skills = repository.defaultSkills
        val stories = repository.successStories
        val trends = repository.marketSectorTrends
        val funds = repository.youthFundGuides
        val ideas = repository.businessIdeas
        val docs = repository.getDocumentLibrary()
        val tasks = repository.defaultJourneyTasks

        _uiState.update { current ->
            current.copy(
                allSkills = skills,
                allIdeas = ideas,
                savedIdeaIds = saved,
                comparedIdeaIds = compared,
                successStories = stories,
                marketSectorTrends = trends,
                youthFundGuides = funds,
                documentLibrary = docs,
                defaultJourneyTasks = tasks,
                mentorshipRequests = mentorshipReqs
            )
        }
        recalculateMatches()
    }

    fun setDiscoveryMode(mode: DiscoveryMode) {
        _uiState.update { it.copy(discoveryMode = mode) }
        recalculateMatches()
    }

    fun toggleSkill(skillId: String) {
        _uiState.update { current ->
            val set = current.selectedSkillIds.toMutableSet()
            if (set.contains(skillId)) {
                set.remove(skillId)
            } else {
                set.add(skillId)
            }
            current.copy(selectedSkillIds = set)
        }
        recalculateMatches()
    }

    fun addCustomSkill(skillName: String) {
        val trimmed = skillName.trim()
        if (trimmed.isEmpty()) return
        val customId = "custom_${trimmed.lowercase().replace(" ", "_")}"
        _uiState.update { current ->
            if (current.customSkills.contains(trimmed)) return@update current
            val newCustom = current.customSkills + trimmed
            val newSelected = current.selectedSkillIds + customId
            val newSkills = current.allSkills + Skill(customId, trimmed, SkillCategory.SERVICES_LOGISTICS, "User custom skill")
            current.copy(
                customSkills = newCustom,
                selectedSkillIds = newSelected,
                allSkills = newSkills,
                userNotification = "Added skill '$trimmed'"
            )
        }
        recalculateMatches()
    }

    fun removeCustomSkill(skillName: String) {
        val customId = "custom_${skillName.lowercase().replace(" ", "_")}"
        _uiState.update { current ->
            val newCustom = current.customSkills.filter { it != skillName }
            val newSelected = current.selectedSkillIds.filter { it != customId }.toSet()
            current.copy(customSkills = newCustom, selectedSkillIds = newSelected)
        }
        recalculateMatches()
    }

    fun toggleInterest(interest: InterestArea) {
        _uiState.update { current ->
            val set = current.selectedInterests.toMutableSet()
            if (set.contains(interest)) {
                set.remove(interest)
            } else {
                set.add(interest)
            }
            current.copy(selectedInterests = set)
        }
        recalculateMatches()
    }

    fun setExactCapital(capital: Int?) {
        _uiState.update { it.copy(exactCapitalKes = capital) }
        recalculateMatches()
    }

    fun setCapitalTier(tier: CapitalTier?) {
        _uiState.update { it.copy(selectedCapitalTier = tier, exactCapitalKes = tier?.maxKes) }
        recalculateMatches()
    }

    fun setEnvironment(env: EnvironmentType?) {
        _uiState.update { it.copy(selectedEnvironment = env) }
        recalculateMatches()
    }

    fun setTimeCommitment(time: TimeCommitment?) {
        _uiState.update { it.copy(availableTime = time) }
        recalculateMatches()
    }

    fun toggleEquipment(equipId: String) {
        _uiState.update { current ->
            val set = current.selectedEquipmentIds.toMutableSet()
            if (set.contains(equipId)) set.remove(equipId) else set.add(equipId)
            current.copy(selectedEquipmentIds = set)
        }
        recalculateMatches()
    }

    fun setRiskTolerance(risk: RiskLevel?) {
        _uiState.update { it.copy(riskTolerance = risk) }
        recalculateMatches()
    }

    fun setPreferredBusinessModel(model: BusinessModelType?) {
        _uiState.update { it.copy(preferredBusinessModel = model) }
        recalculateMatches()
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setCategoryFilter(category: SkillCategory?) {
        _uiState.update { it.copy(categoryFilter = category) }
    }

    fun selectDetailIdea(idea: BusinessIdea?) {
        _uiState.update { it.copy(selectedDetailIdea = idea) }
    }

    fun toggleSaveIdea(ideaId: String) {
        viewModelScope.launch {
            val isNowSaved = repository.toggleSaveIdea(ideaId)
            val newSaved = repository.getSavedIdeaIds()
            _uiState.update { current ->
                current.copy(
                    savedIdeaIds = newSaved,
                    userNotification = if (isNowSaved) "Added to your Launchpad!" else "Removed from Launchpad"
                )
            }
        }
    }

    fun toggleCompareIdea(ideaId: String) {
        val isNowIn = repository.toggleCompareIdea(ideaId)
        val newCompared = repository.getComparedIdeaIds()
        _uiState.update { current ->
            current.copy(
                comparedIdeaIds = newCompared,
                userNotification = if (isNowIn) "Added to Compare list (${newCompared.size}/3)" else "Removed from Compare"
            )
        }
    }

    fun openCompareSheet() {
        _uiState.update { it.copy(showCompareSheet = true) }
    }

    fun closeCompareSheet() {
        _uiState.update { it.copy(showCompareSheet = false) }
    }

    fun openAboutDialog() {
        _uiState.update { it.copy(showAboutDialog = true) }
    }

    fun closeAboutDialog() {
        _uiState.update { it.copy(showAboutDialog = false) }
    }

    // Business Journey & Progress
    fun openJourneyForIdea(idea: BusinessIdea) {
        val progress = repository.getJourneyProgress(idea.id)
        _uiState.update {
            it.copy(
                activeJourneyIdea = idea,
                activeJourneyProgress = progress,
                showJourneySheet = true
            )
        }
    }

    fun closeJourneySheet() {
        _uiState.update { it.copy(showJourneySheet = false, activeJourneyIdea = null) }
    }

    fun toggleJourneyTask(taskId: String) {
        val currentProgress = _uiState.value.activeJourneyProgress ?: return
        val updatedSet = currentProgress.completedTaskIds.toMutableSet()
        if (updatedSet.contains(taskId)) {
            updatedSet.remove(taskId)
        } else {
            updatedSet.add(taskId)
        }
        val updated = currentProgress.copy(completedTaskIds = updatedSet, lastUpdated = System.currentTimeMillis())
        repository.saveJourneyProgress(updated)
        _uiState.update { it.copy(activeJourneyProgress = updated) }
    }

    fun updateJourneyNotes(notes: String) {
        val currentProgress = _uiState.value.activeJourneyProgress ?: return
        val updated = currentProgress.copy(notes = notes, lastUpdated = System.currentTimeMillis())
        repository.saveJourneyProgress(updated)
        _uiState.update { it.copy(activeJourneyProgress = updated) }
    }

    // SCI Business Plan Draft
    fun openBusinessPlanDraft(idea: BusinessIdea) {
        val draft = repository.getBusinessPlanDraft(idea.id)
        _uiState.update {
            it.copy(
                activeBusinessPlanDraft = draft,
                showPlanDraftSheet = true
            )
        }
    }

    fun closeBusinessPlanDraft() {
        _uiState.update { it.copy(showPlanDraftSheet = false, activeBusinessPlanDraft = null) }
    }

    fun updateBusinessPlanDraft(draft: SciBusinessPlanDraft) {
        repository.saveBusinessPlanDraft(draft)
        _uiState.update {
            it.copy(
                activeBusinessPlanDraft = draft,
                userNotification = "Draft saved locally for SCI BUSINESS PLAN!"
            )
        }
    }

    // Document Library
    fun toggleDocumentDownload(docId: String) {
        val isDownloaded = repository.toggleDocumentDownload(docId)
        val refreshedDocs = repository.getDocumentLibrary()
        _uiState.update {
            it.copy(
                documentLibrary = refreshedDocs,
                userNotification = if (isDownloaded) "Document stored locally (Offline ready)" else "Document removed from offline storage"
            )
        }
    }

    // Mentorship
    fun openMentorshipDialog(mentor: EntrepreneurProfile) {
        _uiState.update { it.copy(mentorshipTargetProfile = mentor, isMentorshipSubmittedSuccess = false) }
    }

    fun closeMentorshipDialog() {
        _uiState.update { it.copy(mentorshipTargetProfile = null, isMentorshipSubmittedSuccess = false) }
    }

    fun submitMentorshipRequest(
        applicantName: String,
        phoneOrEmail: String,
        selectedSkillOrVenture: String,
        questionOrGoal: String,
        preferredMode: String
    ) {
        val mentor = _uiState.value.mentorshipTargetProfile ?: return
        val req = MentorshipRequest(
            id = UUID.randomUUID().toString(),
            mentorName = mentor.name,
            applicantName = applicantName,
            phoneOrEmail = phoneOrEmail,
            selectedSkillOrVenture = selectedSkillOrVenture,
            questionOrGoal = questionOrGoal,
            preferredMode = preferredMode
        )
        repository.saveMentorshipRequest(req)
        val allRequests = repository.getMentorshipRequests()
        _uiState.update {
            it.copy(
                mentorshipRequests = allRequests,
                isMentorshipSubmittedSuccess = true,
                userNotification = "Mentorship request registered for ${mentor.name}!"
            )
        }
    }

    // Calculator Updates
    fun updateCalculator(
        units: Int? = null,
        unitPrice: Int? = null,
        unitCost: Int? = null,
        overhead: Int? = null,
        capital: Int? = null,
        multiplier: Float? = null
    ) {
        _uiState.update { current ->
            current.copy(
                calcDailyUnits = units ?: current.calcDailyUnits,
                calcUnitPriceKes = unitPrice ?: current.calcUnitPriceKes,
                calcUnitCostKes = unitCost ?: current.calcUnitCostKes,
                calcDailyOverheadKes = overhead ?: current.calcDailyOverheadKes,
                calcInitialCapitalKes = capital ?: current.calcInitialCapitalKes,
                calcScenarioMultiplier = multiplier ?: current.calcScenarioMultiplier
            )
        }
    }

    fun loadIdeaIntoCalculator(idea: BusinessIdea) {
        val avgPrice = 150
        val avgUnits = 30
        val avgOverhead = 500
        val avgCost = 70
        _uiState.update {
            it.copy(
                calcDailyUnits = avgUnits,
                calcUnitPriceKes = avgPrice,
                calcUnitCostKes = avgCost,
                calcDailyOverheadKes = avgOverhead,
                calcInitialCapitalKes = idea.capitalMinKes,
                calcScenarioMultiplier = 1.0f,
                userNotification = "Loaded '${idea.title}' into Profit Calculator"
            )
        }
    }

    fun clearNotification() {
        _uiState.update { it.copy(userNotification = null) }
    }

    fun clearAllUserData() {
        repository.clearUserData()
        loadInitialData()
        _uiState.update { it.copy(userNotification = "All local user data cleared.") }
    }

    private fun recalculateMatches() {
        val current = _uiState.value
        val query = UserProfileQuery(
            exactCapitalKes = current.exactCapitalKes,
            capitalTier = current.selectedCapitalTier,
            skillIds = current.selectedSkillIds,
            interestAreas = current.selectedInterests,
            availableTime = current.availableTime,
            environment = current.selectedEnvironment,
            availableEquipmentIds = current.selectedEquipmentIds,
            riskTolerance = current.riskTolerance,
            preferredBusinessModel = current.preferredBusinessModel,
            isDetailedMode = current.discoveryMode == DiscoveryMode.DETAILED
        )

        val matched = repository.matchBusinessIdeas(query)

        val readyNow = matched.filter { it.feasibilityGroup == FeasibilityGroup.READY_NOW }
        val prepNeeded = matched.filter { it.feasibilityGroup == FeasibilityGroup.PREPARATION_NEEDED }
        val future = matched.filter { it.feasibilityGroup == FeasibilityGroup.FUTURE_OPPORTUNITIES }

        _uiState.update {
            it.copy(
                matchedIdeas = matched,
                readyNowIdeas = readyNow,
                prepNeededIdeas = prepNeeded,
                futureIdeas = future
            )
        }
    }
}
