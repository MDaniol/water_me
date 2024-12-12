package com.pl.sages.medical.watermepro.domains.water

import com.pl.sages.medical.watermepro.Container
import java.util.Date

class WaterRepository {
    private val store: SharedPreferencesStore = Container.provideSharedPreferencesStore()

    fun getWaterForToday(): Int {
        val waterDrunkToday = store.getWaterForToday()
        return waterDrunkToday
    }

    fun setWaterForToday(amount: Int) {
        store.setWaterForToday(amount)
    }
}