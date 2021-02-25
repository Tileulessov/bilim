package com.example.bilim.course_page.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.bilim.R
import com.example.bilim.course_page.data.models.CourseNameListModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class CoursePageAdapter(options: FirestoreRecyclerOptions<CourseNameListModel>) :
    FirestoreRecyclerAdapter<CourseNameListModel, CoursePageViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursePageViewHolder {
        return CoursePageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.course_item, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: CoursePageViewHolder,
        position: Int,
        model: CourseNameListModel
    ) {
        holder.courseName.text = model.courseName
        Glide.with(holder.courseIcon.context)
                .load(model.iconUrl)
                .into(holder.courseIcon)
    }
}