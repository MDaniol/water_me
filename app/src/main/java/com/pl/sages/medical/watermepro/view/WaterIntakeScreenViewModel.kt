package com.pl.sages.medical.watermepro.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pl.sages.medical.watermepro.repositories.WeatherRepository

class WaterIntakeScreenViewModel: ViewModel() {
    var targetWaterIntake: Int = 0
    private val weatherRepository: WeatherRepository = WeatherRepository()

    // Przyczyna zmian: chcemy aby UI reagował na dane, a jego update nie był triggerowany programistycznie,
    // ponieważ dane mogą być niedostępne w momencie wyzwolenia triggera

    // Korzystamy z mechanizmu LiveData: https://developer.android.com/topic/libraries/architecture/livedata
    // LiveData - holder zapewniajacy mechanizm obserwowania zmian (observable), ktory jest lifecycle-aware (nie jest niszczony podczas rekompozycji ekranu)
    private val _waterIntakeCount = MutableLiveData(0) // MutableLiveData - tutaj mozemy zmieniac wartosc zmiennej
    val waterIntakeCount: LiveData<Int> get() = _waterIntakeCount // LiveData - tutaj mozemy tylko odczytywac, podlaczamy sie tutaj z zewnatrz (metoda observe w activity)

    private val _weather = MutableLiveData<String>()
    val weather: LiveData<String> get() = _weather

    // isDataLoading = LiveData<Boolean>...

    init {
        updateTargetWaterIntake()
        getWeather()
    }

    private fun updateTargetWaterIntake() {
        // isDataLoading.post(true)
        targetWaterIntake = 20 // czas wykonania -> 3s
        // isDataLoading.post(false)
    }

    private fun getWeather() {
        _weather.postValue(weatherRepository.getCurrentWeather())
    }

    fun incrementWaterIntake() {
        // Metoda aktualizująca _waterIntakeCount, dostępna z wewnątrz VM
        // :? - Elvis operator, zwraca wartość po prawej stronie jeśli wartośc po lewej jest null-em
        _waterIntakeCount.value = (_waterIntakeCount.value ?: 0) + 1 // Aktualizacja zmiennej _waterIntakeCount.value - kiedy to nastapi observers sa powiadamiani
    }
}