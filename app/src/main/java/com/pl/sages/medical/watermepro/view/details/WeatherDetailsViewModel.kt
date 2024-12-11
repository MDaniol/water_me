package com.pl.sages.medical.watermepro.view.details

import androidx.lifecycle.ViewModel
import com.pl.sages.medical.watermepro.Container
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherData
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherKind
import com.pl.sages.medical.watermepro.repositories.WeatherRepository

class WeatherDetailsViewModel: ViewModel() {

    private val weatherRepository: WeatherRepository = WeatherRepository()

    private val repository: WeatherRepository = Container.provideWeatherRepository()

    val forecastData: List<ForecastViewData> = repository.getWeatherForecast()

    val currentWeather: WeatherData = WeatherData(
        temperature = 34,
        pressure = 1013,
        description = "Sunny weather, without wind",
        weatherKind = WeatherKind.SUNNY,
        icon = "ic_sunny"
    )
}