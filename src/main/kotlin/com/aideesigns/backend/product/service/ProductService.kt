package com.aideesigns.backend.product.service

import com.aideesigns.backend.product.dto.*
import com.aideesigns.backend.product.entity.Product
import com.aideesigns.backend.product.entity.ProductImage
import com.aideesigns.backend.product.entity.ProductSize
import com.aideesigns.backend.product.repository.ProductImageRepository
import com.aideesigns.backend.product.repository.ProductRepository
import com.aideesigns.backend.product.repository.ProductSizeRepository
import com.aideesigns.backend.shared.exception.ResourceNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val productSizeRepository: ProductSizeRepository,
    private val productImageRepository: ProductImageRepository
) {

    // ─── Public ────────────────────────────────────────────────────────────────

    fun getAllAvailableProducts(): List<ProductResponse> =
        productRepository.findAllByIsDeletedFalseAndIsAvailableTrue()
            .map { it.toResponse() }

    fun getProductById(id: UUID): ProductResponse =
        findActiveProductOrThrow(id).toResponse()

    // ─── Admin ─────────────────────────────────────────────────────────────────

    fun getAllProducts(): List<ProductResponse> =
        productRepository.findAllByIsDeletedFalse()
            .map { it.toResponse() }

    @Transactional
    fun createProduct(request: ProductRequest): ProductResponse {
        val product = Product(
            name = request.name,
            description = request.description,
            price = request.price,
            isAvailable = request.isAvailable
        )
        return productRepository.save(product).toResponse()
    }

    @Transactional
    fun updateProduct(id: UUID, request: ProductRequest): ProductResponse {
        val product = findActiveProductOrThrow(id)

        product.name = request.name
        product.description = request.description
        product.price = request.price
        product.isAvailable = request.isAvailable

        return productRepository.save(product).toResponse()
    }

    @Transactional
    fun deleteProduct(id: UUID) {
        val product = findActiveProductOrThrow(id)
        product.isDeleted = true
        productRepository.save(product)
    }

    // ─── Sizes ─────────────────────────────────────────────────────────────────

    @Transactional
    fun addSize(productId: UUID, request: ProductSizeRequest): ProductSizeResponse {
        val product = findActiveProductOrThrow(productId)

        val size = ProductSize(
            product = product,
            sizeLabel = request.sizeLabel,
            stockQuantity = request.stockQuantity
        )
        return productSizeRepository.save(size).toSizeResponse()
    }

    @Transactional
    fun updateSize(productId: UUID, sizeId: UUID, request: ProductSizeRequest): ProductSizeResponse {
        findActiveProductOrThrow(productId)

        val size = productSizeRepository.findById(sizeId)
            .orElseThrow { ResourceNotFoundException("Size not found with id: $sizeId") }

        size.sizeLabel = request.sizeLabel
        size.stockQuantity = request.stockQuantity

        return productSizeRepository.save(size).toSizeResponse()
    }

    @Transactional
    fun deleteSize(productId: UUID, sizeId: UUID) {
        findActiveProductOrThrow(productId)
        productSizeRepository.deleteById(sizeId)
    }

    // ─── Images ────────────────────────────────────────────────────────────────

    @Transactional
    fun addImage(productId: UUID, request: ProductImageRequest): ProductImageResponse {
        val product = findActiveProductOrThrow(productId)

        // If new image is primary, demote existing primary
        if (request.isPrimary) {
            productImageRepository.findByProductIdAndIsPrimaryTrue(productId)
                ?.let { existing ->
                    existing.isPrimary = false
                    productImageRepository.save(existing)
                }
        }

        val image = ProductImage(
            product = product,
            imageUrl = request.imageUrl,
            isPrimary = request.isPrimary
        )
        return productImageRepository.save(image).toImageResponse()
    }

    @Transactional
    fun deleteImage(productId: UUID, imageId: UUID) {
        findActiveProductOrThrow(productId)
        productImageRepository.deleteById(imageId)
    }

    // ─── Helpers ───────────────────────────────────────────────────────────────

    private fun findActiveProductOrThrow(id: UUID): Product =
        productRepository.findByIdAndIsDeletedFalse(id)
            ?: throw ResourceNotFoundException("Product not found with id: $id")

    private fun Product.toResponse() = ProductResponse(
        id = id,
        name = name,
        description = description,
        price = price,
        isAvailable = isAvailable,
        sizes = productSizeRepository.findAllByProductId(id).map { it.toSizeResponse() },
        images = productImageRepository.findAllByProductId(id).map { it.toImageResponse() }
    )

    private fun ProductSize.toSizeResponse() = ProductSizeResponse(
        id = id,
        sizeLabel = sizeLabel,
        stockQuantity = stockQuantity
    )

    private fun ProductImage.toImageResponse() = ProductImageResponse(
        id = id,
        imageUrl = imageUrl,
        isPrimary = isPrimary
    )
}
