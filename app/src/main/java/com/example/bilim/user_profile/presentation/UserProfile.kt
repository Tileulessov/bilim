package com.example.bilim.user_profile.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.bilim.R
import com.example.bilim.registration.User
import com.example.bilim.sign.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserProfile : AppCompatActivity() {

    private lateinit var logoutButton:Button
    private lateinit var userNameTextView:TextView
    private lateinit var  userAgeTextView:TextView
    private lateinit var userEmailTextView:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        initViews()
        userDetails()
        logoutProfile()
    }

    private fun initViews(){
        logoutButton = findViewById(R.id.activty_user_profile_logout_button)
        userNameTextView = findViewById(R.id.activity_user_profile_user_name_text_view)
        userAgeTextView = findViewById(R.id.activity_user_profile_user_age_text_view)
        userEmailTextView = findViewById(R.id.activity_user_profile_user_email_text_view)
    }


    private fun logoutProfile(){
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,SignInActivity::class.java))
        }
    }

    private fun userDetails(){
    }
}