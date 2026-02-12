package com.aideesigns.backend.cms.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank


@Schema(description = "Payload for creating or updating a content page")
data class ContentPageRequest(

    @field:NotBlank
    @Schema(example = "home")
    val slug: String,

    @field:NotBlank
    @Schema(example = "Home Page")
    val title: String
)
