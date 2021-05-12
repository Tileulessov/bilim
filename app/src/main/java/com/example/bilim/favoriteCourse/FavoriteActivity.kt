package com.example.bilim.favoriteCourse

import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.example.bilim.R

class FavoriteActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        initViews()
        onClickListener()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.activity_favorite_toolbar)
    }

    private fun onClickListener() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}