package com.pocketport.domain.model

enum class ContainerType(val baseValue: Int, val weight: Int, val colorCode: Long) {
    GENERAL(10, 10, 0xFF888888),    // Grey
    ADVANCED(25, 15, 0xFF0000FF),   // Blue
    FROZEN(40, 20, 0xFF00FFFF),     // Cyan
    DANGEROUS(80, 25, 0xFFFF0000),  // Red
    SPECIAL(150, 30, 0xFFFFD700)    // Gold
}

data class Container(
    val id: String,
    val type: ContainerType,
    val destination: String,
    val productionTime: Long
)
