package com.example.vuey.data.remote.response.album

import com.example.vuey.data.local.album.detail.*
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