package com.example.vuey.di

import com.example.vuey.feature_album.data.remote.token.SpotifyInterceptor
import com.example.vuey.feature_movie.data.api.TMDBInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HttpClientModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        tmdbInterceptor: TMDBInterceptor,
        spotifyInterceptor: SpotifyInterceptor
    ) : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(tmdbInterceptor)
            .addInterceptor(spotifyInterceptor)
            .build()
    }

}