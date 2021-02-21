package com.example.bilim.registration

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.bilim.R
import com.example.bilim.sign.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

private const val DB_REFERENCE_PATH = "users"
class RegistrationActivity : AppCompatActivity() {
    private lateinit var logoTextView: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fullNameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var textWatcher: TextWatcher
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        initView()
        onClickListener()
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val isInputEmpty: Boolean = p0.toString().isBlank()
                registerButton.isEnabled = !isInputEmpty
            }
        }
        fullNameEditText.addTextChangedListener(textWatcher)
        ageEditText.addTextChangedListener(textWatcher)
        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
    }

    private fun initView() {
        logoTextView = findViewById(R.id.activity_registration_logo_text_view)
        fullNameEditText = findViewById(R.id.activity_registration_full_name_edit_text)
        ageEditText = findViewById(R.id.activity_registration_age_edit_text)
        emailEditText = findViewById(R.id.activity_registration_email_edit_text)
        passwordEditText = findViewById(R.id.activity_registration_password_edit_text)
        registerButton = findViewById(R.id.activity_registration_register_button)
        progressBar = findViewById(R.id.activity_registration_progress_bar)
        mAuth = FirebaseAuth.getInstance()
    }

    private fun onClickListener() {
        logoTextView.setOnClickListener {
            val signInIntent = Intent(this, SignInActivity::class.java)
            startActivity(signInIntent)
        }

        registerButton.setOnClickListener {
            registerNewUser()
        }
    }

    private fun registerNewUser() {
        val email: String = emailEditText.text.toString().trim()
        val fullName: String = fullNameEditText.text.toString().trim()
        val password: String = passwordEditText.text.toString().trim()
        val age: String = ageEditText.text.toString().trim()

        if (fullName.isEmpty()) {
            fullNameEditText.error = getString(R.string.full_name_empty_error_text)
            fullNameEditText.requestFocus()
            return
        }

        if (age.isEmpty()) {
            ageEditText.error = getString(R.string.age_empty_error_text)
            ageEditText.requestFocus()
            return
        }
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
        performUserRegister(email, password)
    }

    private fun performUserRegister(email: String, password: String) {
        progressBar.isVisible = true
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) return@addOnCompleteListener
                    else {
                        val user = User(email, password)
                        Log.d("RegisterActivity", "Successfully created user with uid: ${task.result?.user?.uid}")
                        FirebaseDatabase.getInstance().getReference(DB_REFERENCE_PATH)
                                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                .setValue(user).addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        progressBar.isVisible = false
                                        Toast.makeText(this, getString(R.string.successfully_created_user_text), Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this,SignInActivity::class.java))
                                    } else {
                                        progressBar.isVisible = false
                                        Toast.makeText(this, getString(R.string.failed_to_create_user_text), Toast.LENGTH_SHORT).show()
                                    }
                                }
                    }
                }
                .addOnFailureListener {
                    progressBar.isVisible = false
                    Log.d("RegisterActivity", "Failed to create user: ${it.message}")
                    Toast.makeText(this, getString(R.string.failed_to_create_user_text), Toast.LENGTH_SHORT).show()
                }
    }
}