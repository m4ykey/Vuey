package com.example.vuey.data.local.album.search

data class Albums(
    val href: String,
    val items: List<Album>,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: Any,
    val total: Int
)