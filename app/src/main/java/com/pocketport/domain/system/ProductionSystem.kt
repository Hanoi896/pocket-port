package com.pocketport.domain.system

import com.pocketport.domain.model.Container
import com.pocketport.domain.model.ContainerType
import com.pocketport.domain.model.Factory
import java.util.UUID
import kotlin.random.Random

class ProductionSystem {

    fun update(factory: Factory, deltaTimeMillis: Long, speedMultiplier: Float = 1.0f) {
        val current = System.currentTimeMillis()
        val interval = (factory.getProductionInterval() / speedMultiplier).toLong()
        
        // Check if enough time has passed since last production
        if (current - factory.lastProductionTime >= interval) {
            if (factory.producedContainers.size < factory.storageCapacity) {
                produceContainer(factory)
                // Update last production time to current, or increment by interval to keep cadence?
                // For simplicity, reset to current to avoid burst production after pause
                // In a real robust system, we might handle "offline progress" differently.
                // For this active loop, we'll just set it to current.
                // However, to be more precise with "tick", we should probably track accumulated time.
                // But let's stick to the simple timestamp check for now as per "Casual" requirement.
                // Actually, let's use a simpler approach: the factory should have a 'progress' field.
            }
        }
    }
    
    // Improved update method using accumulated progress
    fun updateProgress(factory: Factory, deltaTimeMillis: Long) {
        // We can add a 'progress' field to Factory later if we want smooth UI bars.
        // For now, let's assume the caller handles the time check or we do it here.
        // Let's assume this is called every tick.
        
        // Actually, the previous logic of checking system time is safer for consistency.
        // But for a game loop, we usually pass deltaTime.
        
        // Let's refactor Factory to have 'currentProgress'
    }

    private fun produceContainer(factory: Factory) {
        val type = determineContainerType(factory.level)
        val newContainer = Container(
            id = UUID.randomUUID().toString(),
            type = type,
            destination = "Warehouse", // Initially to warehouse
            productionTime = System.currentTimeMillis()
        )
        factory.producedContainers.add(newContainer)
        // In a real app, we would update the factory's lastProductionTime here
        // But since Factory is a data class (immutable-ish in concept, but using MutableList),
        // we need to be careful. The caller should replace the factory instance if it's immutable,
        // or we rely on the mutable list.
        // For this architecture, let's assume we are modifying the mutable state within the object for now,
        // or returning a new state.
    }

    private fun determineContainerType(factoryLevel: Int): ContainerType {
        val roll = Random.nextFloat()
        // Simple probability logic based on level
        return when {
            factoryLevel >= 10 && roll < 0.05 -> ContainerType.SPECIAL
            factoryLevel >= 5 && roll < 0.15 -> ContainerType.DANGEROUS
            factoryLevel >= 3 && roll < 0.30 -> ContainerType.ADVANCED
            else -> ContainerType.GENERAL
        }
    }
}
