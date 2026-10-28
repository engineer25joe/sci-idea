package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.ActivityType
import com.example.model.UserActivityEvent
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class UserActivityTracker(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("biashara_user_tracking_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_EVENTS = "tracking_events_json"
        private const val KEY_DEVICE_ID = "tracking_device_id"
        private const val KEY_DJANGO_URL = "django_backend_url"
        private const val DEFAULT_DJANGO_URL = "http://10.0.2.2:8000"
    }

    init {
        if (!prefs.contains(KEY_DEVICE_ID)) {
            prefs.edit().putString(KEY_DEVICE_ID, "dev_" + UUID.randomUUID().toString().take(8)).apply()
        }
    }

    val deviceId: String
        get() = prefs.getString(KEY_DEVICE_ID, "dev_kenya_user") ?: "dev_kenya_user"

    var djangoBackendUrl: String
        get() = prefs.getString(KEY_DJANGO_URL, DEFAULT_DJANGO_URL) ?: DEFAULT_DJANGO_URL
        set(value) {
            prefs.edit().putString(KEY_DJANGO_URL, value.trim().removeSuffix("/")).apply()
        }

    fun logEvent(
        type: ActivityType,
        targetId: String? = null,
        targetTitle: String? = null,
        category: String? = null,
        metadata: Map<String, String> = emptyMap()
    ): UserActivityEvent {
        val event = UserActivityEvent(
            id = UUID.randomUUID().toString(),
            eventType = type.code,
            targetId = targetId,
            targetTitle = targetTitle,
            category = category,
            metadata = metadata,
            timestampMs = System.currentTimeMillis(),
            isSyncedToDjango = false
        )

        val currentEvents = getAllEvents().toMutableList()
        // Prepend new event so latest appears first
        currentEvents.add(0, event)
        // Keep last 150 events to avoid unbounded storage growth
        val trimmed = if (currentEvents.size > 150) currentEvents.take(150) else currentEvents
        saveEvents(trimmed)
        return event
    }

    fun getAllEvents(): List<UserActivityEvent> {
        val raw = prefs.getString(KEY_EVENTS, null) ?: return emptyList()
        return try {
            val array = JSONArray(raw)
            val list = mutableListOf<UserActivityEvent>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val metaObj = obj.optJSONObject("metadata")
                val metaMap = mutableMapOf<String, String>()
                if (metaObj != null) {
                    val keys = metaObj.keys()
                    while (keys.hasNext()) {
                        val k = keys.next()
                        metaMap[k] = metaObj.getString(k)
                    }
                }
                list.add(
                    UserActivityEvent(
                        id = obj.getString("id"),
                        eventType = obj.getString("eventType"),
                        targetId = if (obj.has("targetId") && !obj.isNull("targetId")) obj.getString("targetId") else null,
                        targetTitle = if (obj.has("targetTitle") && !obj.isNull("targetTitle")) obj.getString("targetTitle") else null,
                        category = if (obj.has("category") && !obj.isNull("category")) obj.getString("category") else null,
                        metadata = metaMap,
                        timestampMs = obj.optLong("timestampMs", System.currentTimeMillis()),
                        isSyncedToDjango = obj.optBoolean("isSyncedToDjango", false)
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun markEventsAsSynced(eventIds: Set<String>) {
        val currentEvents = getAllEvents().map { event ->
            if (eventIds.contains(event.id)) {
                event.copy(isSyncedToDjango = true)
            } else {
                event
            }
        }
        saveEvents(currentEvents)
    }

    fun clearAllEvents() {
        prefs.edit().remove(KEY_EVENTS).apply()
    }

    private fun saveEvents(events: List<UserActivityEvent>) {
        val array = JSONArray()
        for (e in events) {
            val obj = JSONObject()
            obj.put("id", e.id)
            obj.put("eventType", e.eventType)
            obj.put("targetId", e.targetId)
            obj.put("targetTitle", e.targetTitle)
            obj.put("category", e.category)
            obj.put("timestampMs", e.timestampMs)
            obj.put("isSyncedToDjango", e.isSyncedToDjango)
            val metaObj = JSONObject()
            e.metadata.forEach { (k, v) -> metaObj.put(k, v) }
            obj.put("metadata", metaObj)
            array.put(obj)
        }
        prefs.edit().putString(KEY_EVENTS, array.toString()).apply()
    }
}
