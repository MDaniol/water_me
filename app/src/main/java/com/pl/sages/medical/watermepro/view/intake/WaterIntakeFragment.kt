package com.pl.sages.medical.watermepro.view.intake

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.registerReceiver
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.pl.sages.medical.watermepro.Container
import com.pl.sages.medical.watermepro.R
import com.pl.sages.medical.watermepro.databinding.ActivityWaterIntakeBinding
import com.pl.sages.medical.watermepro.databinding.FragmentWaterIntakeBinding
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherKind
import com.pl.sages.medical.watermepro.view.details.WeatherDetailsActivity
import com.pl.sages.medical.watermepro.view.intake.WaterIntakeActivity.Companion

class WaterIntakeFragment : Fragment() {

    private val TAG = WaterIntakeFragment::class.qualifiedName

    private lateinit var binding: FragmentWaterIntakeBinding
    private lateinit var dateChangeReceiver: BroadcastReceiver
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    val viewModel: WaterIntakeScreenViewModel by viewModels()

    companion object {
        private const val FADE_IN_DURATION = 150L
        private const val FADE_OUT_DURATION = 150L
        private const val FIRST_ACHIEVEMENT_COUNT = 5
        private const val SECOND_ACHIEVEMENT_COUNT = 10

        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // (1) Rejestracja odbiorcy zmiany daty, w momencie kiedy data w systemie się zmieni -> wywołana zostanie metoda onReceive
        // W naszym przypadku metoda onReceive wywoła metodę handleDateChange z naszego viewModelu
        // object: BroadcastReceiver() -> tworzymy instancję klasy anonimowej dziedziczącej po BroadcastReceiver, w sposób podobny do lambdy, uproszczony
        dateChangeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                viewModel.handleDateChange() // Odwołanie do ViewModelu
            }
        }

        val intentFilter = IntentFilter(Intent.ACTION_DATE_CHANGED)
        // Rejestracja broadcast receivera w kontekście naszej aplikacji
        requireContext().registerReceiver(dateChangeReceiver, intentFilter)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    // Ważne! W momencie kiedy fragmen jest niszczony (usuwany z pamieci) - nalezy wyrejestrować broadcast receivera
    override fun onDestroy() {
        super.onDestroy()
        requireContext().unregisterReceiver(dateChangeReceiver)
    }

    // Metoda cyklu zycia fragmentu: https://developer.android.com/guide/fragments/lifecycle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWaterIntakeBinding.inflate(layoutInflater)
        return binding.root
    }

    // Metoda cyklu życia fragmentu: https://developer.android.com/guide/fragments/lifecycle
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Container.initialize(requireContext())
        initView()
        requestLocation() // rozpoczynamy proces pobierania lokalizaji: (1) - sprawdzenie permisions, (2) gdy nie ma permissions to requestPermissions, (2a) gdy są permissions to getLastKnownLocation
    }

    private fun initView() {
        binding.ourButton.text = getString(R.string.button_text)

        binding.ourButton.setOnClickListener {
            buttonTapHandler()
        }

        binding.weatherIconImageView.setOnClickListener {
            goToWeatherDetails()
        }
        observeViewModel()
    }

    private fun goToWeatherDetails() {
        findNavController().navigate(R.id.go_to_weather_details)
    }

    private fun buttonTapHandler() {
        viewModel.incrementWaterIntake()
        Log.d(TAG, "Water intake count: ${viewModel.uiState.value?.waterIntakeCount}")
    }

    fun observeViewModel() {
        // tutaj ustawiamy obserwatora na zmienną uiState w naszym viewModelu.
        // Za każdym razem gdy zmieni się cokolwiek w uiState, to cały uiState zostanie przekazany do metody updateUI
        // Metoda observe - jest zależna od cyklu życia "ownera" - czyli fragmentu -> a on z kolei jest zależny od cyklu życia aktywności
        // Jeśli fragment przestanie istnieć, to jakiekolwiek akcje wykonywane w ramach metody updateUI zostaną przerwane
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            updateUI(uiState)
        }
    }

    private fun updateUI(uiState: UiState) {
        binding.targetWaterIntakeTv.text =
            getString(R.string.target_water_intake_text, "${uiState.targetWaterIntake}")

        val fadeOut = ObjectAnimator.ofFloat(binding.waterIntakeCountTv, "alpha", 1f, 0f)
        fadeOut.duration = FADE_OUT_DURATION

        val fadeIn = ObjectAnimator.ofFloat(binding.waterIntakeCountTv, "alpha", 0f, 1f)
        fadeIn.duration = FADE_IN_DURATION

        fadeOut.addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                binding.waterIntakeCountTv.text = "${uiState.waterIntakeCount}"
                fadeIn.start()
            }
        })

        fadeOut.start()

        if(uiState.waterIntakeCount == FIRST_ACHIEVEMENT_COUNT) {
            presentToast("Wspaniała robota!")
        } else if(uiState.waterIntakeCount == SECOND_ACHIEVEMENT_COUNT) {
            presentToast("Keep going!")
        }

        if (uiState.isDataLoading) {
            // jeśli jest ładowanie danych -> ustawiamy kanał alpha (nieprzezroczystość) na 1 dla loading spinnera
            binding.loadingSpinner.alpha = 1f

            // jeśli jest ładowanie danych -> ustawiamy kanał alpha (nieprzezroczystość) na 0 dla reszty (będą niewidoczne)
            binding.targetWaterIntakeTv.alpha = 0f
            binding.waterIntakeCountTv.alpha = 0f
            binding.weatherIconImageView.alpha = 0f
            binding.waterIconImageView.alpha = 0f
            binding.weatherDescriptionTv.alpha = 0f
            // Wyłączamy przysk (nie da sie w niego kliknąć)
            binding.ourButton.isEnabled = false
        } else {
            // Jeśli już nie ładujemy danych, to loading spinner powinien zniknąć a reszta elementów powinna się pojawić
            binding.loadingSpinner.alpha = 0f

            binding.targetWaterIntakeTv.alpha = 1f
            binding.waterIntakeCountTv.alpha = 1f
            binding.weatherIconImageView.alpha = 1f
            binding.waterIconImageView.alpha = 1f
            binding.weatherDescriptionTv.alpha = 1f

            binding.ourButton.isEnabled = true
        }

        uiState.weather?.let { weather ->

            binding.weatherDescriptionTv.text = weather.description

            when(weather.weatherKind) {
                WeatherKind.SUNNY -> {
                    binding.weatherIconImageView.setImageResource(R.drawable.ic_sunny)
                }
                WeatherKind.RAINY -> {
                    binding.weatherIconImageView.setImageResource(R.drawable.ic_thunder)
                }
                WeatherKind.SNOWY -> {
                    binding.weatherIconImageView.setImageResource(R.drawable.ic_snowy)
                }
                WeatherKind.CLOUDY -> {
                    binding.weatherIconImageView.setImageResource(R.drawable.ic_cloudy)
                }
                WeatherKind.NONE -> {
                    binding.weatherIconImageView.setImageResource(R.drawable.ic_question_mark)
                }

                WeatherKind.FOGGY -> {
                    binding.weatherIconImageView.setImageResource(R.drawable.ic_foggy)
                }
            }
        }

        binding.cityTv.text = uiState.cityName
    }

    private fun presentToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    // GPS (1) - zdefiniowanie requestu o pozwolenie użycia serwisów lokalizacyjnych
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                getLastKnownLocation() // (2) jeśli użytkownik zaakceptował dokładną lokalizację to wchodzimy tutaj i pobieramy ją
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                getLastKnownLocation() // (2a) jeśli nie, ale zaakceptował lokalizację z grubsza to wchodzimy tu
            }
            else -> {
                // (3) jeśli użytkownik nie zaakceptował lokalizacji to wyświetlamy mu komunikat
                Toast.makeText(requireContext(), "Location permission not granted", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun requestLocation() {
        if(checkIfLocationPermissionGranted()) {
            getLastKnownLocation()
        } else {
            requestLocationPermissions()
        }
    }

    // Sprawdzamy czy permissions są przyznane
    private fun getLastKnownLocation() {
        if (checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermissions()
            return
        }

        // FusedLocationClient - klient który pośredniczy w dostępie do danych lokalizacyjnych (GPS, sieć)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                handleLocation(location)
            }
    }

    // Metoda uruchamiająca request o pozwolenie na lokalizację
    private fun requestLocationPermissions() {
        locationPermissionRequest.launch(REQUIRED_PERMISSIONS)
    }

    private fun checkIfLocationPermissionGranted(): Boolean {
        val anyPermissionGranted = REQUIRED_PERMISSIONS.any { permission ->
            checkSelfPermission(requireContext(), permission) == PERMISSION_GRANTED
        }
        return anyPermissionGranted
    }

    // Jeśli dostaliśmy lokalizację -> dekodujemy adres z nią związany
    private fun handleLocation(location: Location?) {
        Toast.makeText(requireContext(), "Location: lat:${location?.latitude} lon:${location?.longitude}", Toast.LENGTH_LONG).show()

//        if (location != null) {
//            decodeAddressFromLocation(location)
//        }\

        viewModel.updateWeatherForLocation(location)

        location?.let {
            decodeAddressFromLocation(it)
        }
    }

    // Dekodowanie adresu z lokalizacji
    private fun decodeAddressFromLocation(location: Location) {
        val geocoder = Geocoder(requireContext())
        geocoder.getFromLocation(location.latitude, location.longitude, 6, object : Geocoder.GeocodeListener { // klasa anonimowa implementująca interfejs GeocodeListener
            override fun onGeocode(addresses: MutableList<Address>) {
                val decodedCity = addresses.firstOrNull()?.locality

                // Uruchomienie na wątku głównym (!) https://developer.android.com/guide/components/processes-and-threads
                requireActivity().runOnUiThread {
                    updateCityName(decodedCity)
                }
            }
        })
    }

    private fun updateCityName(cityName: String?) {
        viewModel.updateCityName(cityName)
    }

}