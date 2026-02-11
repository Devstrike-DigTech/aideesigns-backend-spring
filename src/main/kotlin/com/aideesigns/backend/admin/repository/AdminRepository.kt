package com.aideesigns.backend.admin.repository

import com.aideesigns.backend.admin.entity.Admin
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AdminRepository : JpaRepository<Admin, UUID> {
    fun findByEmail(email: String): Admin?
    fun existsByEmail(email: String): Boolean
}
