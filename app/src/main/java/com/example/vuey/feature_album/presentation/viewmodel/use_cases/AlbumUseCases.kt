package com.example.vuey.feature_album.presentation.viewmodel.use_cases

data class AlbumUseCases(
    val getAlbumSearchUseCase: AlbumSearchUseCase,
    val getAlbumDetailUseCase : AlbumDetailUseCase,
    val getAlbumArtistUseCase: AlbumArtistUseCase,
    val getArtistUseCase: ArtistUseCase,
    val getArtistTopTracksUseCase: ArtistTopTracksUseCase
)