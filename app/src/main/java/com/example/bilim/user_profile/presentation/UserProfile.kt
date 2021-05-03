package com.example.bilim.user_profile.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.bilim.R
import com.example.bilim.sign.SignInActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class UserProfile : AppCompatActivity() {

    private lateinit var logoutButton: Button
    private lateinit var userNameTextView: TextView
    private lateinit var userAgeTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var fStore: FirebaseFirestore
    private lateinit var fAuth: FirebaseAuth
    private lateinit var df: DocumentReference
    private lateinit var createCourseButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        initViews()

        logoutProfile()
        val fUser = fAuth.currentUser!!
        userDetails(fUser.uid)
        checkUserAccessLevel(fUser.uid)
    }

    private fun initViews() {
        logoutButton = findViewById(R.id.activty_user_profile_logout_button)
        userNameTextView = findViewById(R.id.activity_user_profile_user_name_text_view)
        userAgeTextView = findViewById(R.id.activity_user_profile_user_age_text_view)
        userEmailTextView = findViewById(R.id.activity_user_profile_user_email_text_view)
        createCourseButton = findViewById(R.id.activty_user_profile_create_button)
        progressBar = findViewById(R.id.activity_user_profile_progress_bar)
        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
    }

    private fun logoutProfile() {
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    private fun userDetails(uid: String) {
        progressBar.isVisible = true
        df = fStore.collection("Users").document(uid)
        df.get().addOnSuccessListener {
            userNameTextView.text = it.getString("fullname")
            userAgeTextView.text = it.getString("age")
            userEmailTextView.text = it.getString("email")
        }
        progressBar.isVisible = false
    }

    private fun checkUserAccessLevel(uid: String) {
        df = fStore.collection("Users").document(uid)
        df.get().addOnSuccessListener {
            if (it.getString("isTeacher") != null) {
                createCourseButton.visibility = View.VISIBLE
            }
        }
    }
}