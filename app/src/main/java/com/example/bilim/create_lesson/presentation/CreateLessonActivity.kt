package com.example.bilim.create_lesson.presentation

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bilim.R

class CreateLessonActivity : AppCompatActivity() {

    private lateinit var autoCompleteTextView: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_lesson)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        val lessonType = resources.getStringArray(R.array.lesson_type)
        val typeArrayAdapter = ArrayAdapter(this, R.layout.item_dropdown, lessonType)
        autoCompleteTextView.setAdapter(typeArrayAdapter)
    }

    private fun initViews() {
        autoCompleteTextView = findViewById(R.id.activity_create_lesson_auto_text_view)
    }
}