package com.aideesigns.backend.cms.repository

import com.aideesigns.backend.cms.entity.ContentBlock
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface ContentBlockRepository : JpaRepository<ContentBlock, UUID> {
    fun findByPageIdAndBlockKey(pageId: UUID, blockKey: String): ContentBlock?
    fun findAllByPageId(pageId: UUID): List<ContentBlock>
}
