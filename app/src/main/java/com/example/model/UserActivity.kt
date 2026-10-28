package com.example.model

data class UserActivityEvent(
    val id: String,
    val eventType: String, // e.g. "VIEW_IDEA", "SEARCH", "FILTER_CATEGORY", "SAVE_IDEA", "USE_CALCULATOR", "COMPLETED_ONBOARDING"
    val targetId: String? = null,
    val targetTitle: String? = null,
    val category: String? = null,
    val metadata: Map<String, String> = emptyMap(),
    val timestampMs: Long = System.currentTimeMillis(),
    val isSyncedToDjango: Boolean = false
)

enum class ActivityType(val code: String, val label: String) {
    VIEW_IDEA("VIEW_IDEA", "Viewed Idea"),
    SEARCH("SEARCH", "Performed Search"),
    FILTER_CATEGORY("FILTER_CATEGORY", "Filtered Category"),
    SAVE_IDEA("SAVE_IDEA", "Saved Idea"),
    UNSAVE_IDEA("UNSAVE_IDEA", "Removed from Saved"),
    USE_CALCULATOR("USE_CALCULATOR", "Used Profit Calculator"),
    START_JOURNEY("START_JOURNEY", "Started Launch Journey"),
    COMPLETE_ONBOARDING("COMPLETE_ONBOARDING", "Completed Onboarding"),
    REQUEST_MENTORSHIP("REQUEST_MENTORSHIP", "Requested Mentorship"),
    VIEW_TRENDS("VIEW_TRENDS", "Viewed Market Trends")
}

data class TrackingSyncPayload(
    val deviceId: String,
    val userEmail: String,
    val events: List<UserActivityEvent>
)
