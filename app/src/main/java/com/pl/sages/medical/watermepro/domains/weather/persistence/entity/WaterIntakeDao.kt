package com.pl.sages.medical.watermepro.domains.weather.persistence.entity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterIntakeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(waterIntakeEntity: WaterIntakeEntity)

    @Query("SELECT * FROM water_intake WHERE date = :date LIMIT 1")
    suspend fun getWaterForDate(date: String): WaterIntakeEntity?

    @Query("SELECT * FROM water_intake ORDER BY date DESC")
    suspend fun getWaterIntakeHistory(): List<WaterIntakeEntity>

    @Query("SELECT * FROM water_intake ORDER BY date DESC")
    fun getWaterIntakeHistoryFlow(): Flow<List<WaterIntakeEntity>>
}