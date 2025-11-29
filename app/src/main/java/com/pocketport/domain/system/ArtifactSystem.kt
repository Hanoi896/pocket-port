package com.pocketport.domain.system

import com.pocketport.domain.model.Artifact
import com.pocketport.domain.model.ArtifactType

class ArtifactSystem {
    private val artifacts = mutableListOf<Artifact>()

    init {
        initializeArtifacts()
    }

    private fun initializeArtifacts() {
        artifacts.add(Artifact("art_gear", "Golden Gear", ArtifactType.PRODUCTION_SPEED, 1.2f, "Production Speed +20%"))
        artifacts.add(Artifact("art_anchor", "Iron Anchor", ArtifactType.MOVE_SPEED, 1.1f, "Ship Speed +10%"))
        artifacts.add(Artifact("art_chest", "Ancient Chest", ArtifactType.STORAGE_CAPACITY, 1.5f, "Storage +50%"))
        artifacts.add(Artifact("art_coin", "Lucky Coin", ArtifactType.TRADE_REWARD, 1.3f, "Trade Reward +30%"))
        
        // New Artifacts
        artifacts.add(Artifact("art_oil", "Premium Oil", ArtifactType.PRODUCTION_SPEED, 1.5f, "Production Speed +50%"))
        artifacts.add(Artifact("art_sail", "Wind Sail", ArtifactType.MOVE_SPEED, 1.3f, "Ship Speed +30%"))
        artifacts.add(Artifact("art_gem", "Mystic Gem", ArtifactType.TRADE_REWARD, 2.0f, "Trade Reward +100%"))
        artifacts.add(Artifact("art_robot", "Auto Bot", ArtifactType.PRODUCTION_SPEED, 2.0f, "Production Speed +100%"))
    }

    fun getArtifacts(): List<Artifact> {
        return artifacts
    }

    fun unlockArtifact(artifactId: String) {
        artifacts.find { it.id == artifactId }?.isUnlocked = true
    }

    fun getMultiplier(type: ArtifactType): Float {
        // Sum of all unlocked artifacts of this type
        // Or product? Usually product for multipliers.
        var multiplier = 1.0f
        artifacts.filter { it.isUnlocked && it.type == type }.forEach {
            multiplier *= it.multiplier
        }
        return multiplier
    }
}
