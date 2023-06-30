package com.example.vuey.feature_album.data.remote.model.spotify.artist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArtistData(
    val name : String = "",
    val content : String = "",
    val followers : String = "",
    val listeners : String = ""
) : Parcelable
