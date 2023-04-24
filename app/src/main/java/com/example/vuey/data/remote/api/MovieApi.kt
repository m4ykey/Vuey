package com.example.vuey.data.remote.api

import com.example.vuey.data.remote.response.movie.MovieTvShowCreditsResponse
import com.example.vuey.data.remote.response.movie.MovieDetailResponse
import com.example.vuey.data.remote.response.movie.MovieSearchResponse
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