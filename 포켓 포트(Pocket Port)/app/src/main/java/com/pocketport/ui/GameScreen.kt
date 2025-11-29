package com.pocketport.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pocketport.domain.model.*
import com.pocketport.R
import kotlinx.coroutines.delay

@Composable
fun GameScreen(viewModel: MainViewModel = viewModel()) {
    val portState by viewModel.portState.collectAsState()
    val puzzleState by viewModel.puzzleState.collectAsState()
    var showArtifacts by remember { mutableStateOf(false) }
    var tutorialStep by remember { mutableStateOf(0) } // 0: None, 1: Welcome, 2: Upgrade
    
    // Simple Tutorial Logic
    LaunchedEffect(Unit) {
        if (portState.level == 1 && portState.experience == 0L) {
            tutorialStep = 1
        }
    }
    
    if (puzzleState.isActive) {
        PuzzleScreen(viewModel = viewModel, onClose = { viewModel.endPuzzle() })
    } else if (showArtifacts) {
        ArtifactScreen(artifacts = viewModel.artifacts, onClose = { showArtifacts = false })
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Bar: Resources
                TopResourceBar(portState)

                // Main Game Area (Canvas for custom drawing of port)
                Box(modifier = Modifier.weight(1f).fillMaxWidth().background(Color(0xFF81D4FA))) { // Sea color
                    PortCanvas(portState)
                }

                // Bottom Control Panel
                ControlPanel(portState, viewModel, onShowArtifacts = { showArtifacts = true })
            }
            
            // Tutorial Overlay
            if (tutorialStep > 0) {
                TutorialOverlay(step = tutorialStep, onNext = { tutorialStep = if (tutorialStep < 2) tutorialStep + 1 else 0 })
            }
        }
    }
}

@Composable
fun TopResourceBar(port: Port) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Gold: ${port.gold}")
        Text("Gems: ${port.diamonds}")
        Text("Level: ${port.level}")
    }
}

@Composable
fun PortCanvas(port: Port) {
    val shipImage = ImageBitmap.imageResource(id = com.pocketport.R.drawable.ic_ship)
    val factoryImage = ImageBitmap.imageResource(id = com.pocketport.R.drawable.ic_factory)
    val craneImage = ImageBitmap.imageResource(id = com.pocketport.R.drawable.ic_crane)
    
    // Particle System State
    val particleSystem = remember { ParticleSystem() }
    // Trigger updates for particles (independent of game loop for smooth visual)
    LaunchedEffect(Unit) {
        while (true) {
            particleSystem.update()
            delay(16) // ~60 FPS
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        
        // Draw Land
        drawRect(
            color = Color(0xFF8D6E63), // Brown land
            topLeft = Offset(0f, canvasHeight * 0.6f),
            size = Size(canvasWidth, canvasHeight * 0.4f)
        )
        
        // Draw Factories
        port.factories.forEachIndexed { index, factory ->
            val x = 100f + (index * 200f)
            val y = canvasHeight * 0.65f // Adjusted for sprite
            
            drawImage(
                image = factoryImage,
                dstOffset = IntOffset(x.toInt(), y.toInt()),
                dstSize = IntSize(120, 120) // Scale up slightly
            )
            
            // Emit Smoke
            if (Math.random() < 0.1) {
                particleSystem.emit(
                    position = Offset(x + 80f, y + 10f), // Smokestack pos
                    count = 1,
                    color = Color.Gray.copy(alpha = 0.5f)
                )
            }
        }
        
        // Draw Ships
        port.ships.forEachIndexed { index, ship ->
            val x = if (ship.state == VehicleState.MOVING) canvasWidth * 0.8f else 100f + (index * 300f)
            val y = canvasHeight * 0.45f
            
            drawImage(
                image = shipImage,
                dstOffset = IntOffset(x.toInt(), y.toInt()),
                dstSize = IntSize(200, 80)
            )
            
            // Draw Container Stack on Ship
            if (ship.currentLoad.isNotEmpty()) {
                drawRect(
                    color = Color(ship.currentLoad.first().type.colorCode),
                    topLeft = Offset(x + 40f, y + 10f),
                    size = Size(100f, 20f)
                )
            }
            
            // Emit Water Trail if moving
            if (ship.state == VehicleState.MOVING) {
                 particleSystem.emit(
                    position = Offset(x, y + 70f), // Rear of ship
                    count = 2,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
        
        // Draw Cranes
        port.cranes.forEachIndexed { index, crane ->
            val x = 180f + (index * 200f)
            val y = canvasHeight * 0.55f
            
            drawImage(
                image = craneImage,
                dstOffset = IntOffset(x.toInt(), y.toInt()),
                dstSize = IntSize(100, 150)
            )
            
            if (crane.state == VehicleState.LOADING || crane.state == VehicleState.UNLOADING) {
                 drawCircle(
                    color = Color.Yellow,
                    center = Offset(x + 85f, y + 55f), // Tip of crane arm
                    radius = 10f
                 )
            }
        }
        
        // Draw Particles
        particleSystem.particles.forEach { p ->
            drawCircle(
                color = p.color.copy(alpha = p.life),
                center = p.position,
                radius = p.size * p.life
            )
        }
    }
}

@Composable
fun ControlPanel(port: Port, viewModel: MainViewModel, onShowArtifacts: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Text("Control Panel", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { 
                val factoryId = port.factories.firstOrNull()?.id
                if (factoryId != null) viewModel.onUpgradeFactory(factoryId)
            }) {
                Text("Upgrade Factory")
            }
            
            Button(onClick = {
                val shipId = port.ships.firstOrNull()?.id
                if (shipId != null) viewModel.onUpgradeVehicle(shipId)
            }) {
                Text("Upgrade Ship")
            }
            
            Button(onClick = { viewModel.startPuzzle() }) {
                Text("Mini Game")
            }
            
            Button(onClick = onShowArtifacts) {
                Text("Artifacts")
            }
            
            Button(onClick = { viewModel.purchaseVip() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))) {
                Text("VIP")
            }
        }
    }
}

@Composable
fun TutorialOverlay(step: Int, onNext: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .pointerInput(Unit) {} // Block clicks
    ) {
        Card(
            modifier = Modifier.align(Alignment.Center).padding(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (step == 1) "Welcome to Pocket Port!" else "Upgrade your Factory to start!",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onNext) {
                    Text("Next")
                }
            }
        }
    }
}
