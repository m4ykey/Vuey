package com.example.vuey.data.local.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.util.Constants.ALBUM_TABLE_NAME
import com.google.gson.annotations.SerializedName
import java.time.LocalTime

@Entity(tableName = ALBUM_TABLE_NAME)
data class AlbumEntity(
    val saveAt: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = false)
    val mbid: String,
    val artist: String,
    val albumName: String,
    val image: List<Image>,
    val url: String,
    val listeners: String,
    val playcount: String,
    val tags: List<Tag>,
    val tracks: List<Track>,
    val wikiContent: String,
    val wikiSummary: String
) {
    data class Image(
        @SerializedName("#text")
        val image: String,
        val size: String
    )

    data class Tag(
        val name: String,
        val url: String
    )

    data class Track(
        @SerializedName("@attr")
        val attr: Attr,
        val artist: Artist,
        val duration: Int,
        @SerializedName("name")
        val trackName: String,
        val url: String
    ) {
        data class Attr(
            val rank: Int
        )
    }

    data class Artist(
        val mbid: String,
        val name: String,
        val url: String
    )
}

