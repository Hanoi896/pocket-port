package com.pocketport.domain.model

data class Port(
    val factories: MutableList<Factory> = mutableListOf(),
    val warehouses: MutableList<Container> = mutableListOf(), // Simplified warehouse as a list of containers
    val cranes: MutableList<Crane> = mutableListOf(),
    val trucks: MutableList<Truck> = mutableListOf(),
    val ships: MutableList<Ship> = mutableListOf(),
    
    var gold: Long = 0,
    var diamonds: Int = 0,
    var experience: Long = 0,
    var level: Int = 1
)
