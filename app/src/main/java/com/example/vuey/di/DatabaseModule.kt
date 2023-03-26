package com.example.vuey.di

import android.app.Application
import androidx.room.Room
import com.example.vuey.data.local.database.AppDatabase
import com.example.vuey.data.local.database.dao.AlbumDao
import com.example.vuey.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAlbumDao(database: AppDatabase) : AlbumDao {
        return database.albumDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application) : AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }

}