package com.mangaka.sources.mangadex

data class MangaDexListResponse<T>(
    val result: String?,
    val data: List<T>?
)

data class MangaDexSingleResponse<T>(
    val result: String?,
    val data: T?
)

data class MangaDexManga(
    val id: String,
    val attributes: MangaDexMangaAttributes?,
    val relationships: List<MangaDexRelationship>?
)

data class MangaDexMangaAttributes(
    val title: Map<String, String>?,
    val description: Map<String, String>?,
    val status: String?
)

data class MangaDexRelationship(
    val id: String?,
    val type: String?,
    val attributes: MangaDexCoverAttributes?
)

data class MangaDexCoverAttributes(
    val fileName: String?
)

data class MangaDexChapter(
    val id: String,
    val attributes: MangaDexChapterAttributes?
)

data class MangaDexChapterAttributes(
    val chapter: String?,
    val title: String?,
    val translatedLanguage: String?
)

data class MangaDexChapterListResponse(
    val result: String?,
    val data: List<MangaDexChapter>?
)

data class MangaDexAtHomeResponse(
    val baseUrl: String?,
    val chapter: MangaDexAtHomeChapter?
)

data class MangaDexAtHomeChapter(
    val hash: String?,
    val data: List<String>?
)
