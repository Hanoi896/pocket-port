package com.pocketport.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "port_state")
data class PortEntity(
    @PrimaryKey val id: Int = 0, // Single row for single save slot
    val gold: Long,
    val diamonds: Int,
    val experience: Long,
    val level: Int,
    val factoriesJson: String, // Stored as JSON for simplicity
    val vehiclesJson: String, // Stored as JSON
    val tradeRoutesJson: String // Stored as JSON
)
