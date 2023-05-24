package com.example.vuey.feature_album.data.remote.model

import com.google.gson.annotations.SerializedName

data class Tracks(
    val items: List<AlbumItem>
) {
    data class AlbumItem(
        @SerializedName("artists")
        val artistList: List<Artist>,
        @SerializedName("duration_ms")
        val durationMs: Int,
        @SerializedName("external_urls")
        val externalUrls: ExternalUrls,
        val id: String,
        @SerializedName("name")
        val albumName: String,
        @SerializedName("type")
        val albumType: String,
    )
}