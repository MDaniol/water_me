package com.pl.sages.medical.watermepro.domains.weather.remote

import com.google.gson.annotations.SerializedName

// Ogólna zasada -> patrz ResponseDto
// Przypadek gdy nazwa w json jest inna niż nasz nazwa -> @SerializedName("nazwa_w_json") val nasza_nazwa: Typ
data class CurrentWeatherDto(
    val time: String,
    val interval: Int,
    @SerializedName("temperature_2m") val temperature: Double,
    @SerializedName("weather_code") val weatherCode: Int
)