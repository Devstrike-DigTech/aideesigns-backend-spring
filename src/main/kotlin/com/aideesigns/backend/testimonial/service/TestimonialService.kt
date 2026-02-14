package com.aideesigns.backend.testimonial.service

import com.aideesigns.backend.shared.exception.ResourceNotFoundException
import com.aideesigns.backend.testimonial.dto.TestimonialRequest
import com.aideesigns.backend.testimonial.dto.TestimonialResponse
import com.aideesigns.backend.testimonial.entity.Testimonial
import com.aideesigns.backend.testimonial.repository.TestimonialRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TestimonialService(
    private val testimonialRepository: TestimonialRepository
) {

    // ─── Public ────────────────────────────────────────────────────────────────

    fun getApprovedTestimonials(): List<TestimonialResponse> =
        testimonialRepository.findAllByIsApprovedTrueOrderByCreatedAtDesc()
            .map { it.toResponse() }

    @Transactional
    fun submitTestimonial(request: TestimonialRequest): TestimonialResponse {
        val testimonial = Testimonial(
            customerName = request.customerName,
            message = request.message,
            rating = request.rating,
            isApproved = false  // always pending until admin approves
        )
        return testimonialRepository.save(testimonial).toResponse()
    }

    // ─── Admin ─────────────────────────────────────────────────────────────────

    fun getAllTestimonials(): List<TestimonialResponse> =
        testimonialRepository.findAllByOrderByCreatedAtDesc()
            .map { it.toResponse() }

    @Transactional
    fun approveTestimonial(id: UUID): TestimonialResponse {
        val testimonial = findOrThrow(id)
        testimonial.isApproved = true
        return testimonialRepository.save(testimonial).toResponse()
    }

    @Transactional
    fun rejectTestimonial(id: UUID): TestimonialResponse {
        val testimonial = findOrThrow(id)
        testimonial.isApproved = false
        return testimonialRepository.save(testimonial).toResponse()
    }

    @Transactional
    fun deleteTestimonial(id: UUID) {
        findOrThrow(id)
        testimonialRepository.deleteById(id)
    }

    // ─── Helpers ───────────────────────────────────────────────────────────────

    private fun findOrThrow(id: UUID): Testimonial =
        testimonialRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Testimonial not found with id: $id") }

    private fun Testimonial.toResponse() = TestimonialResponse(
        id = id,
        customerName = customerName,
        message = message,
        rating = rating,
        isApproved = isApproved,
        createdAt = createdAt
    )
}
