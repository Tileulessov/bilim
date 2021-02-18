package com.example.bilim.sign

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bilim.R
import com.google.firebase.auth.FirebaseAuth


class SignInActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initFirebaseAuth()
    }


    private fun initFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();
    }
}
