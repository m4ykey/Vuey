package com.example.vuey.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vuey.data.database.AppDatabase
import com.example.vuey.data.database.dao.AlbumDao
import com.example.vuey.data.repository.AlbumRepository
import com.example.vuey.util.Constants.DATABASE_NAME
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlbumDaoImpl @Inject constructor(database: AppDatabase) : AlbumDao by database.albumDao()

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    @Singleton
    @Binds
    abstract fun bindAlbumDao(albumDaoImpl: AlbumDaoImpl) : AlbumDao

    companion object {
        @Provides
        @Singleton
        fun provideAppDatabase(application: Application) : AppDatabase {
            return Room.databaseBuilder(
                application,
                AppDatabase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
        }
    }
}