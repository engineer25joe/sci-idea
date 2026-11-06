package com.example.network

import java.net.URI
import java.util.Locale

object BackendUrlPolicy {
    const val PRODUCTION_HOST = "sci-idea-backend.onrender.com"
    private val developmentHosts = setOf("10.0.2.2", "localhost", "127.0.0.1", "::1", "[::1]")

    fun validate(baseUrl: String, isDebug: Boolean): String {
        val uri = runCatching { URI(baseUrl.trim()) }
            .getOrElse { throw IllegalArgumentException("Invalid backend URL") }
        val scheme = uri.scheme?.lowercase(Locale.ROOT)
        val host = uri.host?.lowercase(Locale.ROOT)
            ?: throw IllegalArgumentException("Backend URL must include an approved host")
        require(uri.userInfo == null && uri.query == null && uri.fragment == null) {
            "Backend URL cannot include credentials, query parameters, or fragments"
        }
        if (isDebug) {
            require(host in developmentHosts) { "Development builds can use local backend hosts only" }
            require(scheme == "http" || scheme == "https") { "Backend URL must use HTTP or HTTPS" }
        } else {
            require(scheme == "https") { "Production backend URL must use HTTPS" }
            require(host == PRODUCTION_HOST) { "Backend host is not approved" }
            require(uri.port == -1 || uri.port == 443) { "Production backend port is not approved" }
            require(uri.path.isNullOrEmpty() || uri.path == "/") {
                "Production backend URL must use the approved root endpoint"
            }
        }
        return uri.toString().trimEnd('/') + "/"
    }
}

object BackendConfig {
    val apiBaseUrl: String by lazy {
        BackendUrlPolicy.validate(BuildConfig.API_BASE_URL, BuildConfig.DEBUG)
    }
}
