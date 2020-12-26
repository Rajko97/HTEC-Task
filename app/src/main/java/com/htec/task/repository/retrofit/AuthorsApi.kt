package com.htec.task.repository.retrofit

import com.htec.task.model.network.AuthorsNetworkModel
import retrofit2.http.GET
import retrofit2.http.Path

interface AuthorsApi {
    @GET("/users/{id}")
    suspend fun fetchAuthorData(@Path("id") authorId : Int) : AuthorsNetworkModel
}