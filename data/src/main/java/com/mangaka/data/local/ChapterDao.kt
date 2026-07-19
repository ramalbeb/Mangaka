package com.mangaka.data.local

import androidx.room.*

@Dao
interface ChapterDao {

    @Query("SELECT * FROM chapter WHERE mangaId = :mangaId ORDER BY number ASC")
    suspend fun getChapters(mangaId: Long): List<ChapterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chapters: List<ChapterEntity>)
}
