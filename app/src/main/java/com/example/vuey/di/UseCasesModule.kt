package com.example.vuey.di

import com.example.vuey.feature_album.presentation.viewmodel.use_cases.AlbumDetailUseCase
import com.example.vuey.feature_album.presentation.viewmodel.use_cases.AlbumSearchUseCase
import com.example.vuey.feature_album.presentation.viewmodel.use_cases.AlbumUseCases
import com.example.vuey.feature_movie.presentation.viewmodel.use_case.MovieSearchUseCase
import com.example.vuey.feature_movie.presentation.viewmodel.use_case.MovieUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    fun provideAlbumUseCases(
        getAlbumSearchUseCase: AlbumSearchUseCase,
        getAlbumDetailUseCase: AlbumDetailUseCase
    ) : AlbumUseCases {
        return AlbumUseCases(
            getAlbumSearchUseCase,
            getAlbumDetailUseCase
        )
    }

    @Provides
    fun provideMovieUseCases(
        getMovieSearchUseCase: MovieSearchUseCase
    ) : MovieUseCases {
        return MovieUseCases(
            getMovieSearchUseCase
        )
    }
}