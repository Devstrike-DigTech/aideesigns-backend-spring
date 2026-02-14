package com.aideesigns.backend.cms.dto

import java.time.Instant
import java.util.*


data class ContentPageResponse(
    val id: UUID,
    val slug: String,
    val title: String,
    val blocks: List<ContentBlockResponse>,
    val updatedAt: Instant
)
