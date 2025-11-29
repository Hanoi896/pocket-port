package com.pocketport.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pocketport.domain.model.Artifact

@Composable
fun ArtifactScreen(
    artifacts: List<Artifact>,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF37474F))
            .padding(16.dp)
    ) {
        Column {
            Text("Artifact Collection", style = MaterialTheme.typography.headlineMedium, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(artifacts) { artifact ->
                    ArtifactItem(artifact)
                }
            }
        }
        
        Button(
            onClick = onClose,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Text("Close")
        }
    }
}

@Composable
fun ArtifactItem(artifact: Artifact) {
    Card(
        colors = CardDefaults.cardColors(containerColor = if (artifact.isUnlocked) Color(0xFFFFD700) else Color.Gray)
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Text(artifact.name, style = MaterialTheme.typography.titleMedium)
            Text(artifact.description, style = MaterialTheme.typography.bodyMedium)
            if (!artifact.isUnlocked) {
                Text("(Locked)", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
