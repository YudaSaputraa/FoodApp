package com.kom.foodapp.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout
import com.kom.foodapp.R
import com.kom.foodapp.databinding.ActivityLoginBinding
import com.kom.foodapp.presentation.main.MainActivity
import com.kom.foodapp.presentation.register.RegisterActivity
import com.kom.foodapp.utils.highLightWord
import com.kom.foodapp.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val loginViewModel: LoginViewModel by viewModel()

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
            createEmailInputDialog(this)
        }

    }


    private fun createEmailInputDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Reset Password")
        val layoutContainer = LinearLayout(context)
        layoutContainer.orientation = LinearLayout.VERTICAL
        val textInputLayout = TextInputLayout(context)
        textInputLayout.hint = "Email"
        val input = EditText(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        input.layoutParams = layoutParams
        input.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        textInputLayout.addView(input)
        layoutContainer.addView(textInputLayout)
        textInputLayout.setPadding(32, 32, 32, 32)
        builder.setView(layoutContainer)
        builder.setPositiveButton(getString(R.string.text_submit)) { dialog, _ ->
            val email = input.text.toString()
            forgetPasswordRequestProcess(email)
            dialog.dismiss()
        }
        builder.setNegativeButton(getString(R.string.text_cencel)) { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun forgetPasswordRequestProcess(email: String) {
        loginViewModel.doRequestChangePasswordByEmailWithoutLogin(email).observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    requestChangePasswordDialogSuccess()
                },
                doOnError = {
                    Toast.makeText(
                        this,
                        "Error :  ${it.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d(
                        "reqChangePasswordByEmail",
                        "createEmailInputDialog: ${it.exception?.message}"
                    )
                }
            )
        }
    }


    private fun requestChangePasswordDialogSuccess() {
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
        loginViewModel.doLogin(email, password).observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.pbLoading.isVisible = false
                    binding.btnLogin.isVisible = true
                    navigateToMain()
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnLogin.isVisible = true
                    binding.btnLogin.isEnabled = true
                    binding.btnLogin.text = getString(R.string.text_login)

                    val errorMessage = it.exception?.message ?: getString(R.string.error_unknown)
                    val errorText = if (errorMessage.contains("password")) {
                        getString(R.string.error_wrong_password)
                    } else {
                        getString(R.string.error_wrong_email)
                    }

                    Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show()
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