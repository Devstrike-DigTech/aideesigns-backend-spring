package com.aideesigns.backend.testimonial.repository

import com.aideesigns.backend.testimonial.entity.Testimonial
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TestimonialRepository : JpaRepository<Testimonial, UUID> {
    fun findAllByIsApprovedTrueOrderByCreatedAtDesc(): List<Testimonial>
    fun findAllByOrderByCreatedAtDesc(): List<Testimonial>
}
