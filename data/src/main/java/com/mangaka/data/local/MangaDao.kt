package com.mangaka.data.local

import androidx.room.*

@Dao
interface MangaDao {

    @Query("SELECT * FROM manga")
    suspend fun getAll(): List<MangaEntity>

    @Query("SELECT * FROM manga WHERE id = :id")
    suspend fun getById(id: Long): MangaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(manga: MangaEntity): Long

    @Update
    suspend fun update(manga: MangaEntity)

    @Delete
    suspend fun delete(manga: MangaEntity)
}
