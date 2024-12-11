package com.pl.sages.medical.watermepro.domains.weather.remote

// ResponseDto - klasa która jest na wzór JSONa zwracanego przez API:
//https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&current=temperature_2m,weather_code&daily=weather_code,apparent_temperature_max,wind_speed_10m_max
// Zagnieżdżone dane -> Tworzymy dla nich oddzielną klasę -> CurrentWeatherDto
data class ResponseDto(
    val latitude: Double,
    val longitude: Double,
    val current: CurrentWeatherDto,
)