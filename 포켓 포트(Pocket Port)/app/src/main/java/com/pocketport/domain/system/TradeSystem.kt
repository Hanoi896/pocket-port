package com.pocketport.domain.system

import com.pocketport.domain.model.ContainerType
import com.pocketport.domain.model.TradeRoute
import kotlin.random.Random

class TradeSystem {

    private val routes = mutableListOf<TradeRoute>()
        return routes.filter { it.isUnlocked }
    }

    fun unlockRoute(routeId: String, playerLevel: Int): Boolean {
        val route = routes.find { it.id == routeId }
        if (route != null && playerLevel >= route.requiredLevel) {
            route.isUnlocked = true
            return true
        }
        return false
    }

    fun updateMarketTrends() {
        // Randomize demand for each route
        routes.forEach { route ->
            val newDemand = mutableMapOf<ContainerType, Float>()
            ContainerType.values().forEach { type ->
                // Demand fluctuates between 0.8x and 1.5x
                newDemand[type] = 0.8f + Random.nextFloat() * 0.7f
            }
            route.marketDemand = newDemand
        }
    }
    
    fun calculateTradeValue(route: TradeRoute, containerType: ContainerType): Long {
        val demand = route.marketDemand[containerType] ?: 1.0f
        return (containerType.baseValue * route.baseRewardMultiplier * demand).toLong()
    }
}
