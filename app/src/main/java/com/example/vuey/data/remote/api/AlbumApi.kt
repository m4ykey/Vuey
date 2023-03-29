package com.example.vuey.data.remote.api

import com.example.vuey.data.remote.response.SearchAlbumResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AlbumApi {

    @GET("v1/search")
    suspend fun searchAlbum(
        @Query("q") query : String,
        @Query("type") type : String = "album",
        @Header("Authorization") token : String
    ) : Response<SearchAlbumResponse>

}