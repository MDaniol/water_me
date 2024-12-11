package com.pl.sages.medical.watermepro

import com.pl.sages.medical.watermepro.repositories.WeatherRepository

private val weatherReposiotory by lazy { WeatherRepository() }

// Container jest singletonem (wzorzec projektowy) który zapewnia dostęp do serwisów i repozytoriów z wielu miejsc w aplikacji,
// pilnując aby istniał tylko jeden obiekt danego serwisu/repozytorium.

// Robimy to po to aby mieć jedno źródło danych do aplikacji, które jest zsynchronizowane z różnymi ViewModelami i które jest łatwe do podmiany.
object Container {
    fun provideWeatherRepository() = weatherReposiotory
}