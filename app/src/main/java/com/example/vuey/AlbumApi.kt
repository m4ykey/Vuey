package com.example.vuey

import com.example.vuey.model.AlbumSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumApi {

    @GET("?method=album.search&api_key=&format=json")
    suspend fun searchAlbum(
        @Query("album") albumName : String
    ) : Response<AlbumSearchResponse>

}