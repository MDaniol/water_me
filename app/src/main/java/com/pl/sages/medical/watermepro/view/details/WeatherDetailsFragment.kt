package com.pl.sages.medical.watermepro.view.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pl.sages.medical.watermepro.R
import com.pl.sages.medical.watermepro.databinding.FragmentWeatherDetailsBinding
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherData
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherKind

class WeatherDetailsFragment : Fragment() {

    private val viewModel: WeatherDetailsViewModel by viewModels()

    private val binding: FragmentWeatherDetailsBinding by lazy {
        FragmentWeatherDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView() {
        // (1) ustawiamy adapter dla naszego RecyclerView to ForecastRecyclerViewAdapter który przyjmuje dany z listy defaultData
        binding.weatherForecastRecyclerView.adapter = ForecastRecyclerViewAdapter(viewModel.forecastData)

        // (2) ustawiamy rodzaj layoutu dla RecyclerView i ustawiamy jego orientację (pionową - Vertical)
        binding.weatherForecastRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.currentWeather.observe(viewLifecycleOwner) { currentWeather ->
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