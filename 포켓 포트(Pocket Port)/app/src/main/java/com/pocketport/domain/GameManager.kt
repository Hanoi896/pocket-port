package com.pocketport.domain

import com.pocketport.domain.model.*
import com.pocketport.domain.system.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class GameManager {
    private val _portState = MutableStateFlow(Port())
    val portState: StateFlow<Port> = _portState.asStateFlow()

    private val productionSystem = ProductionSystem()
    private val transportSystem = TransportSystem()
    private val tradeSystem = TradeSystem()
    private val upgradeSystem = UpgradeSystem()
    val puzzleManager = com.pocketport.domain.system.PuzzleManager()
    val artifactSystem = com.pocketport.domain.system.ArtifactSystem()
    // BillingManager needs context, but GameManager is pure domain. 
    // Usually injected. For now, we'll expose a method to set VIP status or handle it in ViewModel.
    // But GameManager needs to know VIP status for logic (speed buff).
    var isVip = false

    private var gameJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        // initializeGame() // Don't init immediately, wait for load
    }

    fun loadGame() {
        scope.launch {
            try {
                val dao = com.pocketport.di.DatabaseProvider.getDatabase().portDao()
                val savedState = dao.getPortState()
                
                if (savedState != null) {
                    val gson = com.google.gson.Gson()
                    val factoriesType = object : com.google.gson.reflect.TypeToken<MutableList<Factory>>() {}.type
                    val cranesType = object : com.google.gson.reflect.TypeToken<MutableList<Crane>>() {}.type
                    val trucksType = object : com.google.gson.reflect.TypeToken<MutableList<Truck>>() {}.type
                    val shipsType = object : com.google.gson.reflect.TypeToken<MutableList<Ship>>() {}.type
                    
                    // Simple parsing for now. In real app, handle Vehicle polymorphism carefully.
                    // For prototype, assuming specific lists or using a wrapper.
                    // Actually, Vehicle is abstract. Gson needs help with polymorphism or we store separate lists.
                    // Let's assume we store separate lists in JSON for simplicity in PortEntity logic, 
                    // but PortEntity has 'vehiclesJson'. Let's split it or use a wrapper object.
                    // To keep it simple: PortEntity stores 'factoriesJson', 'cranesJson', 'trucksJson', 'shipsJson' would be better.
                    // But I defined 'vehiclesJson'. Let's assume it stores a wrapper object 'VehicleSaveData'.
                    
                    // Re-reading PortEntity definition... it has 'vehiclesJson'.
                    // Let's refactor PortEntity to be more specific or parse a wrapper.
                    // Wrapper:
                    data class VehicleWrapper(
                        val cranes: MutableList<Crane>,
                        val trucks: MutableList<Truck>,
                        val ships: MutableList<Ship>
                    )
                    
                    val wrapper = gson.fromJson<VehicleWrapper>(savedState.vehiclesJson, object : com.google.gson.reflect.TypeToken<VehicleWrapper>() {}.type)
                    
                    val loadedPort = Port(
                        factories = gson.fromJson(savedState.factoriesJson, factoriesType),
                        cranes = wrapper.cranes,
                        trucks = wrapper.trucks,
                        ships = wrapper.ships,
                        gold = savedState.gold,
                        diamonds = savedState.diamonds,
                        experience = savedState.experience,
                        level = savedState.level
                    )
                    _portState.value = loadedPort
                } else {
                    initializeGame()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                initializeGame() // Fallback
            }
        }
    }

    fun saveGame() {
        scope.launch {
            val current = _portState.value
            val gson = com.google.gson.Gson()
            
            data class VehicleWrapper(
                val cranes: MutableList<Crane>,
                val trucks: MutableList<Truck>,
                val ships: MutableList<Ship>
            )
            val wrapper = VehicleWrapper(current.cranes, current.trucks, current.ships)
            
            val entity = com.pocketport.data.local.entity.PortEntity(
                id = 0,
                gold = current.gold,
                diamonds = current.diamonds,
                experience = current.experience,
                level = current.level,
                factoriesJson = gson.toJson(current.factories),
                vehiclesJson = gson.toJson(wrapper),
                tradeRoutesJson = "" // TODO: Save routes
            )
            
            try {
                com.pocketport.di.DatabaseProvider.getDatabase().portDao().savePortState(entity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun initializeGame() {
        // Initial setup
        val initialPort = Port()
        initialPort.factories.add(Factory(id = UUID.randomUUID().toString()))
        initialPort.cranes.add(Crane(id = UUID.randomUUID().toString()))
        initialPort.trucks.add(Truck(id = UUID.randomUUID().toString()))
        initialPort.ships.add(Ship(id = UUID.randomUUID().toString()))
        
        _portState.value = initialPort
    }

    fun startGameLoop() {
        if (gameJob?.isActive == true) return
        
        gameJob = scope.launch {
            var lastTime = System.currentTimeMillis()
            while (isActive) {
                val currentTime = System.currentTimeMillis()
                val deltaTime = currentTime - lastTime
                lastTime = currentTime

                update(deltaTime)
                
                delay(100) // 10 FPS tick for logic
            }
        }
    }

    fun stopGameLoop() {
        gameJob?.cancel()
    }

    private fun update(deltaTime: Long) {
        val currentPort = _portState.value
        _portState.value = currentPort.copy() 
    }
    
    // Public API for UI interactions
    fun upgradeFactory(factoryId: String) {
        if (upgradeSystem.upgradeFactory(_portState.value, factoryId)) {
            _portState.value = _portState.value.copy()
        }
    }
    
    fun upgradeVehicle(vehicleId: String) {
        if (upgradeSystem.upgradeVehicle(_portState.value, vehicleId)) {
            _portState.value = _portState.value.copy()
        }
    }
}
