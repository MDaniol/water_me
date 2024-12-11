package com.pl.sages.medical.watermepro.domains.weather.models

data class WeatherData(
    val temperature: Int = 0,
    val pressure: Int = 0,
    val description: String = "",
    val weatherKind: WeatherKind = WeatherKind.NONE,
    val icon: String = ""
)