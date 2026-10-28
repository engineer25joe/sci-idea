package com.example.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

@JsonClass(generateAdapter = true)
data class RemoteIdeaDto(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "category") val category: String,
    @Json(name = "summary") val summary: String,
    @Json(name = "capital_min_kes") val capitalMinKes: Int,
    @Json(name = "capital_max_kes") val capitalMaxKes: Int,
    @Json(name = "capital_tier") val capitalTier: String,
    @Json(name = "estimated_monthly_profit_min") val estimatedMonthlyProfitMin: Int,
    @Json(name = "estimated_monthly_profit_max") val estimatedMonthlyProfitMax: Int,
    @Json(name = "rating") val rating: Double = 4.5,
    @Json(name = "review_count") val reviewCount: Int = 120,
    @Json(name = "startup_requirements") val startupRequirements: List<String> = emptyList(),
    @Json(name = "potential_customers") val potentialCustomers: List<String> = emptyList(),
    @Json(name = "marketing_strategies") val marketingStrategies: List<String> = emptyList(),
    @Json(name = "next_steps") val nextSteps: List<String> = emptyList()
)

@JsonClass(generateAdapter = true)
data class RemoteTestimonialDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "business_name") val businessName: String,
    @Json(name = "location") val location: String,
    @Json(name = "quote") val quote: String,
    @Json(name = "starting_capital_kes") val startingCapitalKes: Int,
    @Json(name = "current_monthly_revenue_kes") val currentMonthlyRevenueKes: Int,
    @Json(name = "advice") val advice: String
)

@JsonClass(generateAdapter = true)
data class RemoteEventDto(
    @Json(name = "event_id") val eventId: String,
    @Json(name = "event_type") val eventType: String,
    @Json(name = "target_id") val targetId: String?,
    @Json(name = "target_title") val targetTitle: String?,
    @Json(name = "category") val category: String?,
    @Json(name = "timestamp_ms") val timestampMs: Long,
    @Json(name = "metadata") val metadata: Map<String, String> = emptyMap()
)

@JsonClass(generateAdapter = true)
data class EventBatchRequest(
    @Json(name = "device_id") val deviceId: String,
    @Json(name = "events") val events: List<RemoteEventDto>
)

@JsonClass(generateAdapter = true)
data class EventBatchResponse(
    @Json(name = "status") val status: String,
    @Json(name = "synced_count") val syncedCount: Int
)

interface BiasharaApiService {

    @GET("/api/v1/ideas/")
    suspend fun getBusinessIdeas(): Response<List<RemoteIdeaDto>>

    @GET("/api/v1/testimonials/")
    suspend fun getTestimonials(): Response<List<RemoteTestimonialDto>>

    @POST("/api/v1/tracking/events/")
    suspend fun postTrackingEvents(@Body request: EventBatchRequest): Response<EventBatchResponse>
}
