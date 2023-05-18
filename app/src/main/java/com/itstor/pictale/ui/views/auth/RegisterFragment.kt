package com.itstor.pictale.ui.views.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.itstor.pictale.R
import com.itstor.pictale.common.Status
import com.itstor.pictale.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var parentActivity: AuthActivity
    private lateinit var parentViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        parentActivity = activity as AuthActivity
        parentViewModel = ViewModelProvider(parentActivity)[AuthViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateBtnLogin()

        binding.btnSignin.setOnClickListener {
            parentActivity.openLoginFragment()
        }

        binding.btnSubmit.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            lifecycleScope.launch {
                parentViewModel.register(name, email, password).collect {
                    when (it.status) {
                        Status.LOADING -> {
                            showLoading()
                        }

                        Status.SUCCESS -> {
                            hideLoading()
                            showToast(getString(R.string.msg_register_success))
                            parentActivity.openLoginFragment()
                        }

                        Status.FAILURE -> {
                            hideLoading()
                            showToast(it.message ?: getString(R.string.msg_register_failed))
                        }
                    }
                }
            }
        }

        // Add text change listeners to email and password fields
        binding.edRegisterEmail.addTextChangedListener {
            updateBtnLogin()
        }

        binding.edRegisterPassword.addTextChangedListener {
            updateBtnLogin()
        }

        binding.edRegisterName.addTextChangedListener {
            updateBtnLogin()
        }
    }

    private fun isValidInput(): Boolean {
        val etName = binding.edRegisterName
        val etEmail = binding.edRegisterEmail
        val etPassword = binding.edRegisterPassword
        val etPasswordLength = etPassword.text.toString().length

        return etEmail.error.isNullOrEmpty() && etPasswordLength >= 8 &&
                etEmail.text.toString().isNotEmpty() && etPassword.text.toString().isNotEmpty() &&
                etName.text.toString().isNotEmpty()
    }

    private fun updateBtnLogin() {
        if (isValidInput()) {
            enableBtnLogin()
        } else {
            disableBtnLogin()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun disableBtnLogin() {
        binding.btnSubmit.isEnabled = false
    }

    private fun enableBtnLogin() {
        binding.btnSubmit.isEnabled = true
    }

    private fun showLoading() {
        disableBtnLogin()
        binding.pbButtonSubmit.visibility = View.VISIBLE
        binding.btnSubmit.text = ""
    }

    private fun hideLoading() {
        enableBtnLogin()
        binding.pbButtonSubmit.visibility = View.GONE
        binding.btnSubmit.text = getString(R.string.submit)
    }
}