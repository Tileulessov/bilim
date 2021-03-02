package com.example.bilim.course_content.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bilim.R

class CourseContentActivity : AppCompatActivity() {
    
    private lateinit var courseIcon:ImageView
    private lateinit var courseName:TextView
    private lateinit var courseTitleRecycler: RecyclerView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_content)
        
        initViews()
        val name: String? = intent.getStringExtra("key")
        courseName.text = name
    }
    
    private fun initViews() {
        courseIcon = findViewById(R.id.activity_course_content_course_icon_image_view)
        courseName = findViewById(R.id.activity_course_content_course_name_text_view)
        courseTitleRecycler = findViewById(R.id.activity_course_content_recycler_view)
    }
    
}