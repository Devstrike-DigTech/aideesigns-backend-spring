package com.aideesigns.backend.cms.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank


@Schema(description = "Payload for upserting a content block")
data class ContentBlockRequest(

    @field:NotBlank
    @Schema(example = "hero_heading")
    val blockKey: String,

    @field:NotBlank
    @Schema(example = "text", description = "Block type: text, image, html")
    val blockType: String,

    @Schema(example = "Custom Outfits Made Just for You")
    val content: String? = null,

    @Schema(example = "https://res.cloudinary.com/aideesigns/hero.jpg")
    val imageUrl: String? = null
)
