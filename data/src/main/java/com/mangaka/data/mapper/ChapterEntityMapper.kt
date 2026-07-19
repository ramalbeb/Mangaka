package com.mangaka.data.mapper

import com.mangaka.data.local.ChapterEntity
import com.mangaka.domain.model.Chapter

fun ChapterEntity.toDomain() = Chapter(
    id = id,
    mangaId = mangaId,
    url = url,
    title = title,
    number = number,
    dateUpload = dateUpload,
    read = read,
    lastPageRead = lastPageRead,
    pageCount = pageCount,
    isBookmarked = isBookmarked
)

fun Chapter.toEntity() = ChapterEntity(
    id = id,
    mangaId = mangaId,
    url = url,
    title = title,
    number = number,
    dateUpload = dateUpload,
    read = read,
    lastPageRead = lastPageRead,
    pageCount = pageCount,
    isBookmarked = isBookmarked
)
