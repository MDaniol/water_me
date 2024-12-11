package com.pl.sages.medical.watermepro.domains.weather.persistence.entity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy


// (3) Definicja Data Access Object
// @Dao - adnotacja oznaczająca klasę jako DAO, powoduje te wygenerowaniu kodu implementującego
// DAO na podstawie interfejsu 'CurrentWeatherDao' oraz metod w nim zawartych
// @Insert - adnotacja oznaczająca metodę SQL INSERT powodująca wstawienie nowego rekordu do tabeli
// @Delete - adnotacja oznaczająca metodę SQL DELETE powodująca usunięcie rekordu z tabeli
@Dao
interface CurrentWeatherDao {
    // INSERT, DELETE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(currentWeatherEntity: CurrentWeatherEntity)

    @Delete
    suspend fun delete(currentWeatherEntity: CurrentWeatherEntity)
}