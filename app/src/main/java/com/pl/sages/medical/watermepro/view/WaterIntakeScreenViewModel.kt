package com.pl.sages.medical.watermepro.view

import androidx.lifecycle.ViewModel

class WaterIntakeScreenViewModel: ViewModel() {
    var targetWaterIntake: Int = 0

    var waterIntakeCount: Int = 0

    init {
        updateTargetWaterIntake()
    }

    private fun updateTargetWaterIntake() {
        targetWaterIntake = 20
    }

    fun incrementWaterIntake() {
        waterIntakeCount++
    }
}