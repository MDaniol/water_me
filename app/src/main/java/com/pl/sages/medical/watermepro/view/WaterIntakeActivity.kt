package com.pl.sages.medical.watermepro.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
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

        updateUI(waterIntakeCount = 0, targetWaterIntake = viewModel.targetWaterIntake, isInitialUpdate = true)
        observeViewModel()
    }

    private fun buttonTapHandler() {
        viewModel.incrementWaterIntake()
        Log.d(TAG, "Water intake count: ${viewModel.waterIntakeCount}")
    }

    fun observeViewModel() { //
        // Obserwator zmiennej LiveData - deklarujemy akcje do wykonania w momencie zmiany wartosci zmiennej
        viewModel.waterIntakeCount.observe(this) {
            Log.d(TAG, "Water intake count: $it")
            updateUI(waterIntakeCount = it, targetWaterIntake = viewModel.targetWaterIntake)
        }

            // .isDataLoading.obeserve(this) { wlacz/wylacz przycisk=... }
    }


    private fun updateUI(waterIntakeCount: Int, targetWaterIntake: Int, isInitialUpdate: Boolean = false) {
        binding.targetWaterIntakeTv.text =
            getString(R.string.target_water_intake_text, "${targetWaterIntake}")

        if (isInitialUpdate) {
            binding.waterIntakeCountTv.text = "${waterIntakeCount}"
        } else {
            val fadeOut = ObjectAnimator.ofFloat(binding.waterIntakeCountTv, "alpha", 1f, 0f)
            fadeOut.duration = FADE_OUT_DURATION

            val fadeIn = ObjectAnimator.ofFloat(binding.waterIntakeCountTv, "alpha", 0f, 1f)
            fadeIn.duration = FADE_IN_DURATION

            fadeOut.addListener(object: AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    binding.waterIntakeCountTv.text = "${waterIntakeCount}"
                    fadeIn.start()
                }
            })

            fadeOut.start()
        }

        if(waterIntakeCount == FIRST_ACHIEVEMENT_COUNT) {
            presentToast("Wspaniała robota!")
        } else if(waterIntakeCount == SECOND_ACHIEVEMENT_COUNT) {
            presentToast("Keep going!")
        }
    }

    private fun presentToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart")
    }
}