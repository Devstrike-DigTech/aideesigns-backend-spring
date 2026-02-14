package com.aideesigns.backend.product.controller

import com.aideesigns.backend.product.dto.ProductResponse
import com.aideesigns.backend.product.service.ProductService
import com.aideesigns.backend.shared.dto.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name = "Public — Products", description = "Public endpoints for browsing products")
@RestController
@RequestMapping("/api/products")
class ProductPublicController(private val productService: ProductService) {

    @GetMapping
    @Operation(summary = "Browse all available products")
    fun getAll(): ApiResponse<List<ProductResponse>> =
        ApiResponse.success(productService.getAllAvailableProducts())

    @GetMapping("/{id}")
    @Operation(summary = "Get a single product by ID")
    fun getById(@PathVariable id: UUID): ApiResponse<ProductResponse> =
        ApiResponse.success(productService.getProductById(id))
}
