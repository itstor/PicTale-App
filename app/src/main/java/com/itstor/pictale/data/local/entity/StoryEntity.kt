package com.itstor.pictale.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "stories")
@Parcelize
data class StoryEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "photo_url")
    var photoUrl: String,

    @ColumnInfo(name = "lon")
    var lon: Double? = null,

    @ColumnInfo(name = "lat")
    var lat: Double? = null,

    @ColumnInfo(name = "created_at")
    var createdAt: String,
) : Parcelable