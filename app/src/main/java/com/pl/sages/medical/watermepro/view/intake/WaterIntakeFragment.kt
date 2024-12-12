package com.pl.sages.medical.watermepro.view.intake

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
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

    val viewModel: WaterIntakeScreenViewModel by viewModels()

    companion object {
        private const val FADE_IN_DURATION = 150L
        private const val FADE_OUT_DURATION = 150L
        private const val FIRST_ACHIEVEMENT_COUNT = 5
        private const val SECOND_ACHIEVEMENT_COUNT = 10
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
    }

    private fun presentToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}