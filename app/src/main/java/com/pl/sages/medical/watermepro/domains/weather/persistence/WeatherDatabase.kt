package com.pl.sages.medical.watermepro.domains.weather.persistence

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pl.sages.medical.watermepro.domains.weather.persistence.entity.CurrentWeatherDao
import com.pl.sages.medical.watermepro.domains.weather.persistence.entity.CurrentWeatherEntity

// (4) Konstrukcja bazy danych przechowującej konkretne entity, w tym przypadku 'CurrentWeatherEntity'
// version - wersja bazy danych, wynikająca z wersji tabel w tje bazie (jeśli tabele (entities) ulegną zmianie, to trzeba
// zwiększyć wersję bazy danych)
// @Database - adnotacja oznaczająca klasę jako bazę danych, powoduje wygenerowanie kodu implementującego bazę danych
// na podstawie klasy abstrakcyjnej WeatherDatabase oraz metod w niej zawartych
@Database(entities = [CurrentWeatherEntity::class], version = 1)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
}

