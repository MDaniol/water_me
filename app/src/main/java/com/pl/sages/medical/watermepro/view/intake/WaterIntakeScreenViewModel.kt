package com.pl.sages.medical.watermepro.view.intake

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pl.sages.medical.watermepro.Container
import com.pl.sages.medical.watermepro.domains.water.WaterRepository
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherData
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherKind
import com.pl.sages.medical.watermepro.repositories.WeatherRepository
import kotlinx.coroutines.launch

data class UiState(
    val waterIntakeCount: Int = 0,
    val targetWaterIntake: Int = 0,
    val weather: WeatherData? = null,
    val isDataLoading: Boolean = true,
    val cityName: String = ""
)

class WaterIntakeScreenViewModel(): ViewModel() {
    private val weatherRepository: WeatherRepository = Container.provideWeatherRepository()
    private val waterRepository: WaterRepository = Container.provideWaterRepository()

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> get() = _uiState

    init {
        getWeather()
        getWaterForToday()
        updateTargetWaterIntake()
    }

    private fun updateTargetWaterIntake() {
        val weatherKind = _uiState.value?.weather?.weatherKind
        var targetWaterIntake = 10

        when(weatherKind) {
            WeatherKind.SUNNY -> targetWaterIntake = 20
            WeatherKind.CLOUDY -> targetWaterIntake = 15
            WeatherKind.RAINY -> targetWaterIntake = 14
            WeatherKind.SNOWY -> targetWaterIntake = 13
            WeatherKind.NONE -> targetWaterIntake = 12
            WeatherKind.FOGGY -> targetWaterIntake = 11
            else -> targetWaterIntake = 10
        }

        _uiState.value = _uiState.value?.copy(targetWaterIntake = targetWaterIntake)
    }

    private fun getWaterForToday() {
        val waterForToday = waterRepository.getWaterForToday()
        _uiState.value = _uiState.value?.copy(waterIntakeCount = waterForToday)
    }

    private fun getWeatherForLocation(location: Location?) {
        location?.let {
            viewModelScope.launch {
                val currentWeather = weatherRepository.getCurrentWeather(it.latitude, it.longitude)
                _uiState.value = _uiState.value?.copy(weather = currentWeather)
            }
        }
    }

    private fun getWeather() {
        // Wchodzimy w ViewModelScope, aby nie blokować wątku UI
        // ViewModelScope - https://developer.android.com/topic/libraries/architecture/coroutines#viewmodelscope
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isDataLoading = true)
            val currentWeather = weatherRepository.getCurrentWeather(lat = 45.55, lon = 45.0)
            _uiState.value = _uiState.value?.copy(weather = currentWeather, isDataLoading = false)
        }
    }

    fun incrementWaterIntake() {
        var currentCount = _uiState.value?.waterIntakeCount ?: 0
        currentCount++
        waterRepository.setWaterForToday(currentCount)
        _uiState.value = _uiState.value?.copy(waterIntakeCount = currentCount)
        viewModelScope.launch {
            waterRepository.addWaterIntake()
            Log.d("WATER INTAKES DB", "Water intake incremented")
            Log.d("WATER INTAKES DB", "${waterRepository.getWaterIntakeHistory()}")
        }
    }

    fun handleDateChange() {
        waterRepository.setWaterForToday(0)
        getWaterForToday()
    }

    fun updateCityName(name: String?) {
        name?.let {
            _uiState.value = _uiState.value?.copy(cityName = name)
            Log.d("WaterIntakeScreenViewModel", "City name updated to: ${_uiState.value?.cityName}")
        }
    }

    fun updateWeatherForLocation(location: Location?) {
        getWeatherForLocation(location)
    }
}