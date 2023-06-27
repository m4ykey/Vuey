package com.example.vuey.feature_album.data.remote.api

import com.example.vuey.feature_album.data.remote.model.last_fm.ArtistInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface ArtistApi {

    @GET("2.0/?method=artist.getinfo&format=json")
    suspend fun getArtistInfo(
        @Query("artist") artist : String
    ) : ArtistInfo
}