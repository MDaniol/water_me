package com.pl.sages.medical.watermepro.domains.weather.models

import com.pl.sages.medical.watermepro.repositories.DailyForecast

data class WeatherData(
    val temperature: Int = 0,
    val pressure: Double = 0.0,
    val description: String = "",
    val weatherKind: WeatherKind = WeatherKind.NONE,
    val icon: String = "",
    val weatherForecast: List<DailyForecast> = emptyList()
)