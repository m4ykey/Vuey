package com.example.vuey.feature_movie.data.remote.api

import com.example.vuey.feature_movie.data.remote.model.SearchMovie
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query : String
    ) : SearchMovie

}