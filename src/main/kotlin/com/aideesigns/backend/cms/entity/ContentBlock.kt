package com.aideesigns.backend.cms.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "content_blocks")
class ContentBlock(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id", nullable = false)
    val page: ContentPage,

    @Column(nullable = false)
    val blockKey: String,

    @Column(nullable = false)
    val blockType: String,  // text, image, html

    @Column(columnDefinition = "TEXT")
    var content: String? = null,

    @Column
    var imageUrl: String? = null

) {
    @Id
    val id: UUID = UUID.randomUUID()

    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()

    @Column(nullable = false)
    var updatedAt: Instant = Instant.now()
}

