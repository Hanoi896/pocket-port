package com.pocketport.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pocketport.data.local.entity.PortEntity

@Dao
interface PortDao {
    @Query("SELECT * FROM port_state WHERE id = 0")
    suspend fun getPortState(): PortEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePortState(portEntity: PortEntity)
}
