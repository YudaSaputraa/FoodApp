package com.kom.foodapp.presentation.register

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout
import com.kom.foodapp.R
import com.kom.foodapp.databinding.ActivityRegisterBinding
import com.kom.foodapp.presentation.login.LoginActivity
import com.kom.foodapp.utils.highLightWord
import com.kom.foodapp.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private val registerViewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setRegisterForm()
        setClickListeners()
    }

    private fun setClickListeners() {
        with(binding) {
            btnRegister.setOnClickListener {
                doRegister()
            }
            tvNotHaveAccount.highLightWord("Login") {
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(
            Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun isFormValid(): Boolean {
        val fullName = binding.layoutForm.etFullName.text.toString().trim()
        val email = binding.layoutForm.etEmail.text.toString().trim()
        val password = binding.layoutForm.etPassword.text.toString().trim()
        val confirmPassword = binding.layoutForm.etConfirmPassword.text.toString().trim()

        return fullNameValidation(fullName) &&
            emailValidation(email) &&
            passwordValidation(password, binding.layoutForm.tilPassword) &&
            passwordValidation(confirmPassword, binding.layoutForm.tilConfirmPassword) &&
            passwordAndConfirmPasswordValidation(password, confirmPassword)
    }

    private fun doRegister() {
        if (isFormValid()) {
            val fullName = binding.layoutForm.etFullName.text.toString().trim()
            val email = binding.layoutForm.etEmail.text.toString().trim()
            val password = binding.layoutForm.etPassword.text.toString().trim()
            registerProcess(fullName, email, password)
        }
    }

    private fun registerProcess(
        fullName: String,
        email: String,
        password: String,
    ) {
        registerViewModel.doRegister(fullName, email, password)
            .observe(this) { result ->
                result.proceedWhen(
                    doOnSuccess = {
                        binding.pbLoading.isVisible = false
                        binding.btnRegister.isVisible = true
                        navigateToLogin()
                        Toast.makeText(
                            this,
                            getString(R.string.text_register_success),
                            Toast.LENGTH_SHORT,
                        ).show()
                    },
                    doOnError = {
                        binding.pbLoading.isVisible = false
                        binding.btnRegister.isEnabled = true
                        binding.btnRegister.isVisible = true
                        binding.btnRegister.text = getString(R.string.text_register)
                        Toast.makeText(
                            this,
                            getString(
                                R.string.text_register_failed,
                                it.exception?.message.orEmpty(),
                            ),
                            Toast.LENGTH_SHORT,
                        ).show()
                    },
                    doOnLoading = {
                        binding.pbLoading.isVisible = true
                        binding.btnRegister.text = ""
                        binding.btnRegister.isEnabled = false
                    },
                )
            }
    }

    private fun navigateToMain() {
        startActivity(
            Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            },
        )
    }

    private fun setRegisterForm() {
        with(binding.layoutForm) {
            tilFullName.isVisible = true
            tilEmail.isVisible = true
            tilPassword.isVisible = true
            tilConfirmPassword.isVisible = true
        }
    }

    private fun fullNameValidation(fullName: String): Boolean {
        return if (fullName.isEmpty()) {
            binding.layoutForm.tilFullName.isErrorEnabled = true
            binding.layoutForm.tilFullName.error = getString(R.string.text_fullname_cannot_empty)
            false
        } else {
            binding.layoutForm.tilFullName.isErrorEnabled = false
            true
        }
    }

    private fun emailValidation(email: String): Boolean {
        return if (email.isEmpty()) {
            binding.layoutForm.tilEmail.isErrorEnabled = true
            binding.layoutForm.tilEmail.error = getString(R.string.text_email_cannot_empty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.layoutForm.tilEmail.isErrorEnabled = true
            binding.layoutForm.tilEmail.error = getString(R.string.text_invalid_email_format)
            false
        } else {
            binding.layoutForm.tilEmail.isErrorEnabled = false
            true
        }
    }

    private fun passwordValidation(
        confirmPassword: String,
        textInputLayout: TextInputLayout,
    ): Boolean {
        return if (confirmPassword.isEmpty()) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = getString(R.string.text_password_cannot_empty)
            false
        } else if (confirmPassword.length < 8) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = getString(R.string.text_password_should_be_8_character)
            false
        } else {
            textInputLayout.isErrorEnabled = false
            true
        }
    }

    private fun passwordAndConfirmPasswordValidation(
        password: String,
        confirmPassword: String,
    ): Boolean {
        return if (password == confirmPassword) {
            binding.layoutForm.tilPassword.isErrorEnabled = false
            binding.layoutForm.tilConfirmPassword.isErrorEnabled = false
            true
        } else {
            binding.layoutForm.tilPassword.isErrorEnabled = true
            binding.layoutForm.tilPassword.error = getString(R.string.text_password_doesnt_match)
            binding.layoutForm.tilConfirmPassword.isErrorEnabled = true
            binding.layoutForm.tilConfirmPassword.error =
                getString(R.string.text_password_doesnt_match)
            false
        }
    }
}
