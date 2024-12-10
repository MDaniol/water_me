package com.pl.sages.medical.watermepro.domains.weather.models

data class WeatherData(
    val temperature: Int,
    val pressure: Int,
    val description: String,
    val weatherKind: WeatherKind,
    val icon: String
)