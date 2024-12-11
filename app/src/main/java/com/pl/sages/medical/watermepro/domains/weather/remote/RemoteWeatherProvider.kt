package com.pl.sages.medical.watermepro.domains.weather.remote

import android.util.Log
import com.pl.sages.medical.watermepro.Container
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherData
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherKind
import com.pl.sages.medical.watermepro.repositories.WeatherRepository
import com.pl.sages.medical.watermepro.view.details.ForecastViewData
import okhttp3.Response

class RemoteWeatherProvider() {

    private val TAG: String = RemoteWeatherProvider::class.java.simpleName
    // Zadanie: Dostarczyć informacje pogodowe z API
    // Funkcja suspend - zawieszająca (asynchroniczna, nie blokująca wątku)
    // withContext - zmiana kontekstu (wątku) na IO, IO - peracje na plikach/sieci/etc.
    // Disparchers - https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html

    val openMetoApi = Container.provideOpenMeteoApi()

    suspend fun getWeatherForecast(): WeatherData {
        val weatherForecast = openMetoApi.getWeatherForecast()

        Log.d(TAG, "Open Meteo Response: ${weatherForecast.toString()}")

        return responseDtoToWeatherData(weatherForecast)
    }

    fun responseDtoToWeatherData(responseDto: ResponseDto): WeatherData {
       return WeatherData(
            temperature = responseDto.current.temperature.toInt(),
            pressure = responseDto.current.pressure,
            description = WeatherCodeToWeatherDescription.map(responseDto.current.weatherCode),
            weatherKind = WeatherKind.RAINY,
            icon = "ic_rainy",
            weatherForecast = WeatherRepository.DailyWeatherDtoToDailyForecastMapper.map(responseDto.daily)
        )
    }

}

object WeatherCodeToWeatherDescription {
    fun map(weatherCode: Int): String {
        return when(weatherCode) {
            0 -> "Perfect weather, enjoy!"
            in 1..3 -> "Cloudy weather, but you can still go out"
            in 40..49 -> "So called english weather"
            in 50..69 -> "A bit weather, there is a hope for us"
            in 70.. 79 -> "Snow is falling, lets make a snowman"
            in 80..99 -> "Rainy weather, without any hope for sun"
            else -> "Weather which is an unknown mixture of strange conditions"
        }
    }
}
