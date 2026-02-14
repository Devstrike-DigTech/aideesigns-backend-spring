package com.aideesigns.backend.booking.repository

import com.aideesigns.backend.booking.entity.ProductionSlot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.util.*

interface ProductionSlotRepository : JpaRepository<ProductionSlot, UUID> {
    fun findByProductionDate(date: LocalDate): ProductionSlot?
    fun findAllByProductionDateBetweenOrderByProductionDateAsc(
        from: LocalDate,
        to: LocalDate
    ): List<ProductionSlot>

    @Query("SELECT s FROM ProductionSlot s WHERE s.productionDate >= :from AND s.isClosed = false AND s.bookedCount < s.maxCapacity ORDER BY s.productionDate ASC")
    fun findAvailableSlots(from: LocalDate): List<ProductionSlot>
}
