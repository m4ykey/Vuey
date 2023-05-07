package com.example.vuey.feature_album.data.api.detail

data class AlbumItem(
    val artists: List<Artist>,
    val duration_ms: Int,
    val external_urls: ExternalUrls,
    val id: String,
    val name: String,
    val track_number: Int,
    val type: String,
    val disc_number: Int
)