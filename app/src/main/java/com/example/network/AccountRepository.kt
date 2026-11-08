package com.example.network

import com.example.network.dto.*

import android.content.Context
import org.json.JSONObject
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient
import java.io.IOException
import java.util.concurrent.TimeUnit
import com.example.security.SecureTokenStore
import com.squareup.moshi.Moshi

class AccountApiException(
    val httpStatus: Int?,
    override val message: String,
) : Exception(message)

class AccountRepository(
    context: Context,
    private val api: AccountApiService = createApi(),
    private val researchApi: ResearchApiService = createResearchApi(),
    private val tokenStore: SecureTokenStore = SecureTokenStore(context.applicationContext),
) {
    @Volatile
    private var accessToken: String? = null
    private val refreshMutex = Mutex()

    suspend fun register(
        email: String,
        password: String,
        passwordConfirm: String,
        firstName: String,
        lastName: String,
    ) {
        val response = safeCall {
            api.register(
                RegisterAccountRequest(
                    email = email.trim(),
                    password = password,
                    passwordConfirm = passwordConfirm,
                    firstName = firstName.trim(),
                    lastName = lastName.trim(),
                ),
            )
        }
        response.requireSuccess()
    }

    suspend fun verifyEmail(code: String) {
        safeCall { api.verifyEmail(CodeRequest(code.trim())) }.requireSuccess()
    }

    suspend fun resendVerification(email: String) {
        safeCall { api.resendVerification(EmailRequest(email.trim())) }.requireSuccess()
    }

    suspend fun login(email: String, password: String): AccountProfileDto {
        val response = safeCall { api.login(LoginRequest(email.trim(), password)) }
        val body = response.requireBody()
        try {
            tokenStore.saveRefreshToken(body.refresh)
        } catch (_: Exception) {
            accessToken = null
            tokenStore.clear()
            throw AccountApiException(null, "Could not securely store the sign-in session.")
        }
        accessToken = body.access
        return body.profile
    }

    suspend fun requestPasswordReset(email: String) {
        safeCall { api.requestPasswordReset(PasswordResetRequest(email.trim())) }.requireSuccess()
    }

    suspend fun confirmPasswordReset(code: String, password: String, passwordConfirm: String) {
        safeCall {
            api.confirmPasswordReset(
                PasswordResetConfirmRequest(code.trim(), password, passwordConfirm),
            )
        }.requireSuccess()
    }

    suspend fun restoreSession(): AccountProfileDto? {
        if (tokenStore.readRefreshToken() == null) return null
        if (refreshAccessToken() == null) return null
        return try {
            authorized { api.getProfile(it) }
        } catch (_: AccountApiException) {
            clearLocalSession()
            null
        }
    }

    suspend fun updateProfile(firstName: String, lastName: String): AccountProfileDto =
        authorized {
            api.updateProfile(
                it,
                ProfileUpdateRequest(firstName.trim(), lastName.trim()),
            )
        }

    suspend fun generatePersonalizedResearch(
        request: PersonalizedResearchRequest,
    ): Map<String, Any?> = authorized {
        researchApi.generatePersonalized(it, request)
    }

    suspend fun getResearchJobStatus(jobId: String): Map<String, Any?> = authorized {
        researchApi.getJobStatus(it, jobId)
    }

    suspend fun getResearchReport(jobId: String): Map<String, Any?> = authorized {
        researchApi.getReport(it, jobId)
    }

    suspend fun logout() {
        try {
            var access = accessToken ?: refreshAccessToken()
            var refresh = tokenStore.readRefreshToken()
            if (access != null && refresh != null) {
                val initialAccess = access
                val initialRefresh = refresh
                var response = safeCall {
                    api.logout("Bearer $initialAccess", LogoutRequest(initialRefresh))
                }
                if (response.code() == 401) {
                    accessToken = null
                    access = refreshAccessToken()
                    refresh = tokenStore.readRefreshToken()
                    if (access != null && refresh != null) {
                        val rotatedAccess = access
                        val rotatedRefresh = refresh
                        response = safeCall {
                            api.logout("Bearer $rotatedAccess", LogoutRequest(rotatedRefresh))
                        }
                    }
                }
                response.requireSuccess()
            }
        } catch (_: Exception) {
            // Local logout always clears credentials, even when the device is offline.
        } finally {
            clearLocalSession()
        }
    }

    fun clearLocalSession() {
        accessToken = null
        tokenStore.clear()
    }

    private suspend fun <T : Any> authorized(
        call: suspend (String) -> Response<T>,
    ): T {
        var token = accessToken ?: refreshAccessToken()
            ?: throw AccountApiException(401, "Sign in again to use your account.")
        var response = safeCall { call("Bearer $token") }
        if (response.code() == 401) {
            accessToken = null
            token = refreshAccessToken()
                ?: throw AccountApiException(401, "Your session expired. Sign in again.")
            response = safeCall { call("Bearer $token") }
        }
        return response.requireBody()
    }

    private suspend fun refreshAccessToken(): String? = refreshMutex.withLock {
        accessToken?.let { return@withLock it }
        val currentRefresh = tokenStore.readRefreshToken() ?: return@withLock null
        val response = try {
            api.refresh(RefreshRequest(currentRefresh))
        } catch (_: IOException) {
            return@withLock null
        } catch (_: Exception) {
            return@withLock null
        }
        if (!response.isSuccessful) {
            if (response.code() in 400..499) clearLocalSession()
            return@withLock null
        }
        val tokens = response.body()
        if (tokens == null) {
            clearLocalSession()
            return@withLock null
        }
        try {
            tokenStore.saveRefreshToken(tokens.refresh)
        } catch (_: Exception) {
            clearLocalSession()
            return@withLock null
        }
        accessToken = tokens.access
        tokens.access
    }

    private suspend fun <T> safeCall(call: suspend () -> Response<T>): Response<T> =
        try {
            call()
        } catch (_: IOException) {
            throw AccountApiException(null, "Network unavailable. Your offline data remains available.")
        } catch (_: AccountApiException) {
            throw AccountApiException(null, "The account request could not be completed.")
        }

    private fun <T> Response<T>.requireSuccess() {
        if (isSuccessful) return
        throw errorFor(this)
    }

    private fun <T : Any> Response<T>.requireBody(): T {
        if (!isSuccessful) throw errorFor(this)
        return body() ?: throw AccountApiException(code(), "The server returned an empty response.")
    }

    private fun <T> errorFor(response: Response<T>): AccountApiException {
        val errorJson = runCatching {
            JSONObject(response.errorBody()?.string().orEmpty())
        }.getOrNull()
        val message = when (response.code()) {
            401 -> "Email, password, or session is invalid. Sign in again if your session expired."
            403 -> if (errorJson?.optString("code") == "EMAIL_UNVERIFIED") {
                "Verify your email before signing in."
            } else {
                "This account cannot access the requested service."
            }
            413 -> "The submitted information is too large. Shorten it and try again."
            429 -> "Too many requests. Wait a little before trying again."
            in 300..399 -> "The backend redirected this request, so it was stopped."
            in 500..599 -> "The account service is temporarily unavailable. Try again later."
            else -> errorJson?.optString("error")?.takeIf { it.isNotBlank() }
                ?: errorJson?.optString("detail")?.takeIf { it.isNotBlank() }
                ?: "Check the submitted information and try again."
        }
        return AccountApiException(response.code(), message)
    }

    companion object {
        private fun createApi(): AccountApiService {
            val retrofit = createRetrofit()
            return retrofit.create(AccountApiService::class.java)
        }

        private fun createResearchApi(): ResearchApiService {
            val retrofit = createRetrofit()
            return retrofit.create(ResearchApiService::class.java)
        }

        private fun createRetrofit(): Retrofit {
            val httpClient = OkHttpClient.Builder()
                .followRedirects(false)
                .followSslRedirects(false)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()
            return Retrofit.Builder()
                .baseUrl(BackendConfig.apiBaseUrl)
                .client(httpClient)
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
                .build()
        }
    }
}
