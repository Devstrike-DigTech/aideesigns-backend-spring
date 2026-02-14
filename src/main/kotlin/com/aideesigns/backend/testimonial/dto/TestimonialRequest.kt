package com.aideesigns.backend.testimonial.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank


@Schema(description = "Payload for submitting a testimonial")
data class TestimonialRequest(

    @field:NotBlank
    @Schema(example = "Amara Okonkwo")
    val customerName: String,

    @field:NotBlank
    @Schema(example = "Absolutely loved my outfit! The stitching was perfect.")
    val message: String,

    @field:Min(1) @field:Max(5)
    @Schema(example = "5", description = "Rating between 1 and 5")
    val rating: Int? = null
)
