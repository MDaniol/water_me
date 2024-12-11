package com.pl.sages.medical.watermepro.domains.weather.persistence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


// (2) - Zdefiniowanie entity - tj. struktury danych którą będziemy przechowywać w tabeli
@Entity
data class CurrentWeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val time: String,
    val interval: Int,
    val temperature: Double,
    val weatherCode: Int,
    val pressure: Double,
)