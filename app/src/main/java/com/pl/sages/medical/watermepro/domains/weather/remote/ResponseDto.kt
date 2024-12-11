package com.pl.sages.medical.watermepro.domains.weather.remote

import com.google.gson.annotations.SerializedName
import java.io.Serial

// ResponseDto - klasa która jest na wzór JSONa zwracanego przez API:
//https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&current=temperature_2m,weather_code&daily=weather_code,apparent_temperature_max,wind_speed_10m_max
// Zagnieżdżone dane -> Tworzymy dla nich oddzielną klasę -> CurrentWeatherDto
data class ResponseDto(
    val latitude: Double,
    val longitude: Double,
    val current: CurrentWeatherDto,
    val daily: DailyWeatherDto,
    @SerializedName("daily_units") val dailyUnits: DailyUnitsDto
)

data class DailyWeatherDto(
    val time: List<String>,
    @SerializedName("weather_code") val weatherCode: List<Int>,
    @SerializedName("apparent_temperature_max") val apparentTempMax: List<Double>,
    @SerializedName("wind_speed_10m_max") val windSpeedMax: List<Double>
)

data class DailyUnitsDto(
    @SerializedName("time") val timeUnit: String,
    @SerializedName("weather_code") val weatherCodeUnit: String,
    @SerializedName("apparent_temperature_max") val apparentTempMaxUnit: String,
    @SerializedName("wind_speed_10m_max") val windSpeedMaxUnit: String
)


/*
"daily_units": {
    "time": "iso8601",
    "weather_code": "wmo code",
    "apparent_temperature_max": "°C",
    "wind_speed_10m_max": "km/h"
  },
 */


/*
daily": {
    "time": [
      "2024-12-11",
      "2024-12-12",
      "2024-12-13",
      "2024-12-14",
      "2024-12-15",
      "2024-12-16",
      "2024-12-17"
    ],
    "weather_code": [3, 51, 45, 61, 53, 53, 3],
    "apparent_temperature_max": [0.8, 0.5, -1.9, -2.3, 0.8, 5.3, 4.8],
    "wind_speed_10m_max": [7.6, 4.6, 10, 15.1, 15.5, 20.2, 18.4]
  }
 */