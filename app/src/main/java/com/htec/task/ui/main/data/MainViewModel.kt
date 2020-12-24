package com.htec.task.ui.main.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.htec.task.model.db.PostDBModel
import com.htec.task.model.network.AuthorsNetworkModel
import com.htec.task.repository.Repository
import com.htec.task.repository.room.RoomPersistenceService
import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

private const val KEY_LAST_UPDATE = "lastUpdateMillis"

class MainViewModel(application: Application): AndroidViewModel(application) {
    val readAllData: LiveData<List<PostDBModel>>
    private val repository: Repository

    init {
        val postDao = RoomPersistenceService.getDatabase(application).postDao()
        repository = Repository(postDao)
        readAllData = repository.readAllData
        val sharedPreferences = getApplication<Application>().getSharedPreferences("htec-test", Context.MODE_PRIVATE)
        val lastUpdateTime = sharedPreferences.getLong(KEY_LAST_UPDATE, 0)
        val now = System.currentTimeMillis()
        if (TimeUnit.MILLISECONDS.toMinutes(now - lastUpdateTime) >= 5) {
            storeLastUpdatedTime()
            viewModelScope.launch(Dispatchers.IO) {
                repository.fetchPostList()
            }
        }
    }

    fun fetchAuthorData(authorId : Int) : Flowable<AuthorsNetworkModel> {
        return repository.fetchAuthorData(authorId)
    }

    fun removePost(post : PostDBModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removePost(post)
        }
    }

    fun fetchPostList() {
        storeLastUpdatedTime()
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchPostList()
        }
    }

    private fun storeLastUpdatedTime() {
        val sp = getApplication<Application>().getSharedPreferences("htec-test", Context.MODE_PRIVATE)
        sp.edit().putLong(KEY_LAST_UPDATE, System.currentTimeMillis()).apply()
    }
}