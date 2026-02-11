package com.aideesigns.backend.shared.enums

enum class BookingStatus {
    PENDING,        // Submitted, awaiting admin review
    APPROVED,       // Admin approved, in production queue
    IN_PROGRESS,    // Being worked on
    COMPLETED,      // Ready / delivered
    DECLINED,       // Admin declined
    CANCELLED       // Customer cancelled
}
