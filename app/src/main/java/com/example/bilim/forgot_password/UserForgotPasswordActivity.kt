package com.example.bilim.forgot_password

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.bilim.R
import com.google.firebase.auth.FirebaseAuth

class UserForgotPasswordActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var userEmailResetPasswordEditText: EditText
    private lateinit var resetButton: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_forgot_password)
        initViews()
        userEmailResetPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val isInputEmpty: Boolean = p0.toString().isBlank()
                resetButton.isEnabled = !isInputEmpty
            }
        })
        resetButton.setOnClickListener {
            resetPassword()
        }
    }

    private fun initViews() {
        toolbar = findViewById(R.id.activity_user_forgot_password_toolbar)
        toolbar.setOnClickListener {
            finish()
        }
        userEmailResetPasswordEditText = findViewById(R.id.activity_user_forgot_password_email_edit_text)
        resetButton = findViewById(R.id.activity_user_forgot_password_reset_button)
        mAuth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.activity_user_forgot_password_progress_bar)
    }

    private fun resetPassword() {
        val email: String = userEmailResetPasswordEditText.text.toString().trim()
        if (email.isEmpty()) {
            userEmailResetPasswordEditText.error = getString(R.string.email_empty_error_text)
            userEmailResetPasswordEditText.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmailResetPasswordEditText.error = getString(R.string.invalid_email_error_text)
            userEmailResetPasswordEditText.requestFocus()
            return
        }
        progressBar.isVisible = true
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                progressBar.isVisible = false
                Toast.makeText(this, getString(R.string.reset_password_text), Toast.LENGTH_LONG).show()
            } else {
                progressBar.isVisible = false
                Toast.makeText(this, getString(R.string.reset_password_error_text), Toast.LENGTH_LONG).show()
            }
        }
    }
}