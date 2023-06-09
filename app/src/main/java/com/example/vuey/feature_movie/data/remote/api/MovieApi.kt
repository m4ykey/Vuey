package com.example.vuey.feature_movie.data.remote.api

import com.example.vuey.feature_movie.data.remote.model.Credits
import com.example.vuey.feature_movie.data.remote.model.MovieDetail
import com.example.vuey.feature_movie.data.remote.model.MovieSearch
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Locale

interface MovieApi {

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query : String,
        @Query("language") language : String = Locale.getDefault().language
    ) : MovieSearch

    @GET("movie/{movie_id}")
    suspend fun movieDetails(
        @Path("movie_id") id: Int,
        @Query("language") language : String = Locale.getDefault().language
    ) : MovieDetail

    @GET("movie/{movie_id}/credits")
    suspend fun movieCredits(
        @Path("movie_id") id : Int
    ) : Credits

}