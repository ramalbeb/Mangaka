package com.mangaka.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: MangaDatabase? = null

    fun get(context: Context): MangaDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                MangaDatabase::class.java,
                "manga.db"
            ).build().also { INSTANCE = it }
        }
    }
}
