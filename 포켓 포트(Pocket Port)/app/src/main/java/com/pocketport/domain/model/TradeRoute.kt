package com.pocketport.domain.model

data class TradeRoute(
    val id: String,
    val name: String,
    val distance: Int, // Affects travel time
    val baseRewardMultiplier: Float = 1.0f,
    val riskFactor: Float = 0.0f, // Chance of event/failure
    val requiredLevel: Int = 1,
    var isUnlocked: Boolean = false,
    var marketDemand: Map<ContainerType, Float> = mapOf() // Demand multiplier per type
)
