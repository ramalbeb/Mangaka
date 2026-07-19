package com.mangaka.sources.mapper

import com.mangaka.domain.model.Chapter
import com.mangaka.domain.model.Manga
import com.mangaka.domain.model.Page
import com.mangaka.sources.models.SourceChapter
import com.mangaka.sources.models.SourceManga
import com.mangaka.sources.models.SourcePage

fun SourceManga.toDomain() = Manga(
    url = url,
    title = title,
    coverUrl = coverUrl,
    description = description ?: "",
    author = author ?: "",
    artist = artist ?: "",
    genres = genres ?: emptyList(),
    status = status ?: ""
)

fun SourceChapter.toDomain(mangaId: Long) = Chapter(
    mangaId = mangaId,
    url = url,
    title = title,
    number = number,
    dateUpload = dateUpload ?: 0L
)

fun SourcePage.toDomain(chapterId: Long) = Page(
    index = index,
    imageUrl = imageUrl,
    chapterId = chapterId
)
