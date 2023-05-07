package com.example.vuey.feature_movie.data.api

import com.example.vuey.feature_movie.data.response.MovieTvShowCreditsResponse
import com.example.vuey.feature_movie.data.response.MovieDetailResponse
import com.example.vuey.feature_movie.data.response.MovieSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Locale

interface MovieApi {

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query : String,
        @Query("language") language : String = Locale.getDefault().language
    ) : Response<MovieSearchResponse>

    @GET("movie/{movie_id}")
    suspend fun movieDetails(
        @Path("movie_id") id: Int,
        @Query("language") language : String = Locale.getDefault().language
    ) : Response<MovieDetailResponse>

    @GET("movie/{movie_id}/credits")
    suspend fun movieCredits(
        @Path("movie_id") id : Int
    ) : Response<MovieTvShowCreditsResponse>

}