package com.pocketport.domain.model

enum class ArtifactType {
    PRODUCTION_SPEED,
    STORAGE_CAPACITY,
    MOVE_SPEED,
    TRADE_REWARD
}

data class Artifact(
    val id: String,
    val name: String,
    val type: ArtifactType,
    val multiplier: Float, // e.g., 1.1 for +10%
    val description: String,
    var isUnlocked: Boolean = false
)
