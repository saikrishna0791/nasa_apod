package com.gs.apod.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_apods")
data class FavouriteApods(
    var isLiked : Boolean = false,
    val copyright: String?,
    val date: String,
    val explanation: String?,
    val hdurl: String,
    val media_type: String?,
    val service_version: String?,
    val title: String,
    @PrimaryKey(autoGenerate = false)
    val url: String
)