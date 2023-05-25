package com.example.vuey.di

import com.example.vuey.feature_album.data.repository.AlbumRepositoryImpl
import com.example.vuey.feature_album.data.repository.AlbumRepository
import com.example.vuey.feature_album.use_cases.AlbumSearchUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideAlbumRepository(albumRepository: AlbumRepositoryImpl) : AlbumRepository {
        return albumRepository
    }

    @Provides
    fun provideAlbumSearchUseCases(albumRepository: AlbumRepositoryImpl) : AlbumSearchUseCase {
        return AlbumSearchUseCase(albumRepository)
    }

}