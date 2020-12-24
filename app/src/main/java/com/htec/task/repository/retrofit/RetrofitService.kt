package com.htec.task.repository.retrofit

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://jsonplaceholder.typicode.com"

class RetrofitService {
    companion object {
        @Volatile
        private var INSTANCE: Retrofit? = null

        fun getRetrofitClient(): Retrofit {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }

            synchronized(this) {
                val instance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create()
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
