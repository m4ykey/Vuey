package com.example.vuey.util.utils

import com.example.vuey.feature_album.data.remote.model.Album
import com.example.vuey.feature_album.data.local.entity.AlbumEntity
import com.example.vuey.feature_movie.data.remote.model.SearchMovie
import com.example.vuey.feature_movie.data.local.entity.MovieEntity
import com.example.vuey.feature_tv_show.data.api.search.SearchTvShow
import com.example.vuey.feature_tv_show.data.database.entity.TvShowEntity

fun SearchTvShow.toTvShowEntity(): TvShowEntity {
    return TvShowEntity(
        id = this.id,
        tvShowStatus = "",
        tvShowOverview = this.overview,
        tvShowName = this.name,
        tvShowBackdropPath = this.backdrop_path.toString(),
        tvShowPosterPath = this.poster_path.toString(),
        tvShowVoteAverage = this.vote_average.formatVoteAverage(),
        tvShowFirstAirDate = this.first_air_date,
        tvShowEpisodeRun = 0
    )
}

fun TvShowEntity.toSearchTv(): SearchTvShow {
    return SearchTvShow(
        backdrop_path = this.tvShowBackdropPath,
        first_air_date = this.tvShowFirstAirDate,
        id = this.id,
        name = this.tvShowName,
        overview = this.tvShowOverview,
        popularity = 0.0,
        poster_path = this.tvShowPosterPath,
        vote_average = this.tvShowVoteAverage.replace(",", ".").toDouble(),
    )
}

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

fun SearchMovie.toMovieEntity() : MovieEntity {
    return MovieEntity(
        id = this.id,
        movieOverview = this.overview,
        movieBackdropPath = this.backdrop_path.toString(),
        movieRuntime = 0,
        moviePosterPath = this.poster_path.toString(),
        movieTitle = this.title,
        movieVoteAverage = this.vote_average.formatVoteAverage(),
        movieReleaseDate = DateUtils.formatAirDate(this.release_date).toString()
    )
}

fun MovieEntity.toSearchMovie() : SearchMovie {
    return SearchMovie(
        backdrop_path = this.movieBackdropPath,
        id = this.id,
        overview = this.movieOverview,
        poster_path = this.moviePosterPath,
        release_date = this.movieReleaseDate,
        title = this.movieTitle,
        vote_average = this.movieVoteAverage.replace(",", ".").toDouble()
    )
}