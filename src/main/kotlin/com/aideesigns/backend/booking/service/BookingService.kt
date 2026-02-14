package com.aideesigns.backend.booking.service

import com.aideesigns.backend.booking.domain.BookingPolicy
import com.aideesigns.backend.booking.domain.ProductionCapacityCalculator
import com.aideesigns.backend.booking.dto.BookingRequest
import com.aideesigns.backend.booking.dto.BookingResponse
import com.aideesigns.backend.booking.dto.BookingStatusUpdateRequest
import com.aideesigns.backend.booking.entity.Booking
import com.aideesigns.backend.booking.repository.BookingRepository
import com.aideesigns.backend.booking.repository.ProductionSlotRepository
import com.aideesigns.backend.notification.events.BookingCreatedEvent
import com.aideesigns.backend.shared.enums.BookingStatus
import com.aideesigns.backend.shared.exception.DomainException
import com.aideesigns.backend.shared.exception.ResourceNotFoundException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class BookingService(
    private val bookingRepository: BookingRepository,
    private val slotRepository: ProductionSlotRepository,
    private val productionSlotService: ProductionSlotService,
    private val eventPublisher: ApplicationEventPublisher

) {

    // ─── Public ────────────────────────────────────────────────────────────────

    @Transactional
    fun createBooking(request: BookingRequest): BookingResponse {

        // Enforce 7-day minimum notice
        BookingPolicy.validateBookingDate(request.preferredDate)

        // Find the slot for the requested date
        val slot = slotRepository.findByProductionDate(request.preferredDate)
            ?: throw DomainException(
                "No production slot is available for ${request.preferredDate}. " +
                "Please choose from the available dates."
            )

        // Check capacity and closed status
        ProductionCapacityCalculator.assertSlotAvailable(slot)

        val booking = Booking(
            customerName = request.customerName,
            phone = request.phone,
            email = request.email,
            outfitType = request.outfitType,
            inspirationImageUrl = request.inspirationImageUrl,
            notes = request.notes,
            productionSlot = slot,
            status = BookingStatus.PENDING
        )

        slot.incrementBookedCount()
        slotRepository.save(slot)

        val savedBooking = bookingRepository.save(booking)
        eventPublisher.publishEvent(BookingCreatedEvent(this, savedBooking))
        return savedBooking.toResponse()
//        return bookingRepository.save(booking).toResponse()
    }

    // Customer can track their booking using booking ID + phone or email
    fun trackBooking(id: UUID, phone: String?, email: String?): BookingResponse {
        if (phone == null && email == null) {
            throw DomainException("Please provide either your phone number or email to track your booking.")
        }

        val booking = when {
            phone != null -> bookingRepository.findByIdAndPhone(id, phone)
            else -> bookingRepository.findByIdAndEmail(id, email!!)
        } ?: throw ResourceNotFoundException("Booking not found. Please check your booking ID and contact details.")

        return booking.toResponse()
    }

    // ─── Admin ─────────────────────────────────────────────────────────────────

    fun getAllBookings(): List<BookingResponse> =
        bookingRepository.findAllByOrderByCreatedAtDesc()
            .map { it.toResponse() }

    fun getBookingsByStatus(status: BookingStatus): List<BookingResponse> =
        bookingRepository.findAllByStatus(status)
            .map { it.toResponse() }

    fun getBookingById(id: UUID): BookingResponse =
        findBookingOrThrow(id).toResponse()

    @Transactional
    fun updateBookingStatus(id: UUID, request: BookingStatusUpdateRequest): BookingResponse {
        val booking = findBookingOrThrow(id)

        // If declining or cancelling, free up the slot capacity
        val wasActive = booking.status in listOf(
            BookingStatus.PENDING,
            BookingStatus.APPROVED,
            BookingStatus.IN_PROGRESS
        )
        val isBeingClosed = request.status in listOf(
            BookingStatus.DECLINED,
            BookingStatus.CANCELLED
        )

        if (wasActive && isBeingClosed) {
            booking.productionSlot?.let { slot ->
                slot.decrementBookedCount()
                slotRepository.save(slot)
            }
        }

        booking.status = request.status
        return bookingRepository.save(booking).toResponse()
    }

    // ─── Helpers ───────────────────────────────────────────────────────────────

    private fun findBookingOrThrow(id: UUID): Booking =
        bookingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Booking not found with id: $id") }

    private fun Booking.toResponse() = BookingResponse(
        id = id,
        customerName = customerName,
        phone = phone,
        email = email,
        outfitType = outfitType,
        inspirationImageUrl = inspirationImageUrl,
        notes = notes,
        status = status,
        productionDate = productionSlot?.productionDate,
        createdAt = createdAt
    )
}
