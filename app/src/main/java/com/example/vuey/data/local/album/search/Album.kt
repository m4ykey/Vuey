package com.example.vuey.data.local.album.search

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(
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
) : Parcelable {
    @Parcelize
    data class Artist(
        val external_urls: ExternalUrls,
        val href: String,
        val id: String,
        val name: String,
        val type: String,
        val uri: String
    ) : Parcelable

    @Parcelize
    data class ExternalUrls(
        val spotify: String
    ) : Parcelable

    @Parcelize
    data class Image(
        val height: Int,
        val url: String,
        val width: Int
    ) : Parcelable
}