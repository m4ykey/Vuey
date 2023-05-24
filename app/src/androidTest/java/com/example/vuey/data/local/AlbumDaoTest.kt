package com.example.vuey.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.vuey.feature_album.data.database.dao.AlbumDao
import com.example.vuey.feature_album.data.database.entity.AlbumEntity
import com.example.vuey.getOrAwaitValue
import com.example.vuey.util.database.VueyDatabase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class AlbumDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database : VueyDatabase
    private lateinit var dao : AlbumDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            VueyDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.albumDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAlbum() = runTest {
        val albumItem = AlbumEntity(
            albumName = "MM...FOOD",
            albumType = "Album",
            id = "albumId",
            albumLength = "23",
            releaseDate = "12 April 1999",
            totalTracks = 21,
            externalUrls = AlbumEntity.ExternalUrlsEntity(
                spotify = "random link"
            )
        )
        dao.insertAlbum(albumItem)

        val allAlbums = dao.getAllAlbums().getOrAwaitValue()

        assertThat(allAlbums).contains(albumItem)
    }

    @Test
    fun deleteAlbum() = runTest {
        val albumItem = AlbumEntity(
            albumName = "MM...FOOD",
            albumType = "Album",
            id = "albumId",
            albumLength = "23",
            releaseDate = "12 April 1999",
            totalTracks = 21,
            externalUrls = AlbumEntity.ExternalUrlsEntity(
                spotify = "random link"
            )
        )
        dao.insertAlbum(albumItem)
        dao.deleteAlbum(albumItem)

        val allAlbums = dao.getAllAlbums().getOrAwaitValue()

        assertThat(allAlbums).doesNotContain(albumItem)
    }

}