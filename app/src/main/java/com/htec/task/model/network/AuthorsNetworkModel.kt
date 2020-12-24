package com.htec.task.model.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AuthorsNetworkModel(
    @SerializedName("name")
    @Expose
    val fullName : String,

    @SerializedName("email")
    @Expose
    val email : String
)