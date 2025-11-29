package com.pocketport.domain.system

import com.pocketport.domain.model.*

class TransportSystem {

    fun update(port: Port, deltaTimeMillis: Long, moveSpeedMultiplier: Float = 1.0f) {
        updateCranes(port, deltaTimeMillis) // Cranes usually static speed or separate buff
        updateTrucks(port, deltaTimeMillis) // Trucks could use buff
        updateShips(port, deltaTimeMillis, moveSpeedMultiplier)
    }

    private fun updateCranes(port: Port, deltaTimeMillis: Long) {
        port.cranes.forEach { crane ->
            when (crane.state) {
                VehicleState.IDLE -> {
                    // Find a factory with containers
                    val factoryWithCargo = port.factories.firstOrNull { it.producedContainers.isNotEmpty() }
                    if (factoryWithCargo != null) {
                        crane.state = VehicleState.LOADING
                        // In a real game, we'd assign a target here
                    }
                }
                VehicleState.LOADING -> {
                    // Simulate loading time
                    // For now, instant load for simplicity or check a timer
                    val factory = port.factories.firstOrNull { it.producedContainers.isNotEmpty() }
                    if (factory != null) {
                        val container = factory.producedContainers.removeAt(0)
                        // Move to warehouse or truck
                        // Simplified: Move directly to warehouse
                        port.warehouses.add(container)
                        crane.state = VehicleState.UNLOADING
                    } else {
                        crane.state = VehicleState.IDLE
                    }
                }
                VehicleState.UNLOADING -> {
                    // Simulate unload
                    crane.state = VehicleState.IDLE
                }
                else -> {}
            }
        }
    }

    private fun updateTrucks(port: Port, deltaTimeMillis: Long) {
        port.trucks.forEach { truck ->
            when (truck.state) {
                VehicleState.IDLE -> {
                    if (port.warehouses.isNotEmpty()) {
                        truck.state = VehicleState.LOADING
                    }
                }
                VehicleState.LOADING -> {
                    if (port.warehouses.isNotEmpty() && truck.currentLoad.size < truck.capacity) {
                        val container = port.warehouses.removeAt(0)
                        truck.currentLoad.add(container)
                        if (truck.currentLoad.size >= truck.capacity) {
                            truck.state = VehicleState.MOVING // Moving to ship
                        }
                    } else if (truck.currentLoad.isNotEmpty()) {
                         truck.state = VehicleState.MOVING
                    } else {
                        truck.state = VehicleState.IDLE
                    }
                }
                VehicleState.MOVING -> {
                    // Simulate travel to ship
                    // For now, instant arrival
                    truck.state = VehicleState.UNLOADING
                }
                VehicleState.UNLOADING -> {
                    // Find a ship that needs cargo
                    val targetShip = port.ships.firstOrNull { it.state == VehicleState.IDLE || it.state == VehicleState.LOADING }
                    if (targetShip != null && targetShip.currentLoad.size < targetShip.capacity) {
                        truck.currentLoad.forEach { container ->
                            if (targetShip.currentLoad.size < targetShip.capacity) {
                                targetShip.currentLoad.add(container)
                            } else {
                                // Ship full, return to warehouse? 
                                // For simplicity, assume infinite ship capacity or wait
                                // This logic needs to be robust in real code
                            }
                        }
                        truck.currentLoad.clear()
                        truck.state = VehicleState.RETURNING
                    }
                }
                VehicleState.RETURNING -> {
                    truck.state = VehicleState.IDLE
                }
                else -> {}
            }
        }
    }

    private fun updateShips(port: Port, deltaTimeMillis: Long, speedMultiplier: Float) {
        port.ships.forEach { ship ->
            when (ship.state) {
                VehicleState.IDLE -> {
                    if (ship.currentLoad.size >= ship.capacity) {
                        ship.state = VehicleState.MOVING // Depart
                        ship.remainingTime = (10000 / speedMultiplier).toLong() // 10 seconds trip / speed
                    }
                }
                VehicleState.MOVING -> {
                    ship.remainingTime -= deltaTimeMillis
                    if (ship.remainingTime <= 0) {
                        ship.state = VehicleState.UNLOADING // Arrived at destination / Returned
                        // In reality: Arrive -> Unload -> Return -> Arrive Home
                        // Simplified: Trip complete, get money
                        completeTrade(port, ship)
                        ship.currentLoad.clear()
                        ship.state = VehicleState.RETURNING
                        ship.remainingTime = 5000 // 5 sec return
                    }
                }
                VehicleState.RETURNING -> {
                    ship.remainingTime -= deltaTimeMillis
                    if (ship.remainingTime <= 0) {
                        ship.state = VehicleState.IDLE
                    }
                }
                else -> {}
            }
        }
    }

    private fun completeTrade(port: Port, ship: Ship) {
        // Calculate earnings
        var totalValue = 0L
        ship.currentLoad.forEach { container ->
            totalValue += container.type.baseValue
        }
        port.gold += totalValue
        port.experience += 10
    }
}
