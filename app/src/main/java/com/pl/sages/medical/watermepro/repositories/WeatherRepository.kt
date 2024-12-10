package com.pl.sages.medical.watermepro.repositories

import com.pl.sages.medical.watermepro.domains.weather.models.WeatherData
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherKind
import com.pl.sages.medical.watermepro.domains.weather.persistence.PersistenceWeatherProvider
import com.pl.sages.medical.watermepro.domains.weather.remote.RemoteWeatherProvider

class WeatherRepository {
    // Zadanie: dostarczyć informacje pogodowe (offline, online)

    val remoteWeatherProvider = RemoteWeatherProvider()
    val persistenceWeatherProvider = PersistenceWeatherProvider()

    // suspen -> funkcja zawieszająca (asynchroniczna, nie blokująca wątku)
    suspend fun getCurrentWeather(): WeatherData {
        // WYwołanie kolejnej funkcji suspend z remoteWeatherProvidera
        val weather = remoteWeatherProvider.getCurrentWeather()
        return weather
    }
}

// Architektura:
// Podział na domeny: Weather oraz Water
// Activity <-> ViewModel <-> Repository <-> WeatherProviders (Remote, Persistence) <-> WeatherManagers (API, Database)