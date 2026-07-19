package com.mangaka.app.di

import android.content.Context
import androidx.room.Room
import com.mangaka.data.local.MangaDatabase
import com.mangaka.data.repository.*
import com.mangaka.reader.ReaderRepository
import com.mangaka.reader.ReaderUseCase
import com.mangaka.app.ui.library.LibraryUseCase
import com.mangaka.data.repository.MangaRepository
import com.mangaka.data.repository.HistoryRepository

object AppModule {

    // Database
    fun provideDatabase(context: Context) =
        Room.databaseBuilder(
            context,
            MangaDatabase::class.java,
            "manga.db"
        ).build()

    // DAOs
    fun provideMangaDao(db: MangaDatabase) = db.mangaDao()
    fun provideChapterDao(db: MangaDatabase) = db.chapterDao()
    fun providePageDao(db: MangaDatabase) = db.pageDao()

    // Repositories
    fun provideSourceRepository() = SourceRepository()
    fun provideMangaRepository(db: MangaDatabase) =
        MangaRepository(provideMangaDao(db))

    fun provideChapterRepository(db: MangaDatabase) =
        ChapterRepository(provideChapterDao(db))

    fun providePageRepository(db: MangaDatabase) =
        PageRepository(providePageDao(db))

    fun provideMangaDataRepository(
        sourceRepo: SourceRepository,
        mangaRepo: MangaRepository,
        chapterRepo: ChapterRepository,
        pageRepo: PageRepository
    ) = MangaDataRepository(sourceRepo, mangaRepo, chapterRepo, pageRepo)

    // Reader
    fun provideReaderRepository(
        sourceRepo: SourceRepository,
        pageRepo: PageRepository
    ) = ReaderRepository(sourceRepo, pageRepo)

    fun provideReaderUseCase(readerRepo: ReaderRepository) =
        ReaderUseCase(readerRepo)

    fun provideLibraryUseCase(mangaRepo: MangaRepository) =
        LibraryUseCase(mangaRepo)

    fun provideHistoryRepository(db: MangaDatabase) =
        HistoryRepository(db.historyDao())



}
