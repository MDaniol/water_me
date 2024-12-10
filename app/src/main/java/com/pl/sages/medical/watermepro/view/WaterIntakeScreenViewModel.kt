package com.pl.sages.medical.watermepro.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherData
import com.pl.sages.medical.watermepro.repositories.WeatherRepository
import kotlinx.coroutines.launch

class WaterIntakeScreenViewModel: ViewModel() {
    var targetWaterIntake: Int = 0
    private val weatherRepository: WeatherRepository = WeatherRepository()

    // Przyczyna zmian: chcemy aby UI reagował na dane, a jego update nie był triggerowany programistycznie,
    // ponieważ dane mogą być niedostępne w momencie wyzwolenia triggera

    // Korzystamy z mechanizmu LiveData: https://developer.android.com/topic/libraries/architecture/livedata
    // LiveData - holder zapewniajacy mechanizm obserwowania zmian (observable), ktory jest lifecycle-aware (nie jest niszczony podczas rekompozycji ekranu)
    private val _waterIntakeCount = MutableLiveData(0) // MutableLiveData - tutaj mozemy zmieniac wartosc zmiennej
    val waterIntakeCount: LiveData<Int> get() = _waterIntakeCount // LiveData - tutaj mozemy tylko odczytywac, podlaczamy sie tutaj z zewnatrz (metoda observe w activity)

    private val _weather = MutableLiveData<WeatherData>()
    val weather: LiveData<WeatherData> get() = _weather

    private val _isDataLoading = MutableLiveData<Boolean>()
    val isDataLoading: LiveData<Boolean> get() = _isDataLoading



    // isDataLoading = LiveData<Boolean>...

    init {
        updateTargetWaterIntake()
        getWeather()
    }

    private fun updateTargetWaterIntake() {

        targetWaterIntake = 20

    }

    private fun getWeather() {
        // Wchodzimy w ViewModelScope, aby nie blokować wątku UI
        // ViewModelScope - https://developer.android.com/topic/libraries/architecture/coroutines#viewmodelscope
        viewModelScope.launch {
            _isDataLoading.postValue(true) // indykator że rozpoczynamy ładowanie danych (jakaś operacja wymagająca czasu)
            val currentWeather = weatherRepository.getCurrentWeather() // ww. operacja
            _weather.postValue(currentWeather) // aktualizacja LiveData z danymi pogodowymi
            _isDataLoading.postValue(false) // aktualizacja statusu ładowania
        }
    }

    fun incrementWaterIntake() {
        // Metoda aktualizująca _waterIntakeCount, dostępna z wewnątrz VM
        // :? - Elvis operator, zwraca wartość po prawej stronie jeśli wartośc po lewej jest null-em
        _waterIntakeCount.value = (_waterIntakeCount.value ?: 0) + 1 // Aktualizacja zmiennej _waterIntakeCount.value - kiedy to nastapi observers sa powiadamiani
    }
}