package com.pl.sages.medical.watermepro.view.details

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.pl.sages.medical.watermepro.R
import com.pl.sages.medical.watermepro.databinding.ActivityWeatherDetailsBinding
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherData
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherKind
import com.pl.sages.medical.watermepro.domains.weather.persistence.WeatherDatabase

class WeatherDetailsActivity : AppCompatActivity() {

    // binding jest inicjalizowany przez by lazy - czyli zostanie zainicjalizowany w momencie pierwszego wywołania (tj. pierwszego użycia)
    // by lazy - jest to mechanizm delegujący (delegat) który pozwala na leniwą inicjalizację obiektu przez wywołanie funkcji - w tym przypadku ...inflate(...)
    private val binding: ActivityWeatherDetailsBinding by lazy {
        ActivityWeatherDetailsBinding.inflate(layoutInflater)
    }

//    private lateinit var database: WeatherDatabase

    private val viewModel: WeatherDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Ważna linijka! - ustawienie widoku na ten związany z bindingiem
        setContentView(binding.root)

        initView()

//        database = Room.databaseBuilder(this, WeatherDatabase::class.java, "weather-database")
//            .fallbackToDestructiveMigration()
//            .build()
    }

    private fun initView() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // (1) ustawiamy adapter dla naszego RecyclerView to ForecastRecyclerViewAdapter który przyjmuje dany z listy defaultData
        binding.weatherForecastRecyclerView.adapter = ForecastRecyclerViewAdapter(viewModel.forecastData)

        // (2) ustawiamy rodzaj layoutu dla RecyclerView i ustawiamy jego orientację (pionową - Vertical)
        binding.weatherForecastRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.currentWeather.observe(this) { currentWeather ->
            setupUi(currentWeather)
        }
    }

    private fun setupUi(weather: WeatherData) {

        binding.imageView.setImageResource(
            when (weather.weatherKind) {
                WeatherKind.SUNNY -> R.drawable.ic_sunny
                WeatherKind.CLOUDY -> R.drawable.ic_cloudy
                WeatherKind.RAINY -> R.drawable.ic_thunder
                WeatherKind.SNOWY -> R.drawable.ic_snowy
                WeatherKind.FOGGY -> R.drawable.ic_foggy
                WeatherKind.NONE -> R.drawable.ic_question_mark
            }
        )

        binding.temperatureTv.text =
            getString(R.string.weather_details_temperature, weather.temperature)
        binding.pressureTv.text =
            getString(R.string.weather_details_pressure, weather.pressure.toInt())

        binding.descriptionTv.text = weather.description
    }
}