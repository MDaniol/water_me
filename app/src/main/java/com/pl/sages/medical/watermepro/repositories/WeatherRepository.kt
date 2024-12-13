package com.pl.sages.medical.watermepro.repositories

import com.pl.sages.medical.watermepro.Container
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherData
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherKind
import com.pl.sages.medical.watermepro.domains.weather.persistence.PersistenceWeatherProvider
import com.pl.sages.medical.watermepro.domains.weather.persistence.entity.CurrentWeatherEntity
import com.pl.sages.medical.watermepro.domains.weather.remote.DailyWeatherDto
import com.pl.sages.medical.watermepro.domains.weather.remote.RemoteWeatherProvider
import com.pl.sages.medical.watermepro.domains.weather.remote.ResponseDto
import com.pl.sages.medical.watermepro.view.details.ForecastViewData

class WeatherRepository {
    // Zadanie: dostarczyć informacje pogodowe (offline, online)

    val remoteWeatherProvider = RemoteWeatherProvider()
    val persistenceWeatherProvider = PersistenceWeatherProvider()

    private var weatherData: WeatherData? = null

    private val database = Container.provideWeatherDatabase()

//    private fun getWeather() {
//        weatherDto = remoteWeatherProvider.getWeatherForecast()
//    }

    suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherData {
        if (weatherData == null) {
            weatherData = remoteWeatherProvider.getWeatherForecast(lat, lon)

            val currentWeatherEntity = CurrentWeatherEntity(
                id = null,
                temperature = weatherData!!.temperature.toDouble(),
                pressure = weatherData!!.pressure,
                time = "time",
                interval = 300,
                weatherCode = 0,
            )
            database.currentWeatherDao().save(currentWeatherEntity)
            return weatherData!!
        } else {
            return weatherData!!
        }
    }



    fun getWeatherForecast(): List<DailyForecast> {
       if (weatherData == null) {
           return emptyList()
       } else {
           return weatherData!!.weatherForecast
       }
    }

    object DailyWeatherDtoToDailyForecastMapper {
        fun map(dailyWeatherDto: DailyWeatherDto): List<DailyForecast> {

            val dailyForecasts = mutableListOf<DailyForecast>()

            for (i in dailyWeatherDto.time.indices) {
                dailyForecasts.add(
                    DailyForecast(
                        date = dailyWeatherDto.time[i],
                        weatherKind = WeatherCodeToWeatherKindMapper.map(dailyWeatherDto.weatherCode[i]),
                        apparentTempMax = dailyWeatherDto.apparentTempMax[i],
                        windSpeedMax = dailyWeatherDto.windSpeedMax[i]
                    )
                )
            }

            return dailyForecasts
        }
    }
}

object WeatherCodeToWeatherKindMapper {
    fun map(weatherCode: Int): WeatherKind {
        return when(weatherCode) {
            0 -> WeatherKind.SUNNY
            in 1..3 -> WeatherKind.CLOUDY
            in 40..49 -> WeatherKind.FOGGY
            in 50..69 -> WeatherKind.RAINY
            in 70.. 79 -> WeatherKind.SNOWY
            in 80..99 -> WeatherKind.RAINY
            else -> WeatherKind.NONE
        }
    }
}

data class DailyForecast(
    val date: String,
    val weatherKind: WeatherKind,
    val apparentTempMax: Double,
    val windSpeedMax: Double
)

// Architektura:
// Podział na domeny: Weather oraz Water
// Activity <-> ViewModel <-> Repository <-> WeatherProviders (Remote, Persistence) <-> WeatherManagers (API, Database)