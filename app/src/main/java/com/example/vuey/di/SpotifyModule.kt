package com.example.vuey.di

import com.example.vuey.feature_album.data.remote.api.AlbumApi
import com.example.vuey.util.Constants.SPOTIFY_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SpotifyModule {

    @Provides
    @Singleton
    fun provideAlbumApi(retrofit: Retrofit) : AlbumApi {
        return retrofit.create(AlbumApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAlbumRetrofit(
        httpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(SPOTIFY_BASE_URL)
            .client(httpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

}
