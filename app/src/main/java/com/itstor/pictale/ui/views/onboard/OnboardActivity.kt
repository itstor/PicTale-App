package com.itstor.pictale.ui.views.onboard

import android.content.Intent
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.itstor.pictale.databinding.ActivityOnboardBinding
import com.itstor.pictale.ui.views.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SetUp Status Bar
        window.insetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )

        // SignUp Button Listener. Click to move to AuthActivity
        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            intent.putExtra(AuthActivity.EXTRA_IS_LOGIN, false)

            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    Pair(binding.ivAppLogo, "app_logo"),
                )

            startActivity(intent, optionsCompat.toBundle())
        }

        // SignIn Button Listener. Click to move to AuthActivity
        binding.btnSignin.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            intent.putExtra(AuthActivity.EXTRA_IS_LOGIN, true)

            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    Pair(binding.ivAppLogo, "app_logo"),
                )

            startActivity(intent, optionsCompat.toBundle())
        }
    }
}