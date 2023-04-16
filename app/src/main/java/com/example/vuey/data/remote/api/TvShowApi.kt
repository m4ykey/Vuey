package com.example.vuey.data.remote.api

import com.example.vuey.data.remote.response.tv_show.TvShowSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale

interface TvShowApi {

    @GET("search/tv")
    suspend fun searchTvShow(
        @Query("query") query : String,
        @Query("language") language : String = Locale.getDefault().language,
        @Query("include_adult") adult : Boolean = false
    ) : Response<TvShowSearchResponse>

}