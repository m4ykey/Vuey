package com.example.vuey.data.remote.api.movie

import com.example.vuey.data.remote.response.movie.MovieSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale

interface MovieApi {

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query : String,
        @Query("language") language : String = Locale.getDefault().language,
        @Query("include_adult") adult : Boolean = false
    ) : Response<MovieSearchResponse>
}