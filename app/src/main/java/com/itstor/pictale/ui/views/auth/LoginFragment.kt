package com.itstor.pictale.ui.views.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.itstor.pictale.R
import com.itstor.pictale.common.Status
import com.itstor.pictale.databinding.FragmentLoginBinding
import com.itstor.pictale.ui.views.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var parentActivity: AuthActivity
    private lateinit var parentViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        parentActivity = activity as AuthActivity
        parentViewModel = ViewModelProvider(parentActivity)[AuthViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateBtnLogin()

        binding.btnSignup.setOnClickListener {
            parentActivity.openRegisterFragment()
        }

        // Add text change listeners to email and password fields
        binding.edLoginEmail.addTextChangedListener {
            updateBtnLogin()
        }

        binding.edLoginPassword.addTextChangedListener {
            updateBtnLogin()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            lifecycleScope.launch {
                parentViewModel.login(email, password).collect {
                    when (it.status) {
                        Status.LOADING -> {
                            showLoading()
                        }

                        Status.SUCCESS -> {
                            hideLoading()

                            showToast(getString(R.string.msg_login_success))

                            // Move to MainActivity
                            val intent = Intent(parentActivity, MainActivity::class.java)

                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finishAffinity(parentActivity)
                        }

                        Status.FAILURE -> {
                            hideLoading()

                            showToast(it.message ?: getString(R.string.msg_login_failed))
                        }
                    }
                }
            }
        }
    }

    private fun isValidInput(): Boolean {
        val etEmail = binding.edLoginEmail
        val etPassword = binding.edLoginPassword
        val etPasswordLength = etPassword.text.toString().length

        return etEmail.error.isNullOrEmpty() && etPasswordLength >= 8 &&
                etEmail.text.toString().isNotEmpty() && etPassword.text.toString().isNotEmpty()
    }

    private fun showToast(message: String) {
        Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateBtnLogin() {
        if (isValidInput()) {
            enableBtnLogin()
        } else {
            disableBtnLogin()
        }
    }

    private fun disableBtnLogin() {
        binding.btnLogin.isEnabled = false
    }

    private fun enableBtnLogin() {
        binding.btnLogin.isEnabled = true
    }

    private fun showLoading() {
        disableBtnLogin()
        binding.pbButtonLogin.visibility = View.VISIBLE
        binding.btnLogin.text = ""
    }

    private fun hideLoading() {
        enableBtnLogin()
        binding.pbButtonLogin.visibility = View.GONE
        binding.btnLogin.text = getString(R.string.login)
    }
}