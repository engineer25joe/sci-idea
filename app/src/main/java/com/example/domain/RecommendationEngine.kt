package com.example.domain

import com.example.model.*
import kotlin.math.abs

data class UserProfileQuery(
    val exactCapitalKes: Int? = null,
    val capitalTier: CapitalTier? = null,
    val skillIds: Set<String> = emptySet(),
    val interestAreas: Set<InterestArea> = emptySet(),
    val availableTime: TimeCommitment? = null,
    val environment: EnvironmentType? = null,
    val availableEquipmentIds: Set<String> = emptySet(),
    val riskTolerance: RiskLevel? = null,
    val preferredBusinessModel: BusinessModelType? = null,
    val isDetailedMode: Boolean = false
)

object RecommendationEngine {

    // Configurable weights totaling 100%
    const val WEIGHT_CAPITAL = 25
    const val WEIGHT_SKILLS = 20
    const val WEIGHT_EQUIPMENT = 15
    const val WEIGHT_INTEREST = 15
    const val WEIGHT_TIME = 10
    const val WEIGHT_LOCATION = 8
    const val WEIGHT_RISK = 4
    const val WEIGHT_MODEL = 3

    fun evaluate(idea: BusinessIdea, query: UserProfileQuery, allSkills: List<Skill>): BusinessIdea {
        val factorScores = mutableMapOf<String, Int>()

        // 1. Capital Compatibility (25%)
        val effectiveCapital = query.exactCapitalKes ?: query.capitalTier?.maxKes
        val capitalScore: Int
        val capitalDiff: Int
        val capitalStatus: String

        if (effectiveCapital != null) {
            capitalDiff = effectiveCapital - idea.capitalMinKes
            capitalScore = when {
                effectiveCapital >= idea.capitalMinKes -> WEIGHT_CAPITAL
                effectiveCapital >= (idea.capitalMinKes * 0.75).toInt() -> (WEIGHT_CAPITAL * 0.7).toInt()
                effectiveCapital >= (idea.capitalMinKes * 0.50).toInt() -> (WEIGHT_CAPITAL * 0.4).toInt()
                else -> (WEIGHT_CAPITAL * 0.15).toInt()
            }
            capitalStatus = if (capitalDiff >= 0) {
                "Capital surplus of KSh ${"%,d".format(capitalDiff)}. You have enough to start right away!"
            } else {
                "Capital gap of KSh ${"%,d".format(abs(capitalDiff))}. Can be bridged via Hustler Fund (*254#) or a Chama."
            }
        } else {
            capitalDiff = 0
            capitalScore = (WEIGHT_CAPITAL * 0.7).toInt() // neutral default
            capitalStatus = "Requires KSh ${"%,d".format(idea.capitalMinKes)} - ${"%,d".format(idea.capitalMaxKes)} initial startup capital."
        }
        factorScores["Capital"] = capitalScore

        // 2. Skills Compatibility (20%)
        val matchedSkillsList = idea.matchedSkills.filter { it in query.skillIds }
        val missingSkillsList = idea.matchedSkills.filter { it !in query.skillIds }
        val skillNamesMatched = matchedSkillsList.map { id ->
            allSkills.find { it.id == id }?.name ?: id.replace("_", " ").capitalize()
        }
        val skillNamesMissing = missingSkillsList.map { id ->
            allSkills.find { it.id == id }?.name ?: id.replace("_", " ").capitalize()
        }

        val skillRatio = if (idea.matchedSkills.isNotEmpty()) {
            matchedSkillsList.size.toFloat() / idea.matchedSkills.size
        } else 0.5f

        val skillScore = (skillRatio * WEIGHT_SKILLS).toInt().coerceIn(0, WEIGHT_SKILLS)
        factorScores["Skills"] = skillScore

        // 3. Equipment Compatibility (15%)
        val matchedEquipment = idea.requiredEquipmentIds.filter { it in query.availableEquipmentIds }
        val missingEquipment = idea.requiredEquipmentIds.filter { it !in query.availableEquipmentIds }
        val equipmentScore = if (idea.requiredEquipmentIds.isEmpty()) {
            WEIGHT_EQUIPMENT
        } else {
            val ratio = matchedEquipment.size.toFloat() / idea.requiredEquipmentIds.size
            (ratio * WEIGHT_EQUIPMENT).toInt().coerceIn(0, WEIGHT_EQUIPMENT)
        }
        factorScores["Equipment"] = equipmentScore
        val equipmentStatus = if (missingEquipment.isEmpty()) {
            "You have the primary tools needed to start."
        } else {
            "Missing primary equipment: ${missingEquipment.joinToString(", ")}."
        }

        // 4. Interest Compatibility (15%)
        val interestScore = if (query.interestAreas.isEmpty() || idea.interestArea in query.interestAreas) {
            WEIGHT_INTEREST
        } else {
            (WEIGHT_INTEREST * 0.3).toInt()
        }
        factorScores["Interests"] = interestScore
        val interestText = if (idea.interestArea in query.interestAreas) {
            "Directly matches your interest in ${idea.interestArea.displayName}."
        } else {
            "Related to ${idea.interestArea.displayName}."
        }

        // 5. Time Compatibility (10%)
        val timeScore = if (query.availableTime == null || idea.timeCommitment == query.availableTime || query.availableTime == TimeCommitment.FLEXIBLE) {
            WEIGHT_TIME
        } else {
            (WEIGHT_TIME * 0.5).toInt()
        }
        factorScores["Time"] = timeScore
        val timeText = "Requires ${idea.timeCommitment.label}."

        // 6. Location Compatibility (8%)
        val locScore = if (query.environment == null || idea.idealLocations.contains(query.environment)) {
            WEIGHT_LOCATION
        } else {
            (WEIGHT_LOCATION * 0.35).toInt()
        }
        factorScores["Location"] = locScore

        // 7. Risk Compatibility (4%)
        val riskScore = if (query.riskTolerance == null || idea.riskLevel == query.riskTolerance) {
            WEIGHT_RISK
        } else {
            (WEIGHT_RISK * 0.5).toInt()
        }
        factorScores["Risk"] = riskScore

        // 8. Business Model (3%)
        val modelScore = if (query.preferredBusinessModel == null || idea.businessModel == query.preferredBusinessModel || idea.businessModel == BusinessModelType.HYBRID) {
            WEIGHT_MODEL
        } else {
            (WEIGHT_MODEL * 0.4).toInt()
        }
        factorScores["BusinessModel"] = modelScore

        // Total score calculation (clamped between 15% and 98%)
        val rawTotal = factorScores.values.sum()
        val finalScore = rawTotal.coerceIn(18, 98)

        // Feasibility Group Determination:
        // Ready Now requires: enough capital (or deficit <= 15%) AND at least 1 matched skill (or no required skills)
        val hasSevereCapitalGap = effectiveCapital != null && effectiveCapital < (idea.capitalMinKes * 0.65)
        val hasSevereSkillGap = idea.matchedSkills.isNotEmpty() && matchedSkillsList.isEmpty()

        val feasibilityGroup = when {
            hasSevereCapitalGap || (effectiveCapital != null && effectiveCapital < idea.capitalMinKes * 0.4) -> {
                FeasibilityGroup.FUTURE_OPPORTUNITIES
            }
            !hasSevereCapitalGap && matchedSkillsList.isNotEmpty() && (effectiveCapital == null || effectiveCapital >= idea.capitalMinKes * 0.85) -> {
                FeasibilityGroup.READY_NOW
            }
            else -> {
                FeasibilityGroup.PREPARATION_NEEDED
            }
        }

        val recommendedNextStep = when (feasibilityGroup) {
            FeasibilityGroup.READY_NOW -> "Open the 5-step roadmap, test your kachumbari/recipe or prototype, and set up Pochi La Biashara."
            FeasibilityGroup.PREPARATION_NEEDED -> "Apply for Hustler Fund or team up with a Chama partner to bridge the KSh ${"%,d".format(abs(capitalDiff))} capital gap."
            FeasibilityGroup.FUTURE_OPPORTUNITIES -> "Save towards the initial inventory and take a free YouTube/TVET masterclass on ${skillNamesMissing.firstOrNull() ?: "the required skills"}."
        }

        val explanation = MatchExplanation(
            matchedSkillNames = skillNamesMatched,
            missingSkillNames = skillNamesMissing,
            capitalDifferenceKes = capitalDiff,
            capitalStatusText = capitalStatus,
            equipmentMatchText = equipmentStatus,
            missingEquipment = missingEquipment,
            interestAlignmentText = interestText,
            timeMatchText = timeText,
            recommendedNextStep = recommendedNextStep,
            feasibilityGroup = feasibilityGroup,
            matchScore = finalScore,
            factorBreakdown = factorScores
        )

        return idea.copy(
            matchScore = finalScore,
            feasibilityGroup = feasibilityGroup,
            explanation = explanation
        )
    }

    private fun String.capitalize(): String {
        return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}
