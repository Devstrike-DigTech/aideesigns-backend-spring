package com.aideesigns.backend.admin.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "admins")
class Admin(

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    var passwordHash: String,

    @Column(nullable = false)
    val role: String = "ADMIN"

) {
    @Id
    val id: UUID = UUID.randomUUID()

    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()
}
