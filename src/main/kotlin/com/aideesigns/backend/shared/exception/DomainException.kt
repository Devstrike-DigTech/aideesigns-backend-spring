package com.aideesigns.backend.shared.exception

import org.springframework.http.HttpStatus

open class DomainException(
    message: String,
    val status: HttpStatus = HttpStatus.BAD_REQUEST
) : RuntimeException(message)

class ResourceNotFoundException(message: String) :
    DomainException(message, HttpStatus.NOT_FOUND)

class UnauthorizedException(message: String) :
    DomainException(message, HttpStatus.UNAUTHORIZED)

class ForbiddenException(message: String) :
    DomainException(message, HttpStatus.FORBIDDEN)

class ConflictException(message: String) :
    DomainException(message, HttpStatus.CONFLICT)
