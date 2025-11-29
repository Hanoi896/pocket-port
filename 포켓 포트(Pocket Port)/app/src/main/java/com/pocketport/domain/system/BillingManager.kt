package com.pocketport.domain.system

import android.app.Activity
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Mock Billing Manager for Prototype
class BillingManager(context: Context) {
    private val _isVipActive = MutableStateFlow(false)
    val isVipActive: StateFlow<Boolean> = _isVipActive.asStateFlow()

    fun startConnection() {
        // Connect to Google Play Billing
    }

    fun purchaseVip(activity: Activity) {
        // Launch billing flow
        // On success:
        _isVipActive.value = true
    }
    
    // For testing
    fun setVip(active: Boolean) {
        _isVipActive.value = active
    }
}
