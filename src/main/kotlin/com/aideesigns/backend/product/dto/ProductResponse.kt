package com.aideesigns.backend.product.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.util.*

@Schema(description = "Product response returned to clients")
data class ProductResponse(

    @Schema(example = "b12a6c6c-4f4c-4f2a-9d22-8f5c1b5f1eaa")
    val id: UUID,

    @Schema(example = "Luxury Ankara Dress")
    val name: String,

    val description: String?,

    @Schema(example = "75000.00")
    val price: BigDecimal,

    val isAvailable: Boolean,

    val sizes: List<ProductSizeResponse>,

    val images: List<ProductImageResponse>
)

data class ProductSizeResponse(
    val id: UUID,
    val sizeLabel: String,
    val stockQuantity: Int
)

data class ProductImageResponse(
    val id: UUID,
    val imageUrl: String,
    val isPrimary: Boolean
)
