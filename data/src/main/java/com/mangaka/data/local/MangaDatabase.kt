package com.mangaka.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        MangaEntity::class,
        ChapterEntity::class,
        PageEntity::class,
        HistoryEntity::class
    ],
    version = 1
)
abstract class MangaDatabase : RoomDatabase() {

    abstract fun mangaDao(): MangaDao
    abstract fun chapterDao(): ChapterDao
    abstract fun pageDao(): PageDao
    abstract fun historyDao(): HistoryDao

}
