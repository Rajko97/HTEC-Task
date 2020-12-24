package com.htec.task.repository.retrofit

import com.htec.task.model.network.PostNetworkModel
import retrofit2.Call
import retrofit2.http.GET

interface PostsApi {
    @GET("/posts")
    fun fetchAllPostsFeed() : Call<List<PostNetworkModel>>
}