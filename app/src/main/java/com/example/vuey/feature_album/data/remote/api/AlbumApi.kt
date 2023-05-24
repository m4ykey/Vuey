package com.example.vuey.feature_album.data.remote.api

import com.example.vuey.feature_album.data.remote.model.AlbumDetailResponse
import com.example.vuey.feature_album.data.remote.model.SearchAlbumResponse
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