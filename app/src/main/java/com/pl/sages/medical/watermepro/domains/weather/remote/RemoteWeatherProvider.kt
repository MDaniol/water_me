package com.pl.sages.medical.watermepro.domains.weather.remote

import com.pl.sages.medical.watermepro.domains.weather.models.WeatherData
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherKind
import com.pl.sages.medical.watermepro.view.details.ForecastViewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class RemoteWeatherProvider() {
    // Zadanie: Dostarczyć informacje pogodowe z API
    // Funkcja suspend - zawieszająca (asynchroniczna, nie blokująca wątku)
    // withContext - zmiana kontekstu (wątku) na IO, IO - peracje na plikach/sieci/etc.
    // Disparchers - https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html
    suspend fun getCurrentWeather(): WeatherData {
        return withContext(Dispatchers.IO) {
            delay(3000) // czekamy 3s

            WeatherData(
                temperature = 26,
                pressure = 1013,
                description = "Rainy weather, without any hope for sun",
                weatherKind = WeatherKind.RAINY,
                icon = "ic_rainy") // zwracamy dane pogodowe
        }
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
