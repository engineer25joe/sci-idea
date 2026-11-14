package com.example.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

@JsonClass(generateAdapter = true)
data class SkillDto(
    val id: String,
    val name: String,
    val category: String,
    val description: String = "",
    @Json(name = "image_url") val imageUrl: String = "",
)

@JsonClass(generateAdapter = true)
data class SkillCategoryDto(
    val code: String,
    val name: String,
    val description: String = "",
    @Json(name = "icon_name") val iconName: String = "",
    @Json(name = "image_url") val imageUrl: String = "",
    val skills: List<SkillDto> = emptyList(),
)

@JsonClass(generateAdapter = true)
data class UserSkillSelectionDto(
    @Json(name = "skill_ids")
    val skillIds: List<String> = emptyList(),

    @Json(name = "custom_skills")
    val customSkills: List<String> = emptyList(),

    @Json(name = "updated_at")
    val updatedAt: String? = null,
)

@JsonClass(generateAdapter = true)
data class SaveUserSkillSelectionRequest(
    @Json(name = "skill_ids")
    val skillIds: List<String>,

    @Json(name = "custom_skills")
    val customSkills: List<String>,
)

interface SkillsApiService {

    @GET("api/v1/skills/")
    suspend fun getSkills(
        @Header("Authorization") authorization: String,
    ): Response<List<SkillDto>>

    @GET("api/v1/skills/categories/")
    suspend fun getCategories(
        @Header("Authorization") authorization: String,
    ): Response<List<SkillCategoryDto>>

    @GET("api/v1/skills/my-selection/")
    suspend fun getMySelection(
        @Header("Authorization") authorization: String,
    ): Response<UserSkillSelectionDto>

    @PUT("api/v1/skills/my-selection/")
    suspend fun saveMySelection(
        @Header("Authorization") authorization: String,
        @Body request: SaveUserSkillSelectionRequest,
    ): Response<UserSkillSelectionDto>
}