package com.pocketport.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pocketport.data.local.dao.PortDao
import com.pocketport.data.local.entity.PortEntity

@Database(entities = [PortEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun portDao(): PortDao
}
