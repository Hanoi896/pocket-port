package com.pocketport.ui

import android.content.Context
import android.util.Log

object SoundManager {
    fun initialize(context: Context) {
        // Load sounds here
    }

    fun playSound(soundName: String) {
        Log.d("SoundManager", "Playing sound: $soundName")
        // pool.play(...)
    }
}
