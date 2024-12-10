package com.pl.sages.medical.watermepro.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pl.sages.medical.watermepro.R
import com.pl.sages.medical.watermepro.databinding.ActivityWeatherDetailsBinding

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

        initUi()
    }

    private fun initUi() {
        binding.temperatureTv.text = "TEST"
        binding.pressureTv.text = viewModel.currentWeather.pressure.toString()
        binding.descriptionTv.text = viewModel.currentWeather.description
    }
}