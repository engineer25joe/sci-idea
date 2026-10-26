package com.example.model

data class ComplianceRequirement(
    val title: String,
    val authority: String,
    val estimatedCostKes: String,
    val description: String,
    val isMandatory: Boolean = false
)

data class MatchExplanation(
    val matchedSkillNames: List<String> = emptyList(),
    val missingSkillNames: List<String> = emptyList(),
    val capitalDifferenceKes: Int = 0, // > 0 surplus, < 0 deficit
    val capitalStatusText: String = "",
    val equipmentMatchText: String = "",
    val missingEquipment: List<String> = emptyList(),
    val interestAlignmentText: String = "",
    val timeMatchText: String = "",
    val recommendedNextStep: String = "",
    val feasibilityGroup: FeasibilityGroup = FeasibilityGroup.PREPARATION_NEEDED,
    val matchScore: Int = 0,
    val factorBreakdown: Map<String, Int> = emptyMap()
)

data class BusinessIdea(
    val id: String,
    val title: String,
    val shengTitle: String,
    val category: SkillCategory,
    val interestArea: InterestArea,
    val summary: String,
    val problemBeingSolved: String = "",
    val proposedSolution: String = "",
    val targetCustomers: List<String> = emptyList(),
    val matchedSkills: List<String>,
    val capitalMinKes: Int,
    val capitalMaxKes: Int,
    val capitalTier: CapitalTier,
    val capitalBreakdown: Map<String, Int>,
    val estimatedDailyRevenueKes: IntRange,
    val estimatedMonthlyProfitKes: IntRange,
    val paybackPeriod: String,
    val idealLocations: List<EnvironmentType>,
    val businessModel: BusinessModelType = BusinessModelType.HYBRID,
    val riskLevel: RiskLevel = RiskLevel.MEDIUM,
    val timeCommitment: TimeCommitment = TimeCommitment.FULL_TIME,
    val requiredEquipmentIds: List<String> = emptyList(),
    val equipmentNeeded: List<String>,
    val supplierAndSourcingGuidance: String = "",
    val pricingConsiderations: String = "",
    val marketingStrategy: String = "",
    val competitorResearchGuidance: String = "",
    val launchSteps: List<String>,
    val compliance: List<ComplianceRequirement>,
    val mpesaTip: String,
    val insiderTips: List<String>,
    val risksAndMitigations: List<Pair<String, String>>,
    val lastReviewedDate: String = "October 2026",
    val isSaved: Boolean = false,
    val matchScore: Int = 0,
    val feasibilityGroup: FeasibilityGroup = FeasibilityGroup.READY_NOW,
    val explanation: MatchExplanation? = null
)
