package com.pl.sages.medical.watermepro.view.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pl.sages.medical.watermepro.Container
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherData
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherKind
import com.pl.sages.medical.watermepro.repositories.DailyForecast
import com.pl.sages.medical.watermepro.repositories.WeatherRepository
import com.pl.sages.medical.watermepro.view.intake.UiState
import kotlinx.coroutines.launch

class WeatherDetailsViewModel: ViewModel() {

    private val weatherRepository: WeatherRepository = WeatherRepository()

    private val repository: WeatherRepository = Container.provideWeatherRepository()

    val forecastData: List<DailyForecast> = repository.getWeatherForecast()

    private val _currentWeather = MutableLiveData(WeatherData())
    val currentWeather: LiveData<WeatherData> get() = _currentWeather

    init {
        getWeather()
    }

    fun getWeather() {
        viewModelScope.launch {
            _currentWeather.postValue(repository.getCurrentWeather())
        }
    }
}