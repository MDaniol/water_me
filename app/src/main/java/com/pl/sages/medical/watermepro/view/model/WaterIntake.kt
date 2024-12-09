package com.pl.sages.medical.watermepro.view.model

data class WaterIntake(
    val amount: Int,
    val unit: WaterIntakeUnit
)

enum class WaterIntakeUnit() {
    GLASS,
    ML
}