package com.example.vuey.feature_album.data.remote.model

import com.google.gson.annotations.SerializedName

data class SpotifyOAuthResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("expires_in")
    val expiresIn: Int
)
