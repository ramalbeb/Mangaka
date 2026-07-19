package com.mangaka.data.repository

import com.mangaka.data.local.ChapterDao
import com.mangaka.data.mapper.toDomain
import com.mangaka.data.mapper.toEntity
import com.mangaka.domain.model.Chapter

class ChapterRepository(
    private val chapterDao: ChapterDao
) {

    suspend fun getChapters(mangaId: Long): List<Chapter> =
        chapterDao.getChapters(mangaId).map { it.toDomain() }

    suspend fun insertAll(chapters: List<Chapter>) =
        chapterDao.insertAll(chapters.map { it.toEntity() })
}
