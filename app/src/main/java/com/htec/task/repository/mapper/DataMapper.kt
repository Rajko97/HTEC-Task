package com.htec.task.repository.mapper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.htec.task.model.db.PostDBModel
import com.htec.task.model.network.PostNetworkModel
import com.rajko.stefanexpress.repository.mapper.Mapper
import com.rajko.stefanexpress.repository.mapper.NullableInputListMapperImpl

object DataMapper {
        fun convertNetworkToDB(data : List<PostNetworkModel>) : List<PostDBModel> {
           return NullableInputListMapperImpl(object :
               Mapper<PostNetworkModel, PostDBModel> {
               override fun map(input: PostNetworkModel): PostDBModel  {
                   return PostDBModel(
                        input.postId,
                        input.ownerId,
                        input.title,
                        input.body
                   )
               }
           }).map(data)
        }
}