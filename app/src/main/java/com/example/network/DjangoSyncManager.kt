package com.example.network

import android.content.Context
import com.example.data.UserActivityTracker
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class DjangoSyncManager(
    private val context: Context,
    private val activityTracker: UserActivityTracker
) {
    private fun buildRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .followRedirects(false)
            .followSslRedirects(false)
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(BackendConfig.apiBaseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    suspend fun testConnection(): Pair<Boolean, String> = withContext(Dispatchers.IO) {
        try {
            val service = buildRetrofit().create(BiasharaApiService::class.java)
            val resp = service.getBusinessIdeas()
            if (resp.isSuccessful) {
                Pair(true, "Connected to Django successfully! (${resp.body()?.size ?: 0} ideas available)")
            } else {
                Pair(false, "Server responded with HTTP ${resp.code()}")
            }
        } catch (e: Exception) {
            Pair(false, "Cannot reach the configured backend. Offline mode remains available.")
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
            val service = buildRetrofit().create(BiasharaApiService::class.java)
            val resp = service.postTrackingEvents(request)
            if (resp.isSuccessful && resp.body()?.status == "success") {
                val syncedIds = unsynced.map { it.id }.toSet()
                activityTracker.markEventsAsSynced(syncedIds)
                Pair(syncedIds.size, "Successfully uploaded ${syncedIds.size} events to Django analytics.")
            } else {
                Pair(0, "Sync deferred: HTTP ${resp.code()}")
            }
        } catch (e: Exception) {
            Pair(0, "Offline: Events queued locally on device.")
        }
    }

    suspend fun fetchRemoteIdeas(): List<RemoteIdeaDto> = withContext(Dispatchers.IO) {
        try {
            val service = buildRetrofit().create(BiasharaApiService::class.java)
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
