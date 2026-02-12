package com.aideesigns.backend.notification.email

import com.aideesigns.backend.config.MailConfig
import jakarta.mail.internet.InternetAddress
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

// ─── Interface ─────────────────────────────────────────────────────────────

interface EmailService {
    fun sendHtml(to: String, subject: String, htmlBody: String)
}
