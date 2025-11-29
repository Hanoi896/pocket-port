package com.pocketport.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketport.domain.GameManager
import com.pocketport.domain.model.Port
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val gameManager = GameManager()
    val portState: StateFlow<Port> = gameManager.portState
    val puzzleState = gameManager.puzzleManager.puzzleState
    val artifacts = gameManager.artifactSystem.getArtifacts()
    
    // Mock Billing
    private val _isVip = MutableStateFlow(false)
    val isVip: StateFlow<Boolean> = _isVip.asStateFlow()

    init {
        gameManager.loadGame()
        gameManager.startGameLoop()
    }

    fun onUpgradeFactory(factoryId: String) {
        viewModelScope.launch {
            gameManager.upgradeFactory(factoryId)
        }
    }

    fun onUpgradeVehicle(vehicleId: String) {
        viewModelScope.launch {
            gameManager.upgradeVehicle(vehicleId)
        }
    }
    
    fun startPuzzle() {
        gameManager.puzzleManager.startPuzzle()
    }
    
    fun onPuzzleSwipe(direction: com.pocketport.domain.system.PuzzleDirection) {
        gameManager.puzzleManager.submitSwipe(direction)
    }
    
    fun endPuzzle() {
        val score = gameManager.puzzleManager.endPuzzle()
        // Add rewards...
    }
    
    fun purchaseVip() {
        _isVip.value = true
        gameManager.isVip = true
    }

    override fun onCleared() {
        super.onCleared()
        gameManager.stopGameLoop()
    }
}

