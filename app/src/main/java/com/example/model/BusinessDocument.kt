package com.example.model

enum class DocumentCategory(val displayName: String) {
    STARTUP_GUIDES("Startup Guides"),
    TEMPLATES("Business Templates"),
    FINANCIAL_SHEETS("Budget & Record Worksheets"),
    MARKETING("Marketing Resources"),
    LEGAL_COMPLIANCE("Legal & Permits Guides")
}

enum class DownloadStatus {
    NOT_DOWNLOADED,
    DOWNLOADING,
    DOWNLOADED,
    UPDATE_AVAILABLE,
    FAILED
}

data class BusinessDocument(
    val id: String,
    val title: String,
    val description: String,
    val category: DocumentCategory,
    val fileType: String = "PDF", // "PDF", "DOCX", "XLSX"
    val sizeBytes: Long,
    val language: String = "English / Swahili",
    val version: String = "1.0",
    val isPremium: Boolean = false,
    val publicationStatus: String = "PUBLISHED",
    val lastUpdatedDate: String = "October 2026",
    val remoteR2ObjectKey: String, // e.g. "guides/2026/biashara_startup_guide_ke.pdf" (stored in Cloudflare R2)
    val downloadStatus: DownloadStatus = DownloadStatus.NOT_DOWNLOADED,
    val localFilePath: String? = null
) {
    fun formattedSize(): String {
        val kb = sizeBytes / 1024
        return if (kb > 1024) {
            "%.1f MB".format(kb / 1024.0)
        } else {
            "$kb KB"
        }
    }
}
