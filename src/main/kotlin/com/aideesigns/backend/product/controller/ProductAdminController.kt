package com.aideesigns.backend.product.controller

import com.aideesigns.backend.product.dto.*
import com.aideesigns.backend.product.service.ProductService
import com.aideesigns.backend.shared.dto.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name = "Admin — Products", description = "Admin endpoints for managing products")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/admin/products")
class ProductAdminController(private val productService: ProductService) {

    @GetMapping
    @Operation(summary = "List all products including unavailable ones")
    fun getAll(): ApiResponse<List<ProductResponse>> =
        ApiResponse.success(productService.getAllProducts())

    @GetMapping("/{id}")
    @Operation(summary = "Get a single product by ID")
    fun getById(@PathVariable id: UUID): ApiResponse<ProductResponse> =
        ApiResponse.success(productService.getProductById(id))

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new product")
    fun create(@Valid @RequestBody request: ProductRequest): ApiResponse<ProductResponse> =
        ApiResponse.success(productService.createProduct(request), "Product created successfully")

    @PutMapping("/{id}")
    @Operation(summary = "Update a product")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody request: ProductRequest
    ): ApiResponse<ProductResponse> =
        ApiResponse.success(productService.updateProduct(id, request), "Product updated successfully")

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete a product")
    fun delete(@PathVariable id: UUID): ApiResponse<Nothing> {
        productService.deleteProduct(id)
        return ApiResponse.success("Product deleted successfully")
    }

    // ─── Sizes ─────────────────────────────────────────────────────────────────

    @PostMapping("/{productId}/sizes")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a size to a product")
    fun addSize(
        @PathVariable productId: UUID,
        @Valid @RequestBody request: ProductSizeRequest
    ): ApiResponse<ProductSizeResponse> =
        ApiResponse.success(productService.addSize(productId, request), "Size added successfully")

    @PutMapping("/{productId}/sizes/{sizeId}")
    @Operation(summary = "Update a product size")
    fun updateSize(
        @PathVariable productId: UUID,
        @PathVariable sizeId: UUID,
        @Valid @RequestBody request: ProductSizeRequest
    ): ApiResponse<ProductSizeResponse> =
        ApiResponse.success(productService.updateSize(productId, sizeId, request), "Size updated successfully")

    @DeleteMapping("/{productId}/sizes/{sizeId}")
    @Operation(summary = "Remove a size from a product")
    fun deleteSize(
        @PathVariable productId: UUID,
        @PathVariable sizeId: UUID
    ): ApiResponse<Nothing> {
        productService.deleteSize(productId, sizeId)
        return ApiResponse.success("Size removed successfully")
    }

    // ─── Images ────────────────────────────────────────────────────────────────

    @PostMapping("/{productId}/images")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add an image to a product")
    fun addImage(
        @PathVariable productId: UUID,
        @Valid @RequestBody request: ProductImageRequest
    ): ApiResponse<ProductImageResponse> =
        ApiResponse.success(productService.addImage(productId, request), "Image added successfully")

    @DeleteMapping("/{productId}/images/{imageId}")
    @Operation(summary = "Remove an image from a product")
    fun deleteImage(
        @PathVariable productId: UUID,
        @PathVariable imageId: UUID
    ): ApiResponse<Nothing> {
        productService.deleteImage(productId, imageId)
        return ApiResponse.success("Image removed successfully")
    }
}
