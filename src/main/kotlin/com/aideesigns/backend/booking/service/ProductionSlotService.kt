package com.aideesigns.backend.booking.service

import com.aideesigns.backend.booking.domain.ProductionCapacityCalculator
import com.aideesigns.backend.booking.dto.ProductionSlotRequest
import com.aideesigns.backend.booking.dto.ProductionSlotResponse
import com.aideesigns.backend.booking.entity.ProductionSlot
import com.aideesigns.backend.booking.repository.ProductionSlotRepository
import com.aideesigns.backend.shared.exception.ConflictException
import com.aideesigns.backend.shared.exception.ResourceNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.*

@Service
class ProductionSlotService(
    private val slotRepository: ProductionSlotRepository
) {

    fun getAvailableSlots(): List<ProductionSlotResponse> =
        slotRepository.findAvailableSlots(LocalDate.now().plusDays(7))
            .map { it.toResponse() }

    fun getSlotsByDateRange(from: LocalDate, to: LocalDate): List<ProductionSlotResponse> =
        slotRepository.findAllByProductionDateBetweenOrderByProductionDateAsc(from, to)
            .map { it.toResponse() }

    fun findByDate(date: LocalDate): ProductionSlot? =
        slotRepository.findByProductionDate(date)

    @Transactional
    fun createSlot(request: ProductionSlotRequest): ProductionSlotResponse {
        if (slotRepository.findByProductionDate(request.productionDate) != null) {
            throw ConflictException("A production slot already exists for ${request.productionDate}.")
        }

        val slot = ProductionSlot(
            productionDate = request.productionDate,
            maxCapacity = request.maxCapacity,
            isClosed = request.isClosed
        )
        return slotRepository.save(slot).toResponse()
    }

    @Transactional
    fun updateSlot(id: UUID, request: ProductionSlotRequest): ProductionSlotResponse {
        val slot = findSlotOrThrow(id)

        slot.maxCapacity = request.maxCapacity
        slot.isClosed = request.isClosed

        return slotRepository.save(slot).toResponse()
    }

    @Transactional
    fun toggleSlotClosed(id: UUID): ProductionSlotResponse {
        val slot = findSlotOrThrow(id)
        slot.isClosed = !slot.isClosed
        return slotRepository.save(slot).toResponse()
    }

    @Transactional
    fun deleteSlot(id: UUID) {
        findSlotOrThrow(id)
        slotRepository.deleteById(id)
    }

    fun findSlotOrThrow(id: UUID): ProductionSlot =
        slotRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Production slot not found with id: $id") }

    private fun ProductionSlot.toResponse() = ProductionSlotResponse(
        id = id,
        productionDate = productionDate,
        maxCapacity = maxCapacity,
        bookedCount = bookedCount,
        remainingCapacity = ProductionCapacityCalculator.remainingCapacity(this),
        isClosed = isClosed,
        isAvailable = isAvailable
    )
}
