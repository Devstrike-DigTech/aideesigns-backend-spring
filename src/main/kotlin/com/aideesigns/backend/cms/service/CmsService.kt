package com.aideesigns.backend.cms.service

import com.aideesigns.backend.cms.dto.ContentBlockRequest
import com.aideesigns.backend.cms.dto.ContentBlockResponse
import com.aideesigns.backend.cms.dto.ContentPageRequest
import com.aideesigns.backend.cms.dto.ContentPageResponse
import com.aideesigns.backend.cms.entity.ContentBlock
import com.aideesigns.backend.cms.entity.ContentPage
import com.aideesigns.backend.cms.repository.ContentBlockRepository
import com.aideesigns.backend.cms.repository.ContentPageRepository
import com.aideesigns.backend.shared.exception.ResourceNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class CmsService(
    private val pageRepository: ContentPageRepository,
    private val blockRepository: ContentBlockRepository
) {

    // ─── Public ────────────────────────────────────────────────────────────────

    fun getPageBySlug(slug: String): ContentPageResponse {
        val page = pageRepository.findBySlug(slug)
            ?: throw ResourceNotFoundException("Page not found with slug: $slug")
        return page.toResponse()
    }

    // ─── Admin ─────────────────────────────────────────────────────────────────

    fun getAllPages(): List<ContentPageResponse> =
        pageRepository.findAll().map { it.toResponse() }

    @Transactional
    fun createOrUpdatePage(request: ContentPageRequest): ContentPageResponse {
        val page = pageRepository.findBySlug(request.slug)
            ?: ContentPage(slug = request.slug, title = request.title).also {
                pageRepository.save(it)
            }

        page.title = request.title
        page.updatedAt = Instant.now()
        return pageRepository.save(page).toResponse()
    }

    @Transactional
    fun upsertBlock(slug: String, request: ContentBlockRequest): ContentBlockResponse {
        val page = pageRepository.findBySlug(slug)
            ?: throw ResourceNotFoundException("Page not found with slug: $slug")

        val block = blockRepository.findByPageIdAndBlockKey(page.id, request.blockKey)
            ?: ContentBlock(
                page = page,
                blockKey = request.blockKey,
                blockType = request.blockType
            ).also { page.blocks.add(it) }

        block.content = request.content
        block.imageUrl = request.imageUrl
        block.updatedAt = Instant.now()
        page.updatedAt = Instant.now()

        blockRepository.save(block)
        pageRepository.save(page)

        return block.toBlockResponse()
    }

    @Transactional
    fun deleteBlock(slug: String, blockKey: String) {
        val page = pageRepository.findBySlug(slug)
            ?: throw ResourceNotFoundException("Page not found with slug: $slug")

        val block = blockRepository.findByPageIdAndBlockKey(page.id, blockKey)
            ?: throw ResourceNotFoundException("Block not found: $blockKey")

        blockRepository.delete(block)
        page.updatedAt = Instant.now()
        pageRepository.save(page)
    }

    @Transactional
    fun deletePage(slug: String) {
        val page = pageRepository.findBySlug(slug)
            ?: throw ResourceNotFoundException("Page not found with slug: $slug")
        pageRepository.delete(page)
    }

    // ─── Helpers ───────────────────────────────────────────────────────────────

    private fun ContentPage.toResponse() = ContentPageResponse(
        id = id,
        slug = slug,
        title = title,
        blocks = blockRepository.findAllByPageId(id).map { it.toBlockResponse() },
        updatedAt = updatedAt
    )

    private fun ContentBlock.toBlockResponse() = ContentBlockResponse(
        id = id,
        blockKey = blockKey,
        blockType = blockType,
        content = content,
        imageUrl = imageUrl,
        updatedAt = updatedAt
    )
}
