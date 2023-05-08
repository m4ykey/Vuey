package com.example.vuey.util.utils

import com.example.vuey.feature_album.data.api.search.Album
import com.example.vuey.feature_album.data.database.entity.AlbumEntity
import com.example.vuey.feature_movie.data.api.search.SearchMovie
import com.example.vuey.feature_movie.data.database.entity.MovieEntity
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
        album_type = this.albumType,
        albumName = this.albumName,
        id = this.id,
        total_tracks = this.totalTracks,
        artists = this.artistList.map { artist ->
            Album.Artist(
                name = artist.name,
                id = artist.id,
                external_urls = Album.ExternalUrls(
                    spotify = this.externalUrls.spotify
                )
            )
        },
        images = this.imageList.map { image ->
            Album.Image(
                height = image.height,
                width = image.width,
                url = image.url
            )
        },
        external_urls = Album.ExternalUrls(
            spotify = this.externalUrls.spotify
        )
    )
}

fun Album.toAlbumEntity() : AlbumEntity {
    return AlbumEntity(
        albumName = this.albumName,
        albumType = this.album_type,
        albumLength = "",
        id = this.id,
        release = "",
        totalTracks = this.total_tracks,
        externalUrls = AlbumEntity.ExternalUrlsEntity(
            spotify = this.external_urls.spotify
        )
    )
}

fun SearchMovie.toMovieEntity() : MovieEntity {
    return MovieEntity(
        id = this.id,
        movieOverview = this.overview,
        movieBackdropPath = this.backdrop_path.toString(),
        movieRuntime = "",
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