package com.pl.sages.medical.watermepro.domains.weather.remote

import android.util.Log
import com.pl.sages.medical.watermepro.Container
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherData
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherKind
import com.pl.sages.medical.watermepro.view.details.ForecastViewData

class RemoteWeatherProvider() {

    private val TAG: String = RemoteWeatherProvider::class.java.simpleName
    // Zadanie: Dostarczyć informacje pogodowe z API
    // Funkcja suspend - zawieszająca (asynchroniczna, nie blokująca wątku)
    // withContext - zmiana kontekstu (wątku) na IO, IO - peracje na plikach/sieci/etc.
    // Disparchers - https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html

    val openMetoApi = Container.provideOpenMeteoApi()

    suspend fun getCurrentWeather(): WeatherData {
        val weatherForecast = openMetoApi.getWeatherForecast()

        Log.d(TAG, "Open Meteo Response: ${weatherForecast.toString()}")

        return responseDtoToWeatherData(weatherForecast)
    }

    fun responseDtoToWeatherData(responseDto: ResponseDto): WeatherData {
       return WeatherData(
            temperature = responseDto.current.temperature.toInt(),
            pressure = 1013,
            description = "Rainy weather, without any hope for sun",
            weatherKind = WeatherKind.RAINY,
            icon = "ic_rainy"
        )
    }

    fun getWeatherForecast(): List<ForecastViewData> {
        return listOf(
            ForecastViewData(
                date = "2024-12-12",
                temperature = "8",
                pressure = "1013",
                weatherKind = WeatherKind.CLOUDY
            ),
            ForecastViewData(
                date = "2024-12-13",
                temperature = "4",
                pressure = "1013",
                weatherKind = WeatherKind.CLOUDY
            ),
            ForecastViewData(
                date = "2024-12-14",
                temperature = "8",
                pressure = "1013",
                weatherKind = WeatherKind.SUNNY
            ),
            ForecastViewData(
                date = "2024-12-12",
                temperature = "8",
                pressure = "1013",
                weatherKind = WeatherKind.CLOUDY
            ),
            ForecastViewData(
                date = "2024-12-13",
                temperature = "4",
                pressure = "1013",
                weatherKind = WeatherKind.CLOUDY
            ),
            ForecastViewData(
                date = "2024-12-14",
                temperature = "8",
                pressure = "1013",
                weatherKind = WeatherKind.SUNNY
            ),
            ForecastViewData(
                date = "2024-12-12",
                temperature = "8",
                pressure = "1013",
                weatherKind = WeatherKind.CLOUDY
            ),
            ForecastViewData(
                date = "2024-12-13",
                temperature = "4",
                pressure = "1013",
                weatherKind = WeatherKind.CLOUDY
            ),
            ForecastViewData(
                date = "2024-12-14",
                temperature = "8",
                pressure = "1013",
                weatherKind = WeatherKind.SUNNY
            ),
            ForecastViewData(
                date = "2024-12-12",
                temperature = "8",
                pressure = "1013",
                weatherKind = WeatherKind.CLOUDY
            ),
            ForecastViewData(
                date = "2024-12-13",
                temperature = "4",
                pressure = "1013",
                weatherKind = WeatherKind.CLOUDY
            ),
            ForecastViewData(
                date = "2024-12-14",
                temperature = "8",
                pressure = "1013",
                weatherKind = WeatherKind.SUNNY
            )
        )
    }
}
