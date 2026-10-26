package com.example.model

enum class ProfileVerificationStatus(val label: String, val badgeColorHex: Long) {
    VERIFIED_ENTREPRENEUR("Verified Entrepreneur Profile", 0xFF16A34A),
    ILLUSTRATIVE_CASE_STUDY("Illustrative Case Study (KNBS & YEDF Field Data)", 0xFF0284C7),
    AWAITING_VERIFICATION("Pending Community Verification", 0xFFCA8A04)
}

data class EntrepreneurProfile(
    val id: String,
    val name: String,
    val age: Int,
    val county: String,
    val ventureName: String,
    val category: String,
    val startedWithKes: Int,
    val currentMonthlyRevenueKes: String,
    val journeyStory: String,
    val keyChallenges: List<String>,
    val keyAdvice: String,
    val primarySkillsUsed: List<String>,
    val avatarInitials: String,
    val mentorAvailable: Boolean = true,
    val mentorshipFocus: String,
    val verificationStatus: ProfileVerificationStatus = ProfileVerificationStatus.ILLUSTRATIVE_CASE_STUDY,
    val citationSource: String = "Kenya National Bureau of Statistics (KNBS) MSME Survey & YEDF Field Reports",
    val lastVerifiedDate: String = "October 2026",
    val mentorshipDisclaimer: String = "Requests are routed to vetted volunteer advisors and peer mentors from the YEDF & SCI BASE COLLECTIONS network."
)

data class MentorshipRequest(
    val id: String,
    val mentorName: String,
    val applicantName: String,
    val phoneOrEmail: String,
    val selectedSkillOrVenture: String,
    val questionOrGoal: String,
    val preferredMode: String, // "WhatsApp Call", "Google Meet", "In-person Coffee"
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Pending Review",
    val verifiedRoute: Boolean = true
)
