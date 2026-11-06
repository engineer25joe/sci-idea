package com.example.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

@JsonClass(generateAdapter = true)
data class PersonalizedResearchRequest(
    @Json(name = "budget_kes") val budgetKes: Long,
    val skills: List<String>,
    val interests: List<String>,
    val county: String,
    @Json(name = "available_equipment") val availableEquipment: List<String>,
    @Json(name = "available_time") val availableTime: String,
    @Json(name = "preferred_business_category") val preferredBusinessCategory: String,
    @Json(name = "device_id") val deviceId: String? = null,
)

interface ResearchApiService {
    @POST("api/v1/research/personalised/")
    suspend fun generatePersonalized(
        @Header("Authorization") authorization: String,
        @Body request: PersonalizedResearchRequest,
    ): Response<Map<String, Any?>>

    @GET("api/v1/research/jobs/{jobId}/")
    suspend fun getJobStatus(
        @Header("Authorization") authorization: String,
        @Path("jobId") jobId: String,
    ): Response<Map<String, Any?>>

    @GET("api/v1/research/jobs/{jobId}/report/")
    suspend fun getReport(
        @Header("Authorization") authorization: String,
        @Path("jobId") jobId: String,
    ): Response<Map<String, Any?>>
}
