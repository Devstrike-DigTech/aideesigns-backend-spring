package com.aideesigns.backend.product.entity

import com.aideesigns.backend.shared.base.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "product_sizes")
class ProductSize(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product,

    @Column(nullable = false)
    var sizeLabel: String,  // var — admin can update size label

    @Column(nullable = false)
    var stockQuantity: Int

) : BaseEntity()
