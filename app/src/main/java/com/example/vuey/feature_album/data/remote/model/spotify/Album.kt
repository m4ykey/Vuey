package com.example.vuey.feature_album.data.remote.model.spotify

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(
    @SerializedName("album_type")
    val albumType: String,
    @SerializedName("artists")
    val artistList: List<Artist>,
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls,
    val id: String,
    @SerializedName("images")
    val imageList: List<Image>,
    @SerializedName("name")
    val albumName: String,
    @SerializedName("total_tracks")
    val totalTracks: Int
) : Parcelable {

    @Parcelize
    data class Artist(
        @SerializedName("external_urls")
        val externalUrls: ExternalUrls,
        val id: String,
        @SerializedName("name")
        val artistName: String,
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