package com.pl.sages.medical.watermepro.view.intake

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pl.sages.medical.watermepro.R
import com.pl.sages.medical.watermepro.databinding.ActivityWaterIntakeBinding
import com.pl.sages.medical.watermepro.domains.weather.models.WeatherKind
import com.pl.sages.medical.watermepro.view.details.WeatherDetailsActivity

class WaterIntakeActivity : AppCompatActivity() {

    private val TAG = WaterIntakeActivity::class.qualifiedName

    private lateinit var binding: ActivityWaterIntakeBinding

    val viewModel: WaterIntakeScreenViewModel by viewModels()

    companion object {
        private const val FADE_IN_DURATION = 150L
        private const val FADE_OUT_DURATION = 150L
        private const val FIRST_ACHIEVEMENT_COUNT = 5
        private const val SECOND_ACHIEVEMENT_COUNT = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", "onCreate")

        enableEdgeToEdge()

        binding = ActivityWaterIntakeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
        Log.d(TAG, "Going to WeatherDetailsActivity")
        val intent = Intent(this, WeatherDetailsActivity::class.java)
        startActivity(intent)
    }

    private fun buttonTapHandler() {
        viewModel.incrementWaterIntake()
        Log.d(TAG, "Water intake count: ${viewModel.uiState.value?.waterIntakeCount}")
    }

    // Inicjalizujemy obserwator uiState z ViewModelu (nasłuchujemy zmian)
    fun observeViewModel() {
       viewModel.uiState.observe(this) { uiState ->
           // jesli jakas zmiana wystapi wykona się kod poniżej,
           // zmieniony obiekt uiState jest przekazany jako zmienna uiState
           updateUI(uiState) // Update danych na UI z obiektu uiState
       }
    }

    // Update interfejsu użytkownika
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
                    binding.weatherIconImageView.setImageIcon(Icon.createWithResource(this, R.drawable.ic_sunny))
                }
                WeatherKind.RAINY -> {
                    binding.weatherIconImageView.setImageIcon(Icon.createWithResource(this, R.drawable.ic_thunder))
                }
                WeatherKind.SNOWY -> {
                    binding.weatherIconImageView.setImageIcon(Icon.createWithResource(this, R.drawable.ic_snowy))
                }
                WeatherKind.CLOUDY -> {
                    binding.weatherIconImageView.setImageIcon(Icon.createWithResource(this, R.drawable.ic_cloudy))
                }
                WeatherKind.NONE -> {
                    binding.weatherIconImageView.setImageIcon(Icon.createWithResource(this, R.drawable.ic_question_mark))
                }

                WeatherKind.FOGGY -> {
                    binding.weatherIconImageView.setImageResource(R.drawable.ic_foggy)
                }
            }
        }
    }

    private fun presentToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}