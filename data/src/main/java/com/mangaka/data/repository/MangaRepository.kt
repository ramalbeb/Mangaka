package com.mangaka.data.repository

import com.mangaka.data.local.MangaDao
import com.mangaka.data.mapper.toDomain
import com.mangaka.data.mapper.toEntity
import com.mangaka.domain.model.Manga

class MangaRepository(
    private val mangaDao: MangaDao
) {

    suspend fun getAll(): List<Manga> =
        mangaDao.getAll().map { it.toDomain() }

    suspend fun getById(id: Long): Manga? =
        mangaDao.getById(id)?.toDomain()

    suspend fun insert(manga: Manga): Long =
        mangaDao.insert(manga.toEntity())

    suspend fun update(manga: Manga) =
        mangaDao.update(manga.toEntity())

    suspend fun delete(manga: Manga) =
        mangaDao.delete(manga.toEntity())
}
