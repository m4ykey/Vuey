package com.example.vuey.feature_album.data.remote.model

import com.google.gson.annotations.SerializedName

data class Artist(
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls,
    val id: String,
    @SerializedName("name")
    val artistName: String
)