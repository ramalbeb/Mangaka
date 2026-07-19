package com.mangaka.data.local

import androidx.room.*

@Dao
interface PageDao {

    @Query("SELECT * FROM page WHERE chapterId = :chapterId ORDER BY 'index' ASC")
    suspend fun getPages(chapterId: Long): List<PageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pages: List<PageEntity>)
}
