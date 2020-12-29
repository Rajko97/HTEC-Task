package com.htec.task.model.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "posts_table")
data class PostDBModel(
    @PrimaryKey()
    @ColumnInfo(name = "postId")
    val postId : Int,

    @ColumnInfo(name = "ownerId")
    val ownerId : Int,

    @ColumnInfo(name = "title")
    val title : String,

    @ColumnInfo(name = "body")
    val body : String
) : Parcelable