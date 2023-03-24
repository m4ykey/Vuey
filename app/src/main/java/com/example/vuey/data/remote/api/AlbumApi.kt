package com.example.vuey.data.remote.api

import com.example.vuey.data.remote.response.AlbumDetailResponse
import com.example.vuey.data.remote.response.AlbumSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumApi {

    @GET("?method=album.search&format=json")
    suspend fun searchAlbum(
        @Query("album") albumName : String
    ) : Response<AlbumSearchResponse>

    @GET("?method=album.getinfo&format=json")
    suspend fun getInfo(
        @Query("artist") artist : String,
        @Query("album") album : String
    ) : Response<AlbumDetailResponse>

}