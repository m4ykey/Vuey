package com.example.vuey.di

import android.content.SharedPreferences
import com.example.vuey.data.remote.api.SpotifyAuthInterceptor
import com.example.vuey.data.remote.token.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideSpotifyAuthInterceptor(
        sharedPreferences: SharedPreferences,
        authApi: AuthApi
    ): SpotifyAuthInterceptor {
        return SpotifyAuthInterceptor(sharedPreferences, authApi)
    }

    @Provides
    fun provideAuthInstance() : AuthApi {
        return Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }
}