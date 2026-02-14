package com.aideesigns.backend.admin.service

import com.aideesigns.backend.admin.entity.Admin
import com.aideesigns.backend.admin.repository.AdminRepository
import com.aideesigns.backend.shared.exception.DomainException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val adminRepository: AdminRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun findByEmail(email: String): Admin =
        adminRepository.findByEmail(email)
            ?: throw DomainException("Admin not found with email: $email")

    fun existsByEmail(email: String): Boolean =
        adminRepository.existsByEmail(email)

    fun registerFirstAdmin(email: String, rawPassword: String): Admin {
        if (adminRepository.count() > 0) {
            throw DomainException("Setup already completed. An admin account already exists.")
        }
        if (adminRepository.existsByEmail(email)) {
            throw DomainException("Email already in use.")
        }

        val admin = Admin(
            email = email,
            passwordHash = passwordEncoder.encode(rawPassword)
        )
        return adminRepository.save(admin)
    }

    fun verifyPassword(rawPassword: String, encodedPassword: String): Boolean =
        passwordEncoder.matches(rawPassword, encodedPassword)
}
