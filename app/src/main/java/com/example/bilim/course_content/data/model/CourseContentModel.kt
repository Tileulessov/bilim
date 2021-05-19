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
        val pdfUrl: String? = "",
        @PropertyName("courseName")
        val courseName: String? = "",
        @PropertyName("question")
        val question: String? = "",
        @PropertyName("answer")
        val answer: String? = "",
        @PropertyName("optionFirst")
        val optionFirst: String? = "",
        @PropertyName("optionSecond")
        val optionSecond: String? = "",
        @PropertyName("optionThird")
        val optionThird: String? = "",
        @PropertyName("optionFourth")
        val optionFourth: String? = "",
)