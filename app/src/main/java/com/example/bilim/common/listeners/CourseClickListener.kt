package com.example.bilim.common.listeners

import com.example.bilim.course_page.data.models.CourseNameListModel
import com.google.firebase.firestore.DocumentSnapshot

interface CourseClickListener {
    fun onCourseClick(documentSnapshot: DocumentSnapshot, position:Int,model: CourseNameListModel)
}