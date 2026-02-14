package com.aideesigns.backend.product.entity

import com.aideesigns.backend.shared.base.BaseEntity
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "products")
class Product(

    @Column(nullable = false)
    var name: String,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(nullable = false, precision = 12, scale = 2)  // matches NUMERIC(12,2)
    var price: BigDecimal,

    @Column(nullable = false)
    var isAvailable: Boolean = true,

    @Column(nullable = false)
    var isDeleted: Boolean = false

) : BaseEntity() {

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true)
    val sizes: MutableList<ProductSize> = mutableListOf()

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], orphanRemoval = true)
    val images: MutableList<ProductImage> = mutableListOf()
}
