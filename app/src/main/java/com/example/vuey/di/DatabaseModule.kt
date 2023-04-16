package com.example.vuey.di

import android.app.Application
import androidx.room.Room
import com.example.vuey.data.database.AppDatabase
import com.example.vuey.data.database.dao.AlbumDao
import com.example.vuey.data.database.dao.MovieDao
import com.example.vuey.util.Constants.DATABASE_NAME
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
    fun provideAppDatabase(application: Application) : AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideAlbumDao(appDatabase: AppDatabase) : AlbumDao {
        return appDatabase.albumDao()
    }

    @Provides
    @Singleton
    fun provideMovieDao(appDatabase: AppDatabase) : MovieDao {
        return appDatabase.movieDao()
    }

}