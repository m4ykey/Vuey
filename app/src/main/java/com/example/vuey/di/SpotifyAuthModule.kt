package com.example.vuey.di

import com.example.vuey.feature_album.data.remote.api.AuthApi
import com.example.vuey.util.Constants.SPOTIFY_AUTH_URL
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
object SpotifyAuthModule {

    @Provides
    @Singleton
    fun provideSpotifyAuth(
        gsonConverterFactory: GsonConverterFactory
    ) : AuthApi {
        return Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(SPOTIFY_AUTH_URL)
            .build()
            .create(AuthApi::class.java)
    }

}