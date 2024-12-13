package com.pl.sages.medical.watermepro.domains.water

import com.pl.sages.medical.watermepro.Container
import com.pl.sages.medical.watermepro.domains.weather.persistence.entity.WaterIntakeDao
import com.pl.sages.medical.watermepro.domains.weather.persistence.entity.WaterIntakeEntity
import kotlinx.coroutines.CoroutineScope
import java.text.SimpleDateFormat
import java.util.Date

class WaterRepository(
    private val sharedPreferencesStore: SharedPreferencesStore,
    private val waterIntakeDao: WaterIntakeDao) {

    init {

    }

    fun getWaterForToday(): Int {
        val waterDrunkToday = sharedPreferencesStore.getWaterForToday()
        return waterDrunkToday
    }

    fun setWaterForToday(amount: Int) {
        sharedPreferencesStore.setWaterForToday(amount)
    }

    suspend fun addWaterIntake() {
        val today = Date() // chcemy mieć yyyy-MM-dd
        val todayFormatted = SimpleDateFormat("yyyy-MM-dd").format(today)

        val currentWaterIntake = waterIntakeDao.getWaterForDate(todayFormatted)

        val newWaterIntake = if (currentWaterIntake != null) {
            currentWaterIntake.copy(waterIntake = currentWaterIntake.waterIntake + 1)
        }
        else {
            WaterIntakeEntity(date = todayFormatted, waterIntake = 1)
        }
        waterIntakeDao.insert(newWaterIntake)
    }

    suspend fun getWaterIntakeHistory(): List<WaterIntake> {
        return waterIntakeDao.getWaterIntakeHistory().map {
            MapWaterIntakeEntityToWaterIntake.map(it)
        }
    }
}

object MapWaterIntakeEntityToWaterIntake {
    fun map(entity: WaterIntakeEntity): WaterIntake = WaterIntake(
        date = entity.date,
        waterIntake = entity.waterIntake
    )
}

data class WaterIntake(
    val date: String,
    val waterIntake: Int
)

