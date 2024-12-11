package com.pl.sages.medical.watermepro.domains.weather.remote

import retrofit2.http.GET

//(5) Interfejs który mówi:
// 1. Jaki rodzaj zapytania -tutaj GET
// 2. Jaki endpoint - tutaj "forecast?latitude=52.52&longitude=13.41&current=temperature_2m,weather_code&daily=weather_code,apparent_temperature_max,wind_speed_10m_max"
// 3. Jakie dane zwraca - tutaj ResponseDto
// 4. Jaka jest nazwa funkcji - tutaj getWeatherForecast
// 5. Jakie są parametry - brak
interface OpenMeteoApi {
    @GET("forecast?latitude=52.52&longitude=13.41&current=temperature_2m,weather_code&daily=weather_code,apparent_temperature_max,wind_speed_10m_max")
    suspend fun getWeatherForecast(): ResponseDto
}