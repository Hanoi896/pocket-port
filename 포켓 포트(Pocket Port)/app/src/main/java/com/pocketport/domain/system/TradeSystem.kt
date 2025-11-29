package com.pocketport.domain.system

import com.pocketport.domain.model.ContainerType
import com.pocketport.domain.model.TradeRoute
import kotlin.random.Random

class TradeSystem {

    private val routes = mutableListOf<TradeRoute>()

    init {
        initializeRoutes()
    }

    private fun initializeRoutes() {
        routes.add(TradeRoute("route_local", "Local Market", 100, 1.0f, 0.0f, mapOf(ContainerType.GENERAL to 1.0f)))
        routes.add(TradeRoute("route_city", "City Center", 500, 1.5f, 0.1f, mapOf(ContainerType.ADVANCED to 1.2f)))
        routes.add(TradeRoute("route_neighbor", "Neighbor Port", 1200, 2.0f, 0.2f, mapOf(ContainerType.FROZEN to 1.5f)))
        routes.add(TradeRoute("route_capital", "Capital City", 3000, 3.5f, 0.3f, mapOf(ContainerType.DANGEROUS to 2.0f)))
        routes.add(TradeRoute("route_overseas", "Overseas Empire", 8000, 5.0f, 0.5f, mapOf(ContainerType.SPECIAL to 3.0f)))
        routes.add(TradeRoute("route_arctic", "Arctic Station", 15000, 8.0f, 0.7f, mapOf(ContainerType.FROZEN to 4.0f)))
        routes.add(TradeRoute("route_moon", "Lunar Base", 50000, 20.0f, 0.9f, mapOf(ContainerType.SPECIAL to 10.0f)))
    }

    fun getAvailableRoutes(): List<TradeRoute> {
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
