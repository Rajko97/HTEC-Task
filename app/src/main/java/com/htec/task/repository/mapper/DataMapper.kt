package com.htec.task.repository.mapper
import com.htec.task.model.db.PostDBModel
import com.htec.task.model.network.PostNetworkModel

object DataMapper {
        fun convertNetworkToDB(data : List<PostNetworkModel>) : List<PostDBModel> {
            return ListMapperImpl(object :
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