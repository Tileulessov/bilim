package com.example.bilim.course_page.presentation.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.bilim.R
import com.example.bilim.common.listeners.CourseClickListener
import com.example.bilim.course_content.presentation.CourseContentActivity
import com.example.bilim.course_page.data.models.CourseNameListModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class CoursePageAdapter(
        options: FirestoreRecyclerOptions<CourseNameListModel>,
        private val courseClickListener: CourseClickListener
) :
        FirestoreRecyclerAdapter<CourseNameListModel, CoursePageViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursePageViewHolder {
        return CoursePageViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(
                                R.layout.course_item,
                                parent,
                                false
                        ),
                courseClickListener = courseClickListener
        )
    }

    override fun onBindViewHolder(
            holder: CoursePageViewHolder,
            position: Int,
            model: CourseNameListModel
    ) {
        val snapshot = snapshots.getSnapshot(position)
        holder.onBind(model, snapshot)
    }
}