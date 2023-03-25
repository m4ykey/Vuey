package com.example.vuey.data.local.album.detail

import com.google.gson.annotations.SerializedName

data class AlbumDetail(
    val artist: String,
    val image: List<Image>,
    val listeners: String,
    val mbid: String,
    val name: String,
    val playcount: String,
    val tags: Tags,
    val tracks: Tracks,
    val url: String,
    val wiki: Wiki
) {
    data class Image(
        @SerializedName("#text")
        val image: String,
        val size: String
    )
}

data class Tags(
    val tag: List<Tag>
) {
    data class Tag(
        val name: String,
        val url: String
    )
}

data class Tracks(
    val track: List<Track>
) {
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
}

data class Wiki(
    val content: String,
    val published: String,
    val summary: String
)