package com.pocketport.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pocketport.domain.system.PuzzleDirection
import kotlin.math.abs

@Composable
fun PuzzleScreen(
    viewModel: MainViewModel = viewModel(),
    onClose: () -> Unit
) {
    val puzzleState by viewModel.puzzleState.collectAsState()
    
    // Simple Swipe Detector
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF263238))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        // Determine direction
                        if (abs(offsetX) > abs(offsetY)) {
                            if (offsetX > 0) viewModel.onPuzzleSwipe(PuzzleDirection.RIGHT)
                            else viewModel.onPuzzleSwipe(PuzzleDirection.LEFT)
                        } else {
                            if (offsetY > 0) viewModel.onPuzzleSwipe(PuzzleDirection.DOWN)
                            else viewModel.onPuzzleSwipe(PuzzleDirection.UP)
                        }
                        offsetX = 0f
                        offsetY = 0f
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Time: ${puzzleState.remainingTimeSeconds}", color = Color.White, style = MaterialTheme.typography.headlineMedium)
            Text("Score: ${puzzleState.score}", color = Color.Yellow, style = MaterialTheme.typography.headlineSmall)
            Text("Combo: ${puzzleState.combo}", color = Color.Cyan)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Current Item Display
            val item = puzzleState.currentItem
            if (item != null) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .background(Color(item.type.colorCode))
                ) {
                    Text(
                        text = item.targetDirection.name,
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Text("Swipe to Match Direction!", color = Color.Gray)
        }
        
        Button(
            onClick = onClose,
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
        ) {
            Text("Exit")
        }
    }
}
