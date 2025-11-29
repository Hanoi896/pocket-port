package com.pocketport.domain.model

enum class VehicleState {
    IDLE, MOVING, LOADING, UNLOADING, RETURNING, MAINTENANCE
}

abstract class Vehicle(
    open val id: String,
    open var level: Int,
    open var state: VehicleState = VehicleState.IDLE
)

data class Crane(
    override val id: String,
    override var level: Int = 1,
    val liftSpeed: Float = 1.0f
) : Vehicle(id, level)

data class Truck(
    override val id: String,
    override var level: Int = 1,
    val speed: Float = 1.0f,
    val capacity: Int = 1,
    val currentLoad: MutableList<Container> = mutableListOf()
) : Vehicle(id, level)

data class Ship(
    override val id: String,
    override var level: Int = 1,
    val capacity: Int = 10,
    val speed: Float = 1.0f,
    val currentLoad: MutableList<Container> = mutableListOf(),
    var destinationRouteId: String? = null,
    var remainingTime: Long = 0L
) : Vehicle(id, level)
