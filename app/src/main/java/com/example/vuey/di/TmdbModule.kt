package com.example.vuey.di

import com.example.vuey.feature_movie.data.api.MovieApi
import com.example.vuey.feature_movie.data.api.TMDBInterceptor
import com.example.vuey.feature_tv_show.data.api.TvShowApi
import com.example.vuey.util.Constants
import com.google.android.datatransport.runtime.retries.Retries
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
object TmdbModule {

    @Provides
    @Singleton
    fun provideTmdbInterceptor(): TMDBInterceptor = TMDBInterceptor()

    @Provides
    @Singleton
    fun provideMovieRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): MovieApi {
        return Retrofit.Builder()
            .baseUrl(Constants.TMDB_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTvShowRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): TvShowApi {
        return Retrofit.Builder()
            .baseUrl(Constants.TMDB_BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
            .create(TvShowApi::class.java)
    }

}