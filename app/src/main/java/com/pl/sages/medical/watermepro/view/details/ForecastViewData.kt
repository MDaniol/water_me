package com.pl.sages.medical.watermepro.view.details

import com.pl.sages.medical.watermepro.domains.weather.models.WeatherKind

data class ForecastViewData(
    val date: String,
    val temperature: String,
    val pressure: String,
    val weatherKind: WeatherKind
)

fun ForecastViewData.getDefault(): List<ForecastViewData> {
    val defaultData = listOf(
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

    return defaultData
}