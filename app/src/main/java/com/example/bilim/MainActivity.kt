package com.example.bilim

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_BilimOnlineCourse)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}