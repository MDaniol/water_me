package com.pl.sages.medical.watermepro.view

import androidx.lifecycle.ViewModel

class WaterIntakeScreenViewModel: ViewModel() {
    var waterIntakeCount: Int = 0

    fun incrementWaterIntake() {
        waterIntakeCount++
    }
}