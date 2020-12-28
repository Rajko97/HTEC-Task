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

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                RoomPersistenceService::class.java,
                "database-htec-task"
            ).build()
    }
}