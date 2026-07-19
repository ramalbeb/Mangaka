package com.mangaka.data.repository

import com.mangaka.data.local.PageDao
import com.mangaka.data.mapper.toDomain
import com.mangaka.data.mapper.toEntity
import com.mangaka.domain.model.Page

class PageRepository(
    private val pageDao: PageDao
) {

    suspend fun getPages(chapterId: Long): List<Page> =
        pageDao.getPages(chapterId).map { it.toDomain() }

    suspend fun insertAll(pages: List<Page>) =
        pageDao.insertAll(pages.map { it.toEntity() })
}
