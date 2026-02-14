package com.aideesigns.backend.product.repository

import com.aideesigns.backend.product.entity.Product
import com.aideesigns.backend.product.entity.ProductImage
import com.aideesigns.backend.product.entity.ProductSize
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ProductRepository : JpaRepository<Product, UUID> {
    fun findAllByIsDeletedFalse(): List<Product>
    fun findAllByIsDeletedFalseAndIsAvailableTrue(): List<Product>
    fun findByIdAndIsDeletedFalse(id: UUID): Product?
}

interface ProductSizeRepository : JpaRepository<ProductSize, UUID> {
    fun findAllByProductId(productId: UUID): List<ProductSize>
    fun deleteAllByProductId(productId: UUID)
}

interface ProductImageRepository : JpaRepository<ProductImage, UUID> {
    fun findAllByProductId(productId: UUID): List<ProductImage>
    fun findByProductIdAndIsPrimaryTrue(productId: UUID): ProductImage?
}
