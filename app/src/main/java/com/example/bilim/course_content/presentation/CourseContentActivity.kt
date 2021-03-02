package com.example.bilim.course_content.presentation

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bilim.R
import com.example.bilim.common.Constants

class CourseContentActivity : AppCompatActivity() {

    private lateinit var courseIcon: ImageView
    private lateinit var courseName: TextView
    private lateinit var courseTitleRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_content)

        initViews()
        val name: String? = intent.getStringExtra(Constants.COURSE_NAME)
        courseName.text = name
        val img = intent.extras?.getString(Constants.COURSE_ICON)
        Glide.with(applicationContext)
                .load(img)
                .into(courseIcon)
    }

    private fun initViews() {
        courseIcon = findViewById(R.id.activity_course_content_course_icon_image_view)
        courseName = findViewById(R.id.activity_course_content_course_name_text_view)
        courseTitleRecycler = findViewById(R.id.activity_course_content_recycler_view)
    }
}