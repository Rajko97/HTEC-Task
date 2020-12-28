package com.htec.task.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.htec.task.model.db.PostDBModel
import com.htec.task.model.network.AuthorsNetworkModel
import com.htec.task.model.network.PostNetworkModel
import com.htec.task.repository.mapper.DataMapper
import com.htec.task.repository.retrofit.*
import com.htec.task.repository.room.PostDao
import com.htec.task.utils.Constants
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.Response
import java.util.concurrent.TimeUnit

class Repository(private val postDao: PostDao) {
    private var postsApi = RetrofitService.invoke().create(PostsApi::class.java)
    private var authorsApi = RetrofitService.invoke().create(AuthorsApi::class.java)

    private var fetchAuthorDataJob : CompletableJob? = null

    val readAllData = Pager(PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 60)) { postDao.findAll()}.liveData

    fun removePost(post: PostDBModel) {
       postDao.deleteOne(post)
        //postDao.deleteAll() //for testing
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun fetchPostList(callback: (Boolean) -> Unit) {
        lateinit var response : Response<List<PostNetworkModel>>
        try {
            response = postsApi.fetchAllPostsFeed().execute()
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                insertMany(DataMapper.convertNetworkToDB(data))
                callback(true)
                return
            }
        } catch(exception : Exception) {

        }
        callback(false)
    }

    private var cachedAuthorId : Int? = null
    private var cachedAuthorData : ResultWrapper<AuthorsNetworkModel>? = null
    fun fetchAuthorData(authorId: Int) : LiveData<ResultWrapper<AuthorsNetworkModel>> {
        if(authorId == cachedAuthorId) {
            return MutableLiveData(cachedAuthorData)
        }
        fetchAuthorDataJob = Job()
        return object: LiveData<ResultWrapper<AuthorsNetworkModel>>() {
            override fun onActive() {
                super.onActive()
                fetchAuthorDataJob?.let { job ->
                    CoroutineScope(IO+job).launch {
                        val networkJob = withTimeoutOrNull(TimeUnit.SECONDS.toMillis(Constants.NETWORK_TIME_OUT_TIME_IN_SECONDS.toLong())) {
                            val response = NetworkHelper.safeApiCall(Dispatchers.IO) {
                                authorsApi.fetchAuthorData(authorId)
                            }
                            if(response is ResultWrapper.Success) {
                                cachedAuthorId = authorId
                                cachedAuthorData = response
                            }
                            withContext(Main) {
                                value = response
                                fetchAuthorDataJob?.complete()
                            }
                        }
                        if(networkJob == null) {
                            withContext(Main) {
                                value = ResultWrapper.NetworkError
                                fetchAuthorDataJob?.complete()
                            }
                        }
                    }
                }
            }
        }
    }

    fun cancelFetchingAuthorData() {
        fetchAuthorDataJob?.cancel()
    }

    private suspend fun insertMany(post: List<PostDBModel>) {
        postDao.insertMany(post)
    }
}