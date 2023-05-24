package com.example.vuey.feature_album.data.remote.model

import com.example.vuey.feature_album.data.remote.model.Artist
import com.example.vuey.feature_album.data.remote.model.ExternalUrls
import com.example.vuey.feature_album.data.remote.model.Image
import com.example.vuey.feature_album.data.remote.model.Tracks
import com.google.gson.annotations.SerializedName

data class AlbumDetailResponse(
    val album_type: String,
    val artists: List<Artist>,
    val external_urls: ExternalUrls,
    val id: String,
    val images: List<Image>,
    @SerializedName("name")
    val albumName: String,
    val release_date: String,
    val total_tracks: Int,
    val tracks: Tracks
)