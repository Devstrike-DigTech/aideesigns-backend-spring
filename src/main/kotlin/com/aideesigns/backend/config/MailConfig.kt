package com.aideesigns.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "app.mail")
class MailConfig {
    var fromAddress: String = "noreply@aideesigns.com"
    var fromName: String = "Aideesigns & Stitches"
    var adminEmail: String = "admin@aideesigns.com"
}
