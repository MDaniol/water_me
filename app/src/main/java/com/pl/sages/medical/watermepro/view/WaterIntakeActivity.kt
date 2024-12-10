package com.pl.sages.medical.watermepro.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pl.sages.medical.watermepro.R

class WaterIntakeActivity : AppCompatActivity() {

    private val TAG = WaterIntakeActivity::class.qualifiedName

    private lateinit var ourButton: Button
    private lateinit var waterIntakeCountTextView: TextView
    private lateinit var targetWaterIntakeTextView: TextView

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
        setContentView(R.layout.activity_water_intake)

        ourButton = findViewById(R.id.our_button)
        waterIntakeCountTextView = findViewById(R.id.water_intake_count_tv)
        targetWaterIntakeTextView = findViewById(R.id.target_water_intake_tv)
        initView()
    }

    private fun initView() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ourButton.text = getString(R.string.button_text)

        ourButton.setOnClickListener {
            buttonTapHandler()
        }

        updateUI(isInitialUpdate = true)
    }

    private fun buttonTapHandler() {
        viewModel.incrementWaterIntake()
        Log.d(TAG, "Water intake count: ${viewModel.waterIntakeCount}")
        updateUI()
    }

    private fun updateUI(isInitialUpdate: Boolean = false) {
        targetWaterIntakeTextView.text =
            getString(R.string.target_water_intake_text, "${viewModel.targetWaterIntake}")

        if (isInitialUpdate) {
            waterIntakeCountTextView.text = "${viewModel.waterIntakeCount}"
        } else {
            val fadeOut = ObjectAnimator.ofFloat(waterIntakeCountTextView, "alpha", 1f, 0f)
            fadeOut.duration = FADE_OUT_DURATION

            val fadeIn = ObjectAnimator.ofFloat(waterIntakeCountTextView, "alpha", 0f, 1f)
            fadeIn.duration = FADE_IN_DURATION

            fadeOut.addListener(object: AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    waterIntakeCountTextView.text = "${viewModel.waterIntakeCount}"
                    fadeIn.start()
                }
            })

            fadeOut.start()
        }

        if(viewModel.waterIntakeCount == FIRST_ACHIEVEMENT_COUNT) {
            presentToast("Wspaniała robota!")
        } else if(viewModel.waterIntakeCount == SECOND_ACHIEVEMENT_COUNT) {
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