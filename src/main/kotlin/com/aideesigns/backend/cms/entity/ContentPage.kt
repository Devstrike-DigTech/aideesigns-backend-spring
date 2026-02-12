package com.aideesigns.backend.cms.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "content_pages")
class ContentPage(

    @Column(nullable = false, unique = true)
    val slug: String,

    @Column(nullable = false)
    var title: String

) {
    @Id
    val id: UUID = UUID.randomUUID()

    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()

    @Column(nullable = false)
    var updatedAt: Instant = Instant.now()

    @OneToMany(mappedBy = "page", cascade = [CascadeType.ALL], orphanRemoval = true)
    val blocks: MutableList<ContentBlock> = mutableListOf()
}
