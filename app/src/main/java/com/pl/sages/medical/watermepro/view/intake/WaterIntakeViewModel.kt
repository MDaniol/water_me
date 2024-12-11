package com.pl.sages.medical.watermepro.view.intake

import com.pl.sages.medical.watermepro.view.model.WaterIntake
import java.util.Date

data class WaterIntakeViewModel(
    val date: Date,
    val waterIntakes: List<WaterIntake>
)