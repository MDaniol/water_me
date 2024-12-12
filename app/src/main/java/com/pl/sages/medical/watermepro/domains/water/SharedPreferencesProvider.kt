package com.pl.sages.medical.watermepro.domains.water

import android.content.Context

class SharedPreferencesStore(private val context: Context) {

    // Shared preferences - przechowuje dane w postaci klucz-wartość
    // Dane nie mogą być o skomplikowanej strukturze, ani customowej klasy - muszą być prostymi typami
    companion object {
        val KEY = "WATER_AMOUNT"
    }

    fun setWaterForToday(amount: Int) {
        val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(KEY, amount).apply()
    }

    fun getWaterForToday(): Int {
        return sharedPreferences().getInt(KEY, 0)
    }

    // Ekstrakcja do funkcji
    fun sharedPreferences() = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
}