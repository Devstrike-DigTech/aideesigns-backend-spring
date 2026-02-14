package com.aideesigns.backend.testimonial.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "testimonials")
class Testimonial(

    @Column(nullable = false)
    val customerName: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val message: String,

    @Column
    val rating: Int? = null,

    @Column(nullable = false)
    var isApproved: Boolean = false

) {
    @Id
    val id: UUID = UUID.randomUUID()

    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()
}
