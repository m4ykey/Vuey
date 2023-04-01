package com.example.vuey.data.local.album.search

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(
    val album_type: String,
    val artists: List<Artist>,
    val external_urls: ExternalUrls,
    val id: String,
    val images: List<Image>,
    @SerializedName("name")
    val albumName: String,
    val total_tracks: Int
) : Parcelable {
    @Parcelize
    data class Artist(
        val external_urls: ExternalUrls,
        val id: String,
        val name: String,
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