package com.example.vuey.di

import com.example.vuey.feature_album.use_cases.AlbumSearchUseCase
import com.example.vuey.feature_album.use_cases.UseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    fun provideUseCases(
        getAlbumSearchUseCase: AlbumSearchUseCase
    ) : UseCases {
        return UseCases(getAlbumSearchUseCase)
    }

}