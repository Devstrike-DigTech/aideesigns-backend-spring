package com.aideesigns.backend.cms.controller

import com.aideesigns.backend.cms.dto.ContentPageResponse
import com.aideesigns.backend.cms.service.CmsService
import com.aideesigns.backend.shared.dto.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@Tag(name = "Public — CMS", description = "Public endpoints for fetching page content")
@RestController
@RequestMapping("/api/content")
class CmsPublicController(private val cmsService: CmsService) {

    @GetMapping("/{slug}")
    @Operation(
        summary = "Get page content by slug",
        description = "Returns all content blocks for a page. Common slugs: home, about, contact"
    )
    fun getPage(@PathVariable slug: String): ApiResponse<ContentPageResponse> =
        ApiResponse.success(cmsService.getPageBySlug(slug))
}
