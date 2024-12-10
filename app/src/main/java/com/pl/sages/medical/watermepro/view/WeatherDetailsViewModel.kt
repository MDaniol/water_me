package com.pl.sages.medical.watermepro.view

import androidx.lifecycle.ViewModel
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherData
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherKind

class WeatherDetailsViewModel: ViewModel() {
    val currentWeather: WeatherData = WeatherData(
        temperature = 34,
        pressure = 1013,
        description = "Sunny weather, without wind",
        weatherKind = WeatherKind.SUNNY,
        icon = "ic_sunny"
    )
}