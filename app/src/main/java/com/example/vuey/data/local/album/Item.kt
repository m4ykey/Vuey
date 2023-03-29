package com.example.vuey.data.local.album

import com.google.gson.annotations.SerializedName

data class Item(
    val album_group: String,
    val album_type: String,
    val artists: List<Artist>,
    val available_markets: List<String>,
    val external_urls: ExternalUrls,
    val href: String,
    val id: String,
    val images: List<Image>,
    val is_playable: Boolean,
    @SerializedName("name")
    val albumName: String,
    val release_date: String,
    val release_date_precision: String,
    val total_tracks: Int,
    val type: String,
    val uri: String
)