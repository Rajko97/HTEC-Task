package com.htec.task.repository.room

import androidx.paging.PagingSource
import androidx.room.*
import com.htec.task.model.db.PostDBModel

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(posts: List<PostDBModel>)

    @Query("SELECT * FROM posts_table ORDER BY postId ASC")
    fun findAll(): PagingSource<Int, PostDBModel>

    @Delete
    fun deleteOne(post: PostDBModel)

//    ForTesting
//    @Query("DELETE FROM posts_table")
//    fun deleteAll()
}