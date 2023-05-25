package com.example.vuey.feature_album.data.remote.model

import com.google.gson.annotations.SerializedName

data class AlbumDetail(
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