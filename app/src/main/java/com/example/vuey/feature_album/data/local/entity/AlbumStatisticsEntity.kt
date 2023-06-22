package com.example.vuey.feature_album.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.util.Constants.STATISTICS_ALBUM_TABLE_NAME
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = STATISTICS_ALBUM_TABLE_NAME)
data class AlbumStatisticsEntity(
    @ColumnInfo(name = "statisticsId") @PrimaryKey(autoGenerate = false) val id : String,
    @ColumnInfo(name = "statisticsArtistList") val artistList : List<ArtistStatisticsEntity>,
    @ColumnInfo(name = "statisticsAlbumLength") val albumLength : Int,
    @ColumnInfo(name = "statisticsTotalTracks") val totalTracks : Int
) : Parcelable {

    @Parcelize
    data class ArtistStatisticsEntity(
        @ColumnInfo("artistName") val artistName : String
    ) : Parcelable
}
