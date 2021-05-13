package com.example.bilim.course_page.data.models

import com.google.firebase.firestore.PropertyName

data class CourseNameListModel(
    @PropertyName("courseName")
    var courseName: String = "",
    @PropertyName("iconUrl")
    var iconUrl: String = "",
    @PropertyName("isFavorite")
    var isFavorite: Boolean = false,
    @PropertyName("isChecked")
    var isChecked: Boolean = false,
    @PropertyName("contentMaker")
    var contentMaker: String = ""
)