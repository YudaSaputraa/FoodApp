package com.kom.foodapp.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout
import com.kom.foodapp.R
import com.kom.foodapp.data.datasource.authentication.AuthDataSource
import com.kom.foodapp.data.datasource.authentication.FirebaseAuthDataSource
import com.kom.foodapp.data.repository.UserRepository
import com.kom.foodapp.data.repository.UserRepositoryImpl
import com.kom.foodapp.data.source.firebase.FirebaseService
import com.kom.foodapp.data.source.firebase.FirebaseServiceImpl
import com.kom.foodapp.databinding.ActivityLoginBinding
import com.kom.foodapp.presentation.main.MainActivity
import com.kom.foodapp.presentation.register.RegisterActivity
import com.kom.foodapp.presentation.register.RegisterViewModel
import com.kom.foodapp.utils.GenericViewModelFactory
import com.kom.foodapp.utils.highLightWord
import com.kom.foodapp.utils.proceedWhen

//TODO:PERLU IMPROVE
class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val viewModel: LoginViewModel by viewModels {
        val service: FirebaseService = FirebaseServiceImpl()
        val authDataSource: AuthDataSource = FirebaseAuthDataSource(service)
        val userRepository: UserRepository = UserRepositoryImpl(authDataSource)
        GenericViewModelFactory.create(LoginViewModel(userRepository))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setLoginForm()
        setClickListeners()

    }

    private fun setClickListeners() {

        binding.btnLogin.setOnClickListener {
            doLogin()
        }
        binding.tvNotHaveAccount.highLightWord(getString(R.string.text_register)) {
            navigateToRegister()
        }

        binding.tvForgetPassword.setOnClickListener {
            requestChangePassword()
            viewModel.doRequestResetPasswordByEmail()
        }

    }

    private fun requestChangePassword() {
        //todo : do change password
        val dialog = AlertDialog.Builder(this)
            .setMessage(getString(R.string.text_dialog_when_change_password))
            .setPositiveButton(
                getString(R.string.text_positive_button_dialog)
            ) { dialog, id ->


            }.create()
        dialog.show()
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }

    private fun doLogin() {
        if (isFormValid()) {
            val email = binding.layoutForm.etEmail.text.toString().trim()
            val password = binding.layoutForm.etPassword.text.toString().trim()
            loginProcess(email, password)
        }
    }

    private fun loginProcess(email: String, password: String) {
        viewModel.doLogin(email, password).observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.pbLoading.isVisible = false
                    binding.btnLogin.isVisible = true
                    navigateToMain()
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnLogin.isVisible = true
                    Toast.makeText(
                        this,
                        getString(R.string.text_login_failed, it.exception?.message.orEmpty()),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnLogin.text = ""
                    binding.btnLogin.isEnabled = false
                }
            )
        }
    }

    private fun isFormValid(): Boolean {
        val email = binding.layoutForm.etEmail.text.toString().trim()
        val password = binding.layoutForm.etPassword.text.toString().trim()

        return emailValidation(email) &&
                passwordValidation(
                    password, binding.layoutForm.tilPassword
                )

    }

    private fun setLoginForm() {
        with(binding) {
            layoutForm.tilEmail.isVisible = true
            layoutForm.tilPassword.isVisible = true
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
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
        textInputLayout: TextInputLayout
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
}