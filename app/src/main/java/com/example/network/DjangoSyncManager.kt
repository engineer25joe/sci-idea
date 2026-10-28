package com.example.network

import android.content.Context
import android.util.Log
import com.example.data.UserActivityTracker
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class DjangoSyncManager(
    private val context: Context,
    private val activityTracker: UserActivityTracker
) {
    private val tag = "DjangoSyncManager"

    private fun buildRetrofit(baseUrl: String): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val cleanUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"

        return Retrofit.Builder()
            .baseUrl(cleanUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    suspend fun testConnection(): Pair<Boolean, String> = withContext(Dispatchers.IO) {
        val url = activityTracker.djangoBackendUrl
        try {
            val service = buildRetrofit(url).create(BiasharaApiService::class.java)
            val resp = service.getBusinessIdeas()
            if (resp.isSuccessful) {
                Pair(true, "Connected to Django successfully! (${resp.body()?.size ?: 0} ideas available)")
            } else {
                Pair(false, "Server responded with HTTP ${resp.code()}")
            }
        } catch (e: Exception) {
            Pair(false, "Cannot reach Django backend at $url (${e.localizedMessage ?: "Connection refused"}). Offline mode active.")
        }
    }

    suspend fun syncTrackingEvents(): Pair<Int, String> = withContext(Dispatchers.IO) {
        val unsynced = activityTracker.getAllEvents().filter { !it.isSyncedToDjango }
        if (unsynced.isEmpty()) {
            return@withContext Pair(0, "No pending tracking events to sync.")
        }

        val dtos = unsynced.map {
            RemoteEventDto(
                eventId = it.id,
                eventType = it.eventType,
                targetId = it.targetId,
                targetTitle = it.targetTitle,
                category = it.category,
                timestampMs = it.timestampMs,
                metadata = it.metadata
            )
        }

        val request = EventBatchRequest(
            deviceId = activityTracker.deviceId,
            events = dtos
        )

        try {
            val service = buildRetrofit(activityTracker.djangoBackendUrl).create(BiasharaApiService::class.java)
            val resp = service.postTrackingEvents(request)
            if (resp.isSuccessful && resp.body()?.status == "success") {
                val syncedIds = unsynced.map { it.id }.toSet()
                activityTracker.markEventsAsSynced(syncedIds)
                Pair(syncedIds.size, "Successfully uploaded ${syncedIds.size} events to Django analytics.")
            } else {
                Pair(0, "Sync deferred: HTTP ${resp.code()}")
            }
        } catch (e: Exception) {
            Log.w(tag, "Django tracking sync deferred: ${e.message}")
            Pair(0, "Offline: Events queued locally on device.")
        }
    }

    suspend fun fetchRemoteIdeas(): List<RemoteIdeaDto> = withContext(Dispatchers.IO) {
        try {
            val service = buildRetrofit(activityTracker.djangoBackendUrl).create(BiasharaApiService::class.java)
            val resp = service.getBusinessIdeas()
            if (resp.isSuccessful) {
                resp.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
