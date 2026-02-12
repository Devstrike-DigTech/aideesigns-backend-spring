package com.aideesigns.backend.testimonial.controller

import com.aideesigns.backend.shared.dto.ApiResponse
import com.aideesigns.backend.testimonial.dto.TestimonialRequest
import com.aideesigns.backend.testimonial.dto.TestimonialResponse
import com.aideesigns.backend.testimonial.service.TestimonialService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name = "Public — Testimonials", description = "Public endpoints for browsing and submitting testimonials")
@RestController
@RequestMapping("/api/testimonials")
class TestimonialController(
    private val testimonialService: TestimonialService
) {

    @GetMapping
    @Operation(summary = "Get all approved testimonials")
    fun getApproved(): ApiResponse<List<TestimonialResponse>> =
        ApiResponse.success(testimonialService.getApprovedTestimonials())

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Submit a testimonial",
        description = "Submitted testimonials are pending until approved by admin."
    )
    fun submit(
        @Valid @RequestBody request: TestimonialRequest
    ): ApiResponse<TestimonialResponse> =
        ApiResponse.success(
            testimonialService.submitTestimonial(request),
            "Thank you for your review! It will be published after approval."
        )
}

@Tag(name = "Admin — Testimonials", description = "Admin endpoints for managing testimonials")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/admin/testimonials")
class TestimonialAdminController(
    private val testimonialService: TestimonialService
) {

    @GetMapping
    @Operation(summary = "Get all testimonials including unapproved")
    fun getAll(): ApiResponse<List<TestimonialResponse>> =
        ApiResponse.success(testimonialService.getAllTestimonials())

    @PatchMapping("/{id}/approve")
    @Operation(summary = "Approve a testimonial")
    fun approve(@PathVariable id: UUID): ApiResponse<TestimonialResponse> =
        ApiResponse.success(
            testimonialService.approveTestimonial(id),
            "Testimonial approved and now visible to the public"
        )

    @PatchMapping("/{id}/reject")
    @Operation(summary = "Reject a testimonial — hides it from public")
    fun reject(@PathVariable id: UUID): ApiResponse<TestimonialResponse> =
        ApiResponse.success(
            testimonialService.rejectTestimonial(id),
            "Testimonial rejected"
        )

    @DeleteMapping("/{id}")
    @Operation(summary = "Permanently delete a testimonial")
    fun delete(@PathVariable id: UUID): ApiResponse<Nothing> {
        testimonialService.deleteTestimonial(id)
        return ApiResponse.success("Testimonial deleted")
    }
}
