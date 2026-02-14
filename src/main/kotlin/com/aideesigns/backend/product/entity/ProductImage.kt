package com.aideesigns.backend.product.entity

import com.aideesigns.backend.shared.base.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "product_images")
class ProductImage(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product,

    @Column(nullable = false)
    val imageUrl: String,

    @Column(nullable = false)
    var isPrimary: Boolean = false  // var — can be demoted when a new primary is set

) : BaseEntity()
