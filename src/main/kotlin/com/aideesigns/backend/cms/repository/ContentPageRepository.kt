package com.aideesigns.backend.cms.repository

import com.aideesigns.backend.cms.entity.ContentPage
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ContentPageRepository : JpaRepository<ContentPage, UUID> {
    fun findBySlug(slug: String): ContentPage?
}
