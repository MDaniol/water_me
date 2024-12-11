package com.pl.sages.medical.watermepro

import android.content.Context
import androidx.room.Room
import com.google.gson.annotations.SerializedName
import com.pl.sages.medical.watermepro.domains.weather.persistence.WeatherDatabase
import com.pl.sages.medical.watermepro.domains.weather.remote.OpenMeteoApi
import com.pl.sages.medical.watermepro.repositories.WeatherRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


private val weatherRepository by lazy { WeatherRepository() }

// Container jest singletonem (wzorzec projektowy) który zapewnia dostęp do serwisów i repozytoriów z wielu miejsc w aplikacji,
// pilnując aby istniał tylko jeden obiekt danego serwisu/repozytorium.

// Robimy to po to aby mieć jedno źródło danych do aplikacji, które jest zsynchronizowane z różnymi ViewModelami i które jest łatwe do podmiany.

// (1) Stworzenei klienta Retrofit, który będzie komunikował się z API o base_url: "https://api.open-meteo.com/v1/"
// (2) Dodanie konwertera Gsona do klienta Retrofit - Gson - dodany w build.gradle.kts (module)
// (3) build() -> stworzenie klienta Retrofit z komponentów (1) i (2) - wzorzec projektowy builder (https://refactoring.guru/design-patterns/builder)
// (4) create(OpenMeteoApi::class.java) -> tworzymy klasę do komunikacji z API na bazie przepisu zawartego w interfejsie OpenMeteoApi
private val openMeteoApi = Retrofit
    .Builder()
    .baseUrl("https://api.open-meteo.com/v1/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(OpenMeteoApi::class.java)


object Container {

    private lateinit var appContext: Context

    // Inicjalizacja kontenera Contextem - aby mieć dostęp do plików (w tym pliku bazy danych Room)
    fun initialize(context: Context) {
        appContext = context
    }

    fun provideWeatherRepository() = weatherRepository
    fun provideOpenMeteoApi() = openMeteoApi

    // (6) Dostęp BD z obszaru całej aplikacji - zwraca instancję bazy danych
    // TODO: Przerobić aby nie tworzyć nowej instancji za każdym razem!
    fun provideWeatherDatabase(): WeatherDatabase {
        return Room.databaseBuilder(appContext, WeatherDatabase::class.java, "weather-database")
            .build()
    }
}




