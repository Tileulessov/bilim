package com.example.bilim.sign

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.bilim.R
import com.example.bilim.course_page.CoursePageActivity
import com.example.bilim.forgot_password.UserForgotPasswordActivity
import com.example.bilim.registration.RegistrationActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignInActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var forgotPasswordTextView: TextView
    private lateinit var signInButton: Button
    private lateinit var registerTextView: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var textWatcher: TextWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initView()
        onClickListener()
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val isInputEmpty: Boolean = p0.toString().isBlank()
                signInButton.isEnabled = !isInputEmpty
            }
        }
        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
        navigateToForgotPassword()
    }

    private fun initView() {
        emailEditText = findViewById(R.id.activity_sign_in_email_edit_text)
        passwordEditText = findViewById(R.id.activity_sign_in_password_edit_text)
        forgotPasswordTextView = findViewById(R.id.activity_sign_in_forgot_password_text_view)
        signInButton = findViewById(R.id.activity_sign_in_enter_button)
        registerTextView = findViewById(R.id.activity_sign_in_registration_text_view)
        progressBar = findViewById(R.id.activity_sign_in_progress_bar)
        mAuth = FirebaseAuth.getInstance()
    }

    private fun onClickListener() {
        registerTextView.setOnClickListener {
            val registrationUserIntent = Intent(this, RegistrationActivity::class.java)
            startActivity(registrationUserIntent)
        }
        signInButton.setOnClickListener {
            userSignIn()
        }
    }

    private fun userSignIn() {
        val email: String = emailEditText.text.toString().trim()
        val password: String = passwordEditText.text.toString().trim()

        if (email.isEmpty()) {
            emailEditText.error = getString(R.string.email_empty_error_text)
            emailEditText.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = getString(R.string.invalid_email_error_text)
            emailEditText.requestFocus()
            return
        }
        if (password.isEmpty()) {
            passwordEditText.error = getString(R.string.password_empty_error_text)
            passwordEditText.requestFocus()
            return
        }
        if (password.length < 8) {
            passwordEditText.error = getString(R.string.password_length_error_text)
            passwordEditText.requestFocus()
            return
        }
        progressBar.isVisible = true
        performUserSignIn(email, password)
    }

    private fun performUserSignIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser = mAuth.currentUser!!
                    if (user.isEmailVerified){
                        progressBar.isVisible = false
                        startActivity(Intent(this, CoursePageActivity::class.java))
                    }else{
                        user.sendEmailVerification()
                        progressBar.isVisible = false
                        Toast.makeText(this,getString(R.string.verify_email_text),Toast.LENGTH_LONG).show()
                    }
                } else {
                    progressBar.isVisible = false
                    Toast.makeText(
                            this,
                            getString(R.string.failed_to_login_text),
                            Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun navigateToForgotPassword() {
        forgotPasswordTextView.setOnClickListener {
            startActivity(Intent(this, UserForgotPasswordActivity::class.java))
        }
    }
}
