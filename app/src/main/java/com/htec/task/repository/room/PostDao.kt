package com.htec.task.repository.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.htec.task.model.db.PostDBModel

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(posts: List<PostDBModel>)

    @Query("SELECT * FROM posts_table ORDER BY postId ASC")
    fun findAll(): LiveData<List<PostDBModel>>

    @Delete
    suspend fun deleteOne(post: PostDBModel)
}