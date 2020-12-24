package com.htec.task.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.htec.task.model.db.PostDBModel

@Database(entities = [PostDBModel::class], version = 1, exportSchema = false)
abstract class RoomPersistenceService : RoomDatabase() {

    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var INSTANCE: RoomPersistenceService? = null

        fun getDatabase(context: Context): RoomPersistenceService{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomPersistenceService::class.java,
                    "database-htec-task"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}