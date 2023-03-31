package com.example.vuey.data.local.album.detail

data class AlbumItem(
    val artists: List<Artist>,
    val available_markets: List<String>,
    val disc_number: Int,
    val duration_ms: Int,
    val explicit: Boolean,
    val external_urls: ExternalUrls,
    val href: String,
    val id: String,
    val is_local: Boolean,
    val name: String,
    val preview_url: String,
    val track_number: Int,
    val type: String,
    val uri: String
)