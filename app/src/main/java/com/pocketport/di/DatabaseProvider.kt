package com.pocketport.di

import android.content.Context
import androidx.room.Room
import com.pocketport.data.local.AppDatabase

object DatabaseProvider {
    private var database: AppDatabase? = null

    fun initialize(context: Context) {
        if (database == null) {
            database = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "pocket-port-db"
            ).build()
        }
    }

    fun getDatabase(): AppDatabase {
        return database ?: throw IllegalStateException("Database not initialized")
    }
}
