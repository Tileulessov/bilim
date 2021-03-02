package com.example.bilim.common.listeners

import com.example.bilim.course_content.data.model.CourseContentModel
import com.google.firebase.firestore.DocumentSnapshot

interface ContentClickListener {
    fun onContentClick(documentSnapshot: DocumentSnapshot, position: Int, model: CourseContentModel)
}