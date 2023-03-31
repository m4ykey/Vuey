package com.example.vuey.data.remote.response

import com.example.vuey.data.local.album.detail.*
import com.google.gson.annotations.SerializedName

data class AlbumDetailResponse(
    val album_group: String,
    val album_type: String,
    val artists: List<Artist>,
    val available_markets: List<String>,
    val copyrights: List<Copyright>,
    val external_ids: ExternalIds,
    val external_urls: ExternalUrls,
    val genres: List<Any>,
    val href: String,
    val id: String,
    val images: List<Image>,
    val label: String,
    @SerializedName("name")
    val albumName: String,
    val popularity: Int,
    val release_date: String,
    val release_date_precision: String,
    val total_tracks: Int,
    val tracks: Tracks,
    val type: String,
    val uri: String
)