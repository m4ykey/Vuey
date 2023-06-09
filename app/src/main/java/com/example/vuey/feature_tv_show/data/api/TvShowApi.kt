package com.example.vuey.feature_tv_show.data.api

import com.example.vuey.feature_movie.data.remote.model.Credits
import com.example.vuey.feature_tv_show.data.response.TvShowDetailResponse
import com.example.vuey.feature_tv_show.data.response.TvShowSearchResponse
import com.example.vuey.feature_tv_show.data.response.TvShowSeasonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Locale

interface TvShowApi {

    @GET("search/tv")
    suspend fun searchTvShow(
        @Query("query") query : String,
        @Query("language") language : String = Locale.getDefault().language
    ) : Response<TvShowSearchResponse>

    @GET("tv/{tv_id}")
    suspend fun detailTvShow(
        @Path("tv_id") id : Int,
        @Query("language") language: String = Locale.getDefault().language
    ) : Response<TvShowDetailResponse>

    @GET("tv/{tv_id}/credits")
    suspend fun tvShowCredits(
        @Path("tv_id") id : Int
    ) : Response<Credits>

    @GET("tv/{tv_id}/season/{season_number}")
    suspend fun tvShowSeasons(
        @Path("tv_id") tvId : Int,
        @Path("season_number") seasonNumber : Int,
        @Query("language") language: String = Locale.getDefault().language
    ) : Response<TvShowSeasonResponse>

}