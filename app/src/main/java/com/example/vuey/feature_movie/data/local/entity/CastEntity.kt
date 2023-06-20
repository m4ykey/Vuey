package com.example.vuey.feature_movie.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.util.Constants.CAST_TABLE_NAME
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = CAST_TABLE_NAME)
data class CastEntity(
    @ColumnInfo("movieId") @PrimaryKey(autoGenerate = false) val movieId: Int,
    @ColumnInfo("moveCastList") val castList: List<CastListEntity>
) : Parcelable {

    @Parcelize
    data class CastListEntity(
        @ColumnInfo("castProfilePath") val castProfilePath: String?,
        @ColumnInfo("castName") val castName: String,
        @ColumnInfo("castId") val castId: Int
    ) : Parcelable
}