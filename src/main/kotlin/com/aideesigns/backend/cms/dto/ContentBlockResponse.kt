package com.aideesigns.backend.cms.dto

import java.time.Instant
import java.util.*

data class ContentBlockResponse(
    val id: UUID,
    val blockKey: String,
    val blockType: String,
    val content: String?,
    val imageUrl: String?,
    val updatedAt: Instant
)
