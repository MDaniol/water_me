package com.pl.sages.medical.watermepro.domains.weather.persistence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_intake")
data class WaterIntakeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val date: String,
    val waterIntake: Int
)