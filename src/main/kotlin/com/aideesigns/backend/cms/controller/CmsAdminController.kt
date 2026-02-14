package com.aideesigns.backend.cms.controller

import com.aideesigns.backend.cms.dto.ContentBlockRequest
import com.aideesigns.backend.cms.dto.ContentBlockResponse
import com.aideesigns.backend.cms.dto.ContentPageRequest
import com.aideesigns.backend.cms.dto.ContentPageResponse
import com.aideesigns.backend.cms.service.CmsService
import com.aideesigns.backend.shared.dto.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@Tag(name = "Admin — CMS", description = "Admin endpoints for managing website content")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/admin/content")
class CmsAdminController(private val cmsService: CmsService) {

    @GetMapping
    @Operation(summary = "Get all pages")
    fun getAllPages(): ApiResponse<List<ContentPageResponse>> =
        ApiResponse.success(cmsService.getAllPages())

    @PostMapping("/pages")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create or update a page")
    fun createOrUpdatePage(
        @Valid @RequestBody request: ContentPageRequest
    ): ApiResponse<ContentPageResponse> =
        ApiResponse.success(
            cmsService.createOrUpdatePage(request),
            "Page saved successfully"
        )

    @PutMapping("/{slug}/blocks")
    @Operation(
        summary = "Upsert a content block",
        description = "Creates a new block or updates an existing one if blockKey already exists for this page."
    )
    fun upsertBlock(
        @PathVariable slug: String,
        @Valid @RequestBody request: ContentBlockRequest
    ): ApiResponse<ContentBlockResponse> =
        ApiResponse.success(
            cmsService.upsertBlock(slug, request),
            "Block saved successfully"
        )

    @DeleteMapping("/{slug}/blocks/{blockKey}")
    @Operation(summary = "Delete a content block")
    fun deleteBlock(
        @PathVariable slug: String,
        @PathVariable blockKey: String
    ): ApiResponse<Nothing> {
        cmsService.deleteBlock(slug, blockKey)
        return ApiResponse.success("Block deleted successfully")
    }

    @DeleteMapping("/{slug}")
    @Operation(summary = "Delete an entire page and all its blocks")
    fun deletePage(@PathVariable slug: String): ApiResponse<Nothing> {
        cmsService.deletePage(slug)
        return ApiResponse.success("Page deleted successfully")
    }
}
