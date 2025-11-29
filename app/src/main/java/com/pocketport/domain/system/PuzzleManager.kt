package com.pocketport.domain.system

import com.pocketport.domain.model.ContainerType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import kotlin.random.Random

data class PuzzleItem(
    val id: String = UUID.randomUUID().toString(),
    val type: ContainerType,
    val targetDirection: PuzzleDirection // LEFT, RIGHT, UP, DOWN
)

enum class PuzzleDirection {
    LEFT, RIGHT, UP, DOWN
}

data class PuzzleState(
    val isActive: Boolean = false,
    val remainingTimeSeconds: Int = 60,
    val score: Int = 0,
    val currentItem: PuzzleItem? = null,
    val combo: Int = 0
)

class PuzzleManager {
    private val _puzzleState = MutableStateFlow(PuzzleState())
    val puzzleState: StateFlow<PuzzleState> = _puzzleState.asStateFlow()

    fun startPuzzle() {
        _puzzleState.value = PuzzleState(
            isActive = true,
            remainingTimeSeconds = 60,
            score = 0,
            currentItem = generateNextItem(),
            combo = 0
        )
    }

    fun submitSwipe(direction: PuzzleDirection) {
        val current = _puzzleState.value
        if (!current.isActive || current.currentItem == null) return

        if (direction == current.currentItem.targetDirection) {
            // Correct
            val newScore = current.score + (10 * (1 + current.combo * 0.1)).toInt()
            _puzzleState.value = current.copy(
                score = newScore,
                combo = current.combo + 1,
                currentItem = generateNextItem()
            )
        } else {
            // Wrong
            _puzzleState.value = current.copy(
                combo = 0, // Reset combo
                currentItem = generateNextItem() // Skip to next or shake? For now skip
            )
        }
    }

    fun tick(deltaTimeMillis: Long) {
        val current = _puzzleState.value
        if (!current.isActive) return

        // Simple timer decrement logic. 
        // In a real loop, we'd accumulate time. 
        // Assuming this is called every 100ms or 1s.
        // Let's assume the caller handles the 1s decrement or we track millis.
        // For simplicity, let's say we have a separate timer in UI or GameManager.
    }
    
    fun endPuzzle(): Int {
        val finalScore = _puzzleState.value.score
        _puzzleState.value = _puzzleState.value.copy(isActive = false)
        return finalScore
    }

    private fun generateNextItem(): PuzzleItem {
        val types = ContainerType.values()
        val type = types[Random.nextInt(types.size)]
        val direction = PuzzleDirection.values()[Random.nextInt(PuzzleDirection.values().size)]
        return PuzzleItem(type = type, targetDirection = direction)
    }
}
