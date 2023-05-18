package com.itstor.pictale.ui.views.auth

import android.graphics.Color
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.itstor.pictale.databinding.ActivityAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)

        setContentView(binding.root)

        window.statusBarColor = Color.BLACK
        window.insetsController?.setSystemBarsAppearance(
            0,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )

        val isLogin = intent.getBooleanExtra(EXTRA_IS_LOGIN, true)

        if (isLogin) openLoginFragment()
        else openRegisterFragment()
    }

    fun openLoginFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.flAuthBody.id, LoginFragment())
            .commit()
    }

    fun openRegisterFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.flAuthBody.id, RegisterFragment())
            .commit()
    }

    companion object {
        const val TAG = "AuthActivity"
        const val EXTRA_IS_LOGIN = "isLogin"
    }
}