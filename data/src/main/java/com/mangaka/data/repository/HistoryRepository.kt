package com.mangaka.data.repository

import com.mangaka.data.local.HistoryDao
import com.mangaka.data.local.HistoryEntity
import com.mangaka.domain.model.HistoryItem

class HistoryRepository(
    private val dao: HistoryDao
) {

    suspend fun addOrUpdate(item: HistoryItem) {
        val entity = HistoryEntity(
            mangaTitle = item.mangaTitle,
            coverUrl = item.coverUrl,
            sourceId = item.sourceId,
            mangaUrl = item.mangaUrl,
            chapterTitle = item.chapterTitle,
            chapterUrl = item.chapterUrl,
            lastPage = item.lastPage,
            timestamp = System.currentTimeMillis()
        )
        dao.insert(entity)
    }

    suspend fun getAll(): List<HistoryItem> =
        dao.getAll().map {
            HistoryItem(
                mangaTitle = it.mangaTitle,
                coverUrl = it.coverUrl,
                sourceId = it.sourceId,
                mangaUrl = it.mangaUrl,
                chapterTitle = it.chapterTitle,
                chapterUrl = it.chapterUrl,
                lastPage = it.lastPage,
                timestamp = it.timestamp
            )
        }

    suspend fun delete(url: String) = dao.deleteByUrl(url)

    suspend fun clear() = dao.clear()

    companion object
}
