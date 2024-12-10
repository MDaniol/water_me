package com.pl.sages.medical.watermepro.domains.weather.remote

import com.pl.sages.medical.watermepro.domains.weather.models.WeatherData
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherKind
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
            WeatherData(temperature = 26, WeatherKind.RAINY, "ic_rainy") // zwracamy dane pogodowe
        }
    }
}
