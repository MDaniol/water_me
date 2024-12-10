package com.pl.sages.medical.watermepro.domains.weather.remote

import com.pl.sages.medical.watermepro.domains.weather.models.WeatherData
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherKind
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class RemoteWeatherProvider() {
    //Zadanie: Dostarczyć informacje pogodowe z API
    suspend fun getCurrentWeather(): WeatherData {
        return withContext(Dispatchers.IO) {
            delay(3000)
            WeatherData(temperature = 26, WeatherKind.RAINY, "ic_rainy")
        }
    }
}
