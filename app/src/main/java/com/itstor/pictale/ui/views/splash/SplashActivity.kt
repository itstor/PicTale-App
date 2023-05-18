package com.itstor.pictale.ui.views.splash

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.itstor.pictale.databinding.ActivitySplashBinding
import com.itstor.pictale.ui.views.home.MainActivity
import com.itstor.pictale.ui.views.onboard.OnboardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the theme and status bar color
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor = Color.BLACK
        window.insetsController?.setSystemBarsAppearance(
            0,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Delay for 1 second
                kotlinx.coroutines.delay(1000)

                viewModel.getAuthToken().collect {
                    // Move to Main when user is logged in. Otherwise, move to Onboard
                    if (it != null) {
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this@SplashActivity, OnboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}