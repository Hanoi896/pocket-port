package com.pocketport.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

data class Particle(
    var position: Offset,
    var velocity: Offset,
    var life: Float, // 0.0 to 1.0
    val color: Color,
    val size: Float
)

class ParticleSystem {
    val particles = mutableListOf<Particle>()

    fun emit(position: Offset, count: Int, color: Color) {
        repeat(count) {
            particles.add(
                Particle(
                    position = position,
                    velocity = Offset(
                        (Random.nextFloat() - 0.5f) * 5f,
                        (Random.nextFloat() - 1.0f) * 5f // Upward bias
                    ),
                    life = 1.0f,
                    color = color,
                    size = Random.nextFloat() * 10f + 5f
                )
            )
        }
    }

    fun update() {
        val iterator = particles.iterator()
        while (iterator.hasNext()) {
            val p = iterator.next()
            p.position += p.velocity
            p.life -= 0.02f // Fade out
            if (p.life <= 0) {
                iterator.remove()
            }
        }
    }
}
