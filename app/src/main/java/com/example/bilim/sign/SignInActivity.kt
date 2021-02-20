package com.example.bilim.sign

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bilim.R
import com.example.bilim.registration.RegistrationActivity


class SignInActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var forgotPasswordTextView: TextView
    private lateinit var enterButton: Button
    private lateinit var registerTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initView()
        onClickListener()
    }

    private fun initView() {
        emailEditText = findViewById(R.id.activity_sign_in_email_edit_text)
        passwordEditText = findViewById(R.id.activity_sign_in_password_edit_text)
        forgotPasswordTextView = findViewById(R.id.activity_sign_in_forgot_password_text_view)
        enterButton = findViewById(R.id.activity_sign_in_enter_button)
        registerTextView = findViewById(R.id.activity_sign_in_registration_text_view)
    }

    private fun onClickListener() {
        registerTextView.setOnClickListener {
            val registrationUserIntent = Intent(this, RegistrationActivity::class.java)
            startActivity(registrationUserIntent)
        }
    }
}
