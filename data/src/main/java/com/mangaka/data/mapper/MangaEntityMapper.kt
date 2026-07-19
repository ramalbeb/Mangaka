package com.mangaka.data.mapper

import com.mangaka.data.local.MangaEntity
import com.mangaka.domain.model.Manga

fun MangaEntity.toDomain() = Manga(
    id = id,
    url = url,
    title = title,
    coverUrl = coverUrl,
    description = description,
    author = author,
    artist = artist,
    genres = genres.split(","),
    status = status,
    isFavorite = isFavorite,
    isDownloaded = isDownloaded,
    lastRead = lastRead,
    lastUpdated = lastUpdated,
    unreadCount = unreadCount
)

fun Manga.toEntity() = MangaEntity(
    id = id,
    url = url,
    title = title,
    coverUrl = coverUrl,
    description = description,
    author = author,
    artist = artist,
    genres = genres.joinToString(","),
    status = status,
    isFavorite = isFavorite,
    isDownloaded = isDownloaded,
    lastRead = lastRead,
    lastUpdated = lastUpdated,
    unreadCount = unreadCount
)
