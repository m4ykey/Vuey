package com.example.vuey.di

import com.example.vuey.feature_album.presentation.viewmodel.use_cases.AlbumArtistUseCase
import com.example.vuey.feature_album.presentation.viewmodel.use_cases.AlbumDetailUseCase
import com.example.vuey.feature_album.presentation.viewmodel.use_cases.AlbumSearchUseCase
import com.example.vuey.feature_album.presentation.viewmodel.use_cases.AlbumUseCases
import com.example.vuey.feature_album.presentation.viewmodel.use_cases.ArtistTopTracksUseCase
import com.example.vuey.feature_album.presentation.viewmodel.use_cases.ArtistUseCase
import com.example.vuey.feature_movie.presentation.viewmodel.use_case.MovieCastUseCase
import com.example.vuey.feature_movie.presentation.viewmodel.use_case.MovieDetailUseCase
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
        getAlbumDetailUseCase: AlbumDetailUseCase,
        getAlbumArtistUseCase: AlbumArtistUseCase,
        getArtistUseCase: ArtistUseCase,
        getArtistTopTrackUseCase : ArtistTopTracksUseCase
    ) : AlbumUseCases {
        return AlbumUseCases(
            getAlbumSearchUseCase,
            getAlbumDetailUseCase,
            getAlbumArtistUseCase,
            getArtistUseCase,
            getArtistTopTrackUseCase
        )
    }

    @Provides
    fun provideMovieUseCases(
        getMovieSearchUseCase: MovieSearchUseCase,
        getMovieDetailUseCase: MovieDetailUseCase,
        getMovieCastUseCase: MovieCastUseCase
    ) : MovieUseCases {
        return MovieUseCases(
            getMovieSearchUseCase,
            getMovieDetailUseCase,
            getMovieCastUseCase
        )
    }
}