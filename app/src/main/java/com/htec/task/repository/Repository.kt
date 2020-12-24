package com.htec.task.repository

import androidx.lifecycle.LiveData
import com.htec.task.model.db.PostDBModel
import com.htec.task.model.network.AuthorsNetworkModel
import com.htec.task.repository.mapper.DataMapper
import com.htec.task.repository.retrofit.AuthorsApi
import com.htec.task.repository.retrofit.PostsApi
import com.htec.task.repository.retrofit.RetrofitService
import com.htec.task.repository.room.PostDao
import io.reactivex.Flowable

class Repository(private val postDao: PostDao) {
    private var postsApi = RetrofitService.getRetrofitClient().create(PostsApi::class.java)
    private var authorsApi = RetrofitService.getRetrofitClient().create(AuthorsApi::class.java)

    val readAllData: LiveData<List<PostDBModel>> = postDao.findAll()

    suspend fun removePost(post: PostDBModel) {
        postDao.deleteOne(post)
    }

    suspend fun fetchPostList() {
        val response = postsApi.fetchAllPostsFeed().execute()
        if(response.isSuccessful && response.body() != null) {
            val data = response.body()
            if (data != null) {
                insertMany(DataMapper.convertNetworkToDB(data))
            }
        }
    }

    fun fetchAuthorData(authorId: Int) : Flowable<AuthorsNetworkModel> {
        return authorsApi.fetchAllAuthorData(authorId)
    }

    private suspend fun insertMany(post: List<PostDBModel>) {
        postDao.insertMany(post)
    }
}