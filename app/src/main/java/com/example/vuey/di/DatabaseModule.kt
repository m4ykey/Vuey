package com.example.vuey.di

import android.app.Application
import androidx.room.Room
import com.example.vuey.feature_album.data.local.dao.AlbumDao
import com.example.vuey.util.Constants.DATABASE_NAME
import com.example.vuey.util.database.VueyDatabase
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
    fun provideAppDatabase(application: Application) : VueyDatabase {
        return Room.databaseBuilder(
            application,
            VueyDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideAlbumDao(appDatabase: VueyDatabase) : AlbumDao {
        return appDatabase.albumDao()
    }

}