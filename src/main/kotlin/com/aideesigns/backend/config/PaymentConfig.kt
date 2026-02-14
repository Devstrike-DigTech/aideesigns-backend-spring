package com.aideesigns.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "payment.paystack")
class PaystackConfig {
    var secretKey: String = ""
    var publicKey: String = ""
    var baseUrl: String = "https://api.paystack.co"
    var webhookSecret: String = ""
}

@Component
@ConfigurationProperties(prefix = "payment.flutterwave")
class FlutterwaveConfig {
    var secretKey: String = ""
    var publicKey: String = ""
    var baseUrl: String = "https://api.flutterwave.com/v3"
    var webhookSecret: String = ""
}
