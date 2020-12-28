package com.htec.task.repository.retrofit

import com.htec.task.utils.Constants
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {
    companion object {
        @Volatile
        private var INSTANCE: Retrofit? = null

        operator fun invoke(): Retrofit = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildRetrofit().also { INSTANCE = it }
        }

        private fun buildRetrofit() =
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()
                ).build()
    }
}
