package com.aideesigns.backend.auth.service

import com.aideesigns.backend.config.JwtConfig
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService(private val jwtConfig: JwtConfig) {

    private val signingKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtConfig.secret.toByteArray())
    }

    fun generateToken(email: String, role: String): String {
        val now = Date()
        val expiry = Date(now.time + jwtConfig.expirationSeconds * 1000)

        return Jwts.builder()
            .subject(email)
            .claim("role", role)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(signingKey)
            .compact()
    }

    fun extractEmail(token: String): String =
        extractClaims(token).subject

    fun extractRole(token: String): String =
        extractClaims(token).get("role", String::class.java)

    fun isTokenValid(token: String, email: String): Boolean =
        runCatching {
            extractEmail(token) == email && !isTokenExpired(token)
        }.getOrDefault(false)

    private fun isTokenExpired(token: String): Boolean =
        extractClaims(token).expiration.before(Date())

    private fun extractClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload
}
