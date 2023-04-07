package com.example.vuey.data.remote.api.album

import com.example.vuey.data.remote.response.album.AlbumDetailResponse
import com.example.vuey.data.remote.response.album.SearchAlbumResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface AlbumApi {

    @GET("v1/search")
    suspend fun searchAlbum(
        @Query("q") query : String,
        @Query("type") type : String = "album",
        @Header("Authorization") token : String
    ) : Response<SearchAlbumResponse>

    @GET("v1/albums/{id}")
    suspend fun getAlbum(
        @Path("id") albumId : String,
        @Header("Authorization") token : String
    ) : Response<AlbumDetailResponse>

}