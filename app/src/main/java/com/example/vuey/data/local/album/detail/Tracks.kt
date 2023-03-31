package com.example.vuey.data.local.album.detail

data class Tracks(
    val href: String,
    val items: List<AlbumItem>,
    val limit: Int,
    val next: Any,
    val offset: Int,
    val previous: Any,
    val total: Int
)