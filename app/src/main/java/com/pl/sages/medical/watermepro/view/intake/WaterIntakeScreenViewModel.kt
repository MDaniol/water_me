package com.pl.sages.medical.watermepro.view.intake

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pl.sages.medical.watermepro.Container
import com.pl.sages.medical.watermepro.domains.water.WaterRepository
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherData
import com.pl.sages.medical.watermepro.repositories.WeatherRepository
import kotlinx.coroutines.launch

data class UiState(
    val waterIntakeCount: Int = 0,
    val targetWaterIntake: Int = 0,
    val weather: WeatherData? = null,
    val isDataLoading: Boolean = true
)

class WaterIntakeScreenViewModel: ViewModel() {

    private val weatherRepository: WeatherRepository = Container.provideWeatherRepository()
    private val waterRepository: WaterRepository = WaterRepository()

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> get() = _uiState

    init {
        updateTargetWaterIntake()
        getWeather()
        getWaterForToday()
    }

    private fun updateTargetWaterIntake() {
        _uiState.value = _uiState.value?.copy(targetWaterIntake = 20)
    }

    private fun getWaterForToday() {
        val waterForToday = waterRepository.getWaterForToday()
        _uiState.value = _uiState.value?.copy(waterIntakeCount = waterForToday)
    }

    private fun getWeather() {
        // Wchodzimy w ViewModelScope, aby nie blokować wątku UI
        // ViewModelScope - https://developer.android.com/topic/libraries/architecture/coroutines#viewmodelscope
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isDataLoading = true)
            val currentWeather = weatherRepository.getCurrentWeather()
            _uiState.value = _uiState.value?.copy(weather = currentWeather, isDataLoading = false)
        }
    }

    fun incrementWaterIntake() {
        var currentCount = _uiState.value?.waterIntakeCount ?: 0
        currentCount++
        waterRepository.setWaterForToday(currentCount)
        _uiState.value = _uiState.value?.copy(waterIntakeCount = currentCount)
    }
}