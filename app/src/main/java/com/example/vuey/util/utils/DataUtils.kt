package com.example.vuey.util.utils

import com.example.vuey.feature_album.data.local.entity.AlbumEntity
import com.example.vuey.feature_album.data.remote.model.Album

fun AlbumEntity.toAlbum() : Album {
    return Album(
        albumType = this.albumType,
        albumName = this.albumName,
        id = this.id,
        totalTracks = this.totalTracks,
        artistList = this.artistList.map { artist ->
            Album.Artist(
                artistName = artist.name,
                id = artist.id,
                externalUrls = Album.ExternalUrls(
                    spotify = this.externalUrls.spotify
                )
            )
        },
        externalUrls = Album.ExternalUrls(
            spotify = this.externalUrls.spotify
        ),
        imageList = listOf(Album.Image(
            height = 640,
            width = 640,
            url = albumCover.url
        ))
    )
}

fun Album.toAlbumEntity() : AlbumEntity {
    return AlbumEntity(
        albumName = this.albumName,
        albumType = this.albumType,
        albumLength = 0,
        id = this.id,
        releaseDate = "",
        totalTracks = this.totalTracks,
        externalUrls = AlbumEntity.ExternalUrlsEntity(
            spotify = this.externalUrls.spotify
        ),
        albumCover = AlbumEntity.ImageEntity(
            height = 640,
            width = 640,
            url = ""
        )
    )
}