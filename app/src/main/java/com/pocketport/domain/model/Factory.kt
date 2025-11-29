package com.pocketport.domain.model

data class Factory(
    val id: String,
    val level: Int = 1,
package com.pocketport.domain.model

data class Factory(
    val id: String,
    val level: Int = 1,
    val productionSpeedModifier: Float = 1.0f,
    val storageCapacity: Int = 10,
    val producedContainers: MutableList<Container> = mutableListOf(),
    val lastProductionTime: Long = 0L
) {
    fun getProductionInterval(): Long {
        // Base 5000ms, decreases by 10% per level, min 500ms
        val reduction = (level - 1) * 0.1f
        val interval = 5000 * (1.0f - reduction)
        return interval.coerceAtLeast(500.0f).toLong()
    }
}
