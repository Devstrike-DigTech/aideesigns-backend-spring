package com.aideesigns.backend.testimonial.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.util.*


@Schema(description = "Testimonial response")
data class TestimonialResponse(
    val id: UUID,
    val customerName: String,
    val message: String,
    val rating: Int?,
    val isApproved: Boolean,
    val createdAt: Instant
)