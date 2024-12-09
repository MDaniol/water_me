package com.pl.sages.medical.watermepro

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.qualifiedName

    private lateinit var ourTextView: TextView
    private lateinit var ourButton: Button
    private lateinit var ourTextView2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", "onCreate")

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ourButton = findViewById(R.id.our_button)
        ourTextView = findViewById(R.id.our_textview)
        ourTextView2 = findViewById(R.id.our_textview_2)

        initView()
    }

    private fun initView() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ourTextView.text = getString(R.string.hello_polish)
        ourTextView2.text = getString(R.string.empty_placeholder)
        ourButton.text = getString(R.string.button_text)

        ourButton.setOnClickListener {
            buttonTapHandler()
        }
    }

    private fun buttonTapHandler() {
        ourTextView2.text = getString(R.string.i_am_app)
        // JAKAŚ ZMIANA
    }

    private fun sayHello() {
        ourTextView.text = "Cześć, Jestem Apką!"
        ourTextView2.text = "Inne textview"
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