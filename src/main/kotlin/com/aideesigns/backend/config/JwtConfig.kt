package com.aideesigns.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "jwt")
class JwtConfig {
    var secret: String = ""
    var expirationSeconds: Long = 86400  // 24 hours default
}
