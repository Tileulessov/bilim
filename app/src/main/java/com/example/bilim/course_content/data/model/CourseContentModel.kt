package com.example.bilim.course_content.data.model

import com.google.firebase.firestore.PropertyName

data class CourseContentModel(
        @PropertyName("courseId")
        val courseId: Int? = 0,
        @PropertyName("courseTitle")
        val courseTitle: String? = "",
        @PropertyName("courseType")
        val courseType: String? = "",
        @PropertyName("content")
        val content: String? = "",
        @PropertyName("pdfUrl")
        val pdfUrl: String? = ""
)