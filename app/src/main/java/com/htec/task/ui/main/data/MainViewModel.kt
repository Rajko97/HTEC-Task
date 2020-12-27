package com.htec.task.ui.main.data

import android.app.Application
import androidx.lifecycle.*
import com.htec.task.model.db.PostDBModel
import com.htec.task.model.network.AuthorsNetworkModel
import com.htec.task.repository.Repository
import com.htec.task.repository.PreferenceDataStore
import com.htec.task.repository.retrofit.ResultWrapper
import com.htec.task.repository.room.RoomPersistenceService
import com.htec.task.utils.Constants
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

class MainViewModel(application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<PostDBModel>>
    private val repository: Repository
    private val preferenceDataStore : PreferenceDataStore

    init {
        val postDao = RoomPersistenceService.invoke(application).postDao()
        repository = Repository(postDao)
        readAllData = repository.readAllData
        preferenceDataStore = PreferenceDataStore(application)

        viewModelScope.launch {
            val lastUpdateTime: Long = preferenceDataStore.readLastUpdateTime.first()
            val now = System.currentTimeMillis()
            if (TimeUnit.MILLISECONDS.toMinutes(now - lastUpdateTime) >= Constants.INVALIDATE_CACHE_TIME_IN_MINUTES) {
                fetchPostList {}
            }
        }
    }

    fun authorData(authorId : Int) : LiveData<ResultWrapper<AuthorsNetworkModel>> {
        return repository.fetchAuthorData(authorId)
    }

    fun cancelFetchingAuthorData() {
        repository.cancelFetchingAuthorData()
    }

    fun removePost(post : PostDBModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.removePost(post)
    }

    fun fetchByClient() = object : LiveData<Boolean>() {
        override fun onActive() {
            super.onActive()
            fetchPostList {
               viewModelScope.launch {
                   withContext(Main) {
                       postValue(it)
                   }
               }
            }
        }
    }

    private fun fetchPostList(callback: (Boolean) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        delay(5000)
         repository.fetchPostList { isSuccessfully ->
             if (isSuccessfully) {
                 storeLastUpdatedTime()
             }
             callback(isSuccessfully)
         }
     }

    private fun storeLastUpdatedTime() = viewModelScope.launch(Dispatchers.IO) {
        preferenceDataStore.saveCurrentTime()
    }
}