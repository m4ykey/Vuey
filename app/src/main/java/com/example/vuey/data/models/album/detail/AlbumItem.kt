package com.example.vuey.data.models.album.detail

data class AlbumItem(
    val artists: List<Artist>,
    val duration_ms: Int,
    val external_urls: ExternalUrls,
    val id: String,
    val name: String,
    val track_number: Int,
    val type: String,
)