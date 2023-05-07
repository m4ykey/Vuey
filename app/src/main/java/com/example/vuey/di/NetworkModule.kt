package com.example.vuey.di

import com.example.vuey.feature_album.data.api.AlbumApi
import com.example.vuey.feature_album.data.api.SpotifyAuthInterceptor
import com.example.vuey.feature_movie.data.api.MovieApi
import com.example.vuey.feature_tv_show.data.api.TvShowApi
import com.example.vuey.feature_movie.data.api.TMDBInterceptor
import com.example.vuey.util.Constants.SPOTIFY_BASE_URL
import com.example.vuey.util.Constants.TMDB_BASE_URL
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(
        authInterceptor: SpotifyAuthInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        TMDBInterceptor: TMDBInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(TMDBInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideAlbumInstance(
        httpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ) : AlbumApi {
        return Retrofit.Builder()
            .baseUrl(SPOTIFY_BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(httpClient)
            .build()
            .create(AlbumApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieInstance(
        httpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ) : MovieApi {
        return Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(httpClient)
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTvShowInstance(
        httpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ) : TvShowApi {
        return Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(httpClient)
            .build()
            .create(TvShowApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieInterceptor() : TMDBInterceptor {
        return TMDBInterceptor()
    }
}