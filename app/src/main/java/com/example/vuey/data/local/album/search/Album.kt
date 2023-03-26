package com.example.vuey.data.local.album.search

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.util.Constants.ALBUM_TABLE_NAME
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(
    val artist: String,
    val image: List<Image>,
    @PrimaryKey(autoGenerate = false)
    val mbid: String,
    @SerializedName("name")
    val albumName: String,
    val streamable: String,
    val url: String
) : Parcelable {
    @Parcelize
    data class Image(
        @SerializedName("#text")
        val image: String,
        val size: String
    ) : Parcelable
}
