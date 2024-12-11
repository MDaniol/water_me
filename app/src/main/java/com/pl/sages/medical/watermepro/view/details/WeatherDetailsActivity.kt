package com.pl.sages.medical.watermepro.view.details

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.pl.sages.medical.watermepro.R
import com.pl.sages.medical.watermepro.databinding.ActivityWeatherDetailsBinding
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherKind

class WeatherDetailsActivity : AppCompatActivity() {

    // binding jest inicjalizowany przez by lazy - czyli zostanie zainicjalizowany w momencie pierwszego wywołania (tj. pierwszego użycia)
    // by lazy - jest to mechanizm delegujący (delegat) który pozwala na leniwą inicjalizację obiektu przez wywołanie funkcji - w tym przypadku ...inflate(...)
    private val binding: ActivityWeatherDetailsBinding by lazy {
        ActivityWeatherDetailsBinding.inflate(layoutInflater)
    }

    private val viewModel: WeatherDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Ważna linijka! - ustawienie widoku na ten związany z bindingiem
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val defaultData = listOf(
            ForecastViewData(
                date = "2024-12-12",
                temperature = "8",
                pressure = "1013",
                weatherKind = WeatherKind.CLOUDY
            ),
            ForecastViewData(
                date = "2024-12-13",
                temperature = "4",
                pressure = "1013",
                weatherKind = WeatherKind.CLOUDY
            ),
            ForecastViewData(
                date = "2024-12-14",
                temperature = "8",
                pressure = "1013",
                weatherKind = WeatherKind.SUNNY
            ),
            ForecastViewData(
                date = "2024-12-12",
                temperature = "8",
                pressure = "1013",
                weatherKind = WeatherKind.CLOUDY
            ),
            ForecastViewData(
                date = "2024-12-13",
                temperature = "4",
                pressure = "1013",
                weatherKind = WeatherKind.CLOUDY
            ),
            ForecastViewData(
                date = "2024-12-14",
                temperature = "8",
                pressure = "1013",
                weatherKind = WeatherKind.SUNNY
            ),
            ForecastViewData(
                date = "2024-12-12",
                temperature = "8",
                pressure = "1013",
                weatherKind = WeatherKind.CLOUDY
            ),
            ForecastViewData(
                date = "2024-12-13",
                temperature = "4",
                pressure = "1013",
                weatherKind = WeatherKind.CLOUDY
            ),
            ForecastViewData(
                date = "2024-12-14",
                temperature = "8",
                pressure = "1013",
                weatherKind = WeatherKind.SUNNY
            ),
            ForecastViewData(
                date = "2024-12-12",
                temperature = "8",
                pressure = "1013",
                weatherKind = WeatherKind.CLOUDY
            ),
            ForecastViewData(
                date = "2024-12-13",
                temperature = "4",
                pressure = "1013",
                weatherKind = WeatherKind.CLOUDY
            ),
            ForecastViewData(
                date = "2024-12-14",
                temperature = "8",
                pressure = "1013",
                weatherKind = WeatherKind.SUNNY
            )
        )
        // (1) ustawiamy adapter dla naszego RecyclerView to ForecastRecyclerViewAdapter który przyjmuje dany z listy defaultData
        binding.weatherForecastRecyclerView.adapter = ForecastRecyclerViewAdapter(defaultData)

        // (2) ustawiamy rodzaj layoutu dla RecyclerView i ustawiamy jego orientację (pionową - Vertical)
        binding.weatherForecastRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        initUi()
    }

    private fun initUi() {

        binding.temperatureTv.text =
            getString(R.string.weather_details_temperature, viewModel.currentWeather.temperature)
        binding.pressureTv.text =
            getString(R.string.weather_details_pressure, viewModel.currentWeather.pressure)

        binding.descriptionTv.text = viewModel.currentWeather.description
    }
}