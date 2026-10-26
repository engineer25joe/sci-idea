package com.example.model

enum class TrendEvidenceType(val label: String) {
    MEASURED_DATA("Measured Sector Data"),
    INDUSTRY_PROJECTION("Industry Projection"),
    QUALITATIVE_DEMAND("Qualitative Field Demand")
}

data class MarketSectorTrend(
    val id: String,
    val sectorName: String,
    val growthTag: String,
    val summary: String,
    val whyPromisingForYouth: String,
    val potentialCustomerBase: List<String>,
    val averageStartupCapitalRange: String,
    val hotNiches: List<String>,
    val keyEnablersInKenya: List<String>,
    val evidenceType: TrendEvidenceType = TrendEvidenceType.QUALITATIVE_DEMAND,
    val sourceCitation: String = "Kenya National Bureau of Statistics (KNBS) MSME Basic Report & Communications Authority (CA) Sector Statistics",
    val publicationDate: String = "2024/2025",
    val geographicScope: String = "National (Focus on Nairobi, Nakuru, Kisumu, Eldoret & Peri-Urban estates)",
    val periodCovered: String = "2023 - 2025"
)

data class YouthFundGuide(
    val name: String,
    val provider: String,
    val maxAmountKes: String,
    val interestRate: String,
    val targetGroup: String,
    val eligibilityChecklist: List<String>,
    val applicationProcess: String,
    val ussdOrPortal: String,
    val officialSourceUrl: String = "",
    val lastVerifiedDate: String = "October 2026",
    val approvalDisclaimer: String = "Approval is strictly determined by the issuing government agency or financial institution. Requirements vary by applicant circumstances."
)
