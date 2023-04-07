package com.example.vuey.di

import android.content.Context
import android.content.SharedPreferences
import com.example.vuey.data.remote.api.album.SpotifyAuthInterceptor
import com.example.vuey.data.remote.response.album.token.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("my_app_pref", Context.MODE_PRIVATE)
    }
    
}