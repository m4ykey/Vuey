package com.example.vuey.feature_album.data.response

import com.example.vuey.feature_album.data.api.detail.Artist
import com.example.vuey.feature_album.data.api.detail.ExternalUrls
import com.example.vuey.feature_album.data.api.detail.Image
import com.example.vuey.feature_album.data.api.detail.Tracks
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