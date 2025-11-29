package com.pocketport.domain.system

import com.pocketport.domain.model.Factory
import com.pocketport.domain.model.Port
import com.pocketport.domain.model.Vehicle

class UpgradeSystem {

    fun upgradeFactory(port: Port, factoryId: String): Boolean {
        val factory = port.factories.find { it.id == factoryId } ?: return false
        val cost = calculateFactoryUpgradeCost(factory.level)
        
        if (port.gold >= cost) {
            port.gold -= cost
            // Create a new factory instance with incremented level
            // Since Factory is a data class, we replace it in the list
            val index = port.factories.indexOf(factory)
            port.factories[index] = factory.copy(level = factory.level + 1, storageCapacity = factory.storageCapacity + 5)
            return true
        }
        return false
    }

    fun upgradeVehicle(port: Port, vehicleId: String): Boolean {
        // Search in all vehicle lists
        val crane = port.cranes.find { it.id == vehicleId }
        if (crane != null) {
            return upgradeCrane(port, crane)
        }
        
        val truck = port.trucks.find { it.id == vehicleId }
        if (truck != null) {
            return upgradeTruck(port, truck)
        }
        
        val ship = port.ships.find { it.id == vehicleId }
        if (ship != null) {
            return upgradeShip(port, ship)
        }
        
        return false
    }

    private fun upgradeCrane(port: Port, crane: Vehicle): Boolean {
        val cost = calculateVehicleUpgradeCost(crane.level)
        if (port.gold >= cost) {
            port.gold -= cost
            crane.level++
            return true
        }
        return false
    }
    
    // Similar logic for Truck and Ship, simplified for now to share same cost logic
    private fun upgradeTruck(port: Port, truck: Vehicle): Boolean {
        val cost = calculateVehicleUpgradeCost(truck.level)
        if (port.gold >= cost) {
            port.gold -= cost
            truck.level++
            return true
        }
        return false
    }

    private fun upgradeShip(port: Port, ship: Vehicle): Boolean {
        val cost = calculateVehicleUpgradeCost(ship.level)
        if (port.gold >= cost) {
            port.gold -= cost
            ship.level++
            return true
        }
        return false
    }

    private fun calculateFactoryUpgradeCost(level: Int): Long {
        return (100 * Math.pow(1.5, level.toDouble())).toLong()
    }

    private fun calculateVehicleUpgradeCost(level: Int): Long {
        return (50 * Math.pow(1.4, level.toDouble())).toLong()
    }
}
