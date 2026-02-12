package com.aideesigns.backend.notification.email

import com.aideesigns.backend.config.MailConfig
import jakarta.mail.internet.InternetAddress
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service


// ─── SMTP Implementation ───────────────────────────────────────────────────

@Service
class SmtpEmailService(
    private val mailSender: JavaMailSender,
    private val mailConfig: MailConfig
) : EmailService {

    override fun sendHtml(to: String, subject: String, htmlBody: String) {
        try {
            val message = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true, "UTF-8")

            helper.setFrom(InternetAddress(mailConfig.fromAddress, mailConfig.fromName))
            helper.setTo(to)
            helper.setSubject(subject)
            helper.setText(htmlBody, true)

            mailSender.send(message)
        } catch (ex: Exception) {
            // Log but don't rethrow — email failure should never break the main flow
            println("Failed to send email to $to: ${ex.message}")
        }
    }
}
