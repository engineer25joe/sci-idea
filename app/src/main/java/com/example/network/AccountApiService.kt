package com.example.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

@JsonClass(generateAdapter = true)
data class RegisterAccountRequest(
    val email: String,
    val password: String,
    @Json(name = "password_confirm") val passwordConfirm: String,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String,
)

@JsonClass(generateAdapter = true)
data class EmailRequest(val email: String)

@JsonClass(generateAdapter = true)
data class CodeRequest(val token: String)

@JsonClass(generateAdapter = true)
data class LoginRequest(val email: String, val password: String)

@JsonClass(generateAdapter = true)
data class PasswordResetRequest(val email: String)

@JsonClass(generateAdapter = true)
data class PasswordResetConfirmRequest(
    val token: String,
    val password: String,
    @Json(name = "password_confirm") val passwordConfirm: String,
)

@JsonClass(generateAdapter = true)
data class RefreshRequest(val refresh: String)

@JsonClass(generateAdapter = true)
data class LogoutRequest(val refresh: String)

@JsonClass(generateAdapter = true)
data class ProfileUpdateRequest(
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String,
)

@JsonClass(generateAdapter = true)
data class AccountProfileDto(
    val email: String,
    @Json(name = "email_verified") val emailVerified: Boolean,
    @Json(name = "first_name") val firstName: String = "",
    @Json(name = "last_name") val lastName: String = "",
    @Json(name = "created_at") val createdAt: String? = null,
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    val access: String,
    val refresh: String,
    val profile: AccountProfileDto,
)

@JsonClass(generateAdapter = true)
data class RefreshResponse(val access: String, val refresh: String)

@JsonClass(generateAdapter = true)
data class AccountMessage(
    val detail: String? = null,
    val error: String? = null,
    val code: String? = null,
)

interface AccountApiService {
    @POST("api/v1/auth/register/")
    suspend fun register(@Body request: RegisterAccountRequest): Response<AccountMessage>

    @POST("api/v1/auth/verify-email/")
    suspend fun verifyEmail(@Body request: CodeRequest): Response<AccountMessage>

    @POST("api/v1/auth/verify-email/resend/")
    suspend fun resendVerification(@Body request: EmailRequest): Response<AccountMessage>

    @POST("api/v1/auth/login/")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/v1/auth/token/refresh/")
    suspend fun refresh(@Body request: RefreshRequest): Response<RefreshResponse>

    @POST("api/v1/auth/logout/")
    suspend fun logout(
        @Header("Authorization") authorization: String,
        @Body request: LogoutRequest,
    ): Response<Unit>

    @POST("api/v1/auth/password-reset/")
    suspend fun requestPasswordReset(@Body request: PasswordResetRequest): Response<AccountMessage>

    @POST("api/v1/auth/password-reset/confirm/")
    suspend fun confirmPasswordReset(@Body request: PasswordResetConfirmRequest): Response<AccountMessage>

    @GET("api/v1/auth/profile/")
    suspend fun getProfile(@Header("Authorization") authorization: String): Response<AccountProfileDto>

    @PATCH("api/v1/auth/profile/")
    suspend fun updateProfile(
        @Header("Authorization") authorization: String,
        @Body request: ProfileUpdateRequest,
    ): Response<AccountProfileDto>
}
