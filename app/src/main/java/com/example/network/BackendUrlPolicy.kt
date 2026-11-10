package com.example.network

import com.example.BuildConfig
import java.net.URI
import java.util.Locale

object BackendUrlPolicy {

    const val PRODUCTION_HOST = "sci-idea-backend.onrender.com"

    fun validate(baseUrl: String): String {
        val uri = runCatching {
            URI(baseUrl.trim())
        }.getOrElse {
            throw IllegalArgumentException("Invalid backend URL")
        }

        val scheme = uri.scheme?.lowercase(Locale.ROOT)

        val host = uri.host?.lowercase(Locale.ROOT)
            ?: throw IllegalArgumentException(
                "Backend URL must include an approved host"
            )

        require(scheme == "https") {
            "Backend URL must use HTTPS"
        }

        require(host == PRODUCTION_HOST) {
            "Backend host is not approved"
        }

        require(uri.userInfo == null &&
                uri.query == null &&
                uri.fragment == null) {
            "Backend URL cannot include credentials, query parameters, or fragments"
        }

        require(uri.port == -1 || uri.port == 443) {
            "Backend port is not approved"
        }

        require(uri.path.isNullOrEmpty() || uri.path == "/") {
            "Backend URL must use the approved root endpoint"
        }

        return uri.toString().trimEnd('/') + "/"
    }
}

object BackendConfig {

    val apiBaseUrl: String by lazy {
        BackendUrlPolicy.validate(BuildConfig.API_BASE_URL)
    }
}