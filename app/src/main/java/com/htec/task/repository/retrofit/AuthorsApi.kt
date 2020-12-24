package com.htec.task.repository.retrofit

import com.htec.task.model.network.AuthorsNetworkModel
import com.htec.task.model.network.PostNetworkModel
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface AuthorsApi {
    @GET("/users/{id}")
    fun fetchAllAuthorData(@Path("id") authorId : Int) : Flowable<AuthorsNetworkModel>
}