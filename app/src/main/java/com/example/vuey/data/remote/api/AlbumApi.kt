package com.example.vuey.data.remote.api

import com.example.vuey.data.remote.response.AlbumSearchResponse
import com.example.vuey.data.remote.response.AlbumDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AlbumApi {

    @GET("?method=album.search")
    suspend fun searchAlbum(
        @Query("album") albumName : String,
        @Query("format") format : String = "json"
    ) : Response<AlbumSearchResponse>

    @GET("?method=album.getinfo")
    suspend fun getInfo(
        @Path("artist") artistName : String,
        @Path("album") albumName : String,
        //@Query("format") format : String = "json"
    ) : Response<AlbumDetailResponse>

}