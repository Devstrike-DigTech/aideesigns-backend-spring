package com.aideesigns.backend.product.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import java.math.BigDecimal

@Schema(description = "Payload for creating or updating a product")
data class ProductRequest(

    @field:NotBlank
    @Schema(example = "Luxury Ankara Dress")
    val name: String,

    @Schema(example = "Premium handmade Ankara dress perfect for weddings.")
    val description: String? = null,

    @field:Positive
    @Schema(example = "75000.00")
    val price: BigDecimal,

    val isAvailable: Boolean = true
)

@Schema(description = "Payload for adding a size to a product")
data class ProductSizeRequest(

    @field:NotBlank
    @Schema(example = "XL")
    val sizeLabel: String,

    @field:PositiveOrZero
    @Schema(example = "10")
    val stockQuantity: Int
)

@Schema(description = "Payload for adding an image to a product")
data class ProductImageRequest(

    @field:NotBlank
    @Schema(example = "https://res.cloudinary.com/aideesigns/image/upload/v1/dress.jpg")
    val imageUrl: String,

    @Schema(example = "true")
    val isPrimary: Boolean = false
)
