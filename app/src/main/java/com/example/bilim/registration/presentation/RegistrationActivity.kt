package com.example.bilim.registration.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import com.example.bilim.R
import com.example.bilim.common.Constants
import com.example.bilim.common.dataSourse.SharedPrefDataSource
import com.example.bilim.sign.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.HashMap

class RegistrationActivity : AppCompatActivity() {

    private lateinit var logoTextView: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fullNameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var userHaveAcc: TextView
    private lateinit var fStore: FirebaseFirestore
    private lateinit var agreementDescTextView: TextView
    private lateinit var agreementSwitch: SwitchCompat
    private lateinit var teacherRadioButton: RadioButton
    private lateinit var studentRadioButton: RadioButton
    private lateinit var radioGroup: RadioGroup
    private var isTeacher: Boolean = false
    private var valid: Boolean = false
    private val userNameSharedPref: SharedPrefDataSource by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        initView()
        onClickListener()
    }

    private fun initView() {
        logoTextView = findViewById(R.id.activity_registration_logo_text_view)
        fullNameEditText = findViewById(R.id.activity_registration_full_name_edit_text)
        ageEditText = findViewById(R.id.activity_registration_age_edit_text)
        emailEditText = findViewById(R.id.activity_registration_email_edit_text)
        passwordEditText = findViewById(R.id.activity_registration_password_edit_text)
        registerButton = findViewById(R.id.activity_registration_register_button)
        progressBar = findViewById(R.id.activity_registration_progress_bar)
        userHaveAcc = findViewById(R.id.activity_registration_user_have_account_text_view)
        agreementDescTextView =
            findViewById(R.id.activity_registration_register_agreement_description_text_view)
        agreementSwitch = findViewById(R.id.activity_registration_register_agreement_switch)
        teacherRadioButton = findViewById(R.id.activity_registration_register_teacher_radio_button)
        studentRadioButton = findViewById(R.id.activity_registration_register_student_radio_button)
        radioGroup = findViewById(R.id.activity_registration_register_radio_group)
        mAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
    }

    private fun onClickListener() {
        logoTextView.setOnClickListener {
            val signInIntent = Intent(this, SignInActivity::class.java)
            startActivity(signInIntent)
        }
        checkUserAccessLevel()
        userHaveAcc.setOnClickListener {
            val signInIntent = Intent(this, SignInActivity::class.java)
            startActivity(signInIntent)
        }
    }

    private fun checkFieldAndRegister() {
        val email: String = emailEditText.text.toString().trim()
        val fullName: String = fullNameEditText.text.toString().trim()
        val password: String = passwordEditText.text.toString().trim()
        val age: String = ageEditText.text.toString().trim()

        checkEditTextField(emailEditText)
        checkEditTextField(fullNameEditText)
        checkEditTextField(passwordEditText)
        checkEditTextField(ageEditText)

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = getString(R.string.invalid_email_error_text)
            emailEditText.requestFocus()
            return
        }
        if (password.length < 8) {
            passwordEditText.error = getString(R.string.password_length_error_text)
            passwordEditText.requestFocus()
            return
        }
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            studentRadioButton.error = "Please check"
            teacherRadioButton.error = "Please check"
            studentRadioButton.requestFocus()
            teacherRadioButton.requestFocus()
            return
        }
        saveUserNameSharedPref(fullName)
        performUserRegister(email, password)
    }

    private fun performUserRegister(email: String, password: String) {
        progressBar.isVisible = true
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) return@addOnCompleteListener
                else {
                    val fUser: FirebaseUser = mAuth.currentUser!!
                    val df = fStore.collection("Users").document(fUser.uid)
                    val userInfo = HashMap<String, Any>()
                    fUser.sendEmailVerification()
                    userInfo.put("fullname", fullNameEditText.text.toString())
                    userInfo.put("email", email)
                    userInfo.put("age", ageEditText.text.toString())
                    if (isTeacher) {
                        userInfo.put("isTeacher", "1")
                    } else {
                        userInfo.put("isUser", "1")
                    }
                    df.set(userInfo)
                    Toast.makeText(
                        this,
                        getString(R.string.successfully_created_user_text),
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this, SignInActivity::class.java))

                    FirebaseAuth.getInstance().signOut()
                    finish()
                }
            }
            .addOnFailureListener {
                progressBar.isVisible = false
                Log.d("RegisterActivity", "Failed to create user: ${it.message}")
                Toast.makeText(
                    this,
                    getString(R.string.failed_to_create_user_text),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun saveUserNameSharedPref(userName: String) =
        userNameSharedPref.saveValue(Constants.USER_NAME_SHARED_PREF, userName)

    private fun checkUserAccessLevel(): Boolean {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.activity_registration_register_teacher_radio_button) {
                registerButton.text = getString(R.string.video_verification_text)
                agreementDescTextView.visibility = View.VISIBLE
                agreementSwitch.visibility = View.VISIBLE
                registerButton.setOnClickListener {
                    if (agreementSwitch.isChecked)
                        checkFieldAndRegister()
                }
                registerButton.isEnabled = true
                isTeacher = true
            } else if (checkedId == R.id.activity_registration_register_student_radio_button) {
                registerButton.text = getString(R.string.register_button_text)
                registerButton.setOnClickListener {
                    checkFieldAndRegister()
                }
                registerButton.isEnabled = true
                isTeacher = false
            }
        }
        return isTeacher
    }

    private fun checkEditTextField(textField: EditText) {
        if (textField.text.toString().isBlank()) {
            textField.error = "This is a required field"
            textField.requestFocus()
            return
            valid = false
        } else valid = true
    }
}