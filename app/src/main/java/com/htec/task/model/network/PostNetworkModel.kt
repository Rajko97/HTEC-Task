package com.htec.task.model.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*[{
    "userId": 1,
    "id": 1,
    "title": "",
    "body": ""
  }]*/

data class PostNetworkModel(
    @SerializedName("userId")
    @Expose
    val ownerId : Int,

    @SerializedName("id")
    @Expose
    val postId : Int,

    @SerializedName("title")
    @Expose
    val title : String,

    @SerializedName("body")
    @Expose
    val body : String
)