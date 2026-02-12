package com.aideesigns.backend.config

import com.aideesigns.backend.auth.filter.JwtAuthenticationFilter
import com.aideesigns.backend.auth.security.SecurityBeans
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val securityBeans: SecurityBeans
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors {  }
            .csrf { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { auth ->
                auth
                    // Auth
                    .requestMatchers("/api/auth/login", "/api/auth/setup").permitAll()

                    // Public products
                    .requestMatchers("/api/products", "/api/products/**").permitAll()

                    // Public bookings
                    .requestMatchers("/api/bookings", "/api/bookings/**").permitAll()

                    // Public orders tracking
                    .requestMatchers("/api/orders", "/api/orders/**").permitAll()

                    // Public testimonials
                    .requestMatchers("/api/testimonials", "/api/testimonials/**").permitAll()

                    // Public CMS content
                    .requestMatchers("/api/content/**").permitAll()

                    // Payment webhooks
                    .requestMatchers("/api/payments/webhook/**").permitAll()

                    // Swagger
                    .requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html"
                    ).permitAll()

                    // Actuator
                    .requestMatchers("/actuator/health").permitAll()

                    // Everything else — must be authenticated
                    .anyRequest().authenticated()
            }
            .authenticationProvider(securityBeans.authenticationProvider())
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter::class.java
            )

        return http.build()
    }
}
