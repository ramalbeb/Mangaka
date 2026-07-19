package com.mangaka.data.mapper

import com.mangaka.data.local.PageEntity
import com.mangaka.domain.model.Page

fun PageEntity.toDomain() = Page(
    index = index,
    imageUrl = imageUrl,
    chapterId = chapterId
)

fun Page.toEntity() = PageEntity(
    id = 0L,
    chapterId = chapterId,
    index = index,
    imageUrl = imageUrl
)
