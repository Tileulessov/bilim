package com.example.bilim.createContent.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.bilim.R
import com.example.bilim.common.listeners.CourseClickListener
import com.example.bilim.course_page.data.models.CourseNameListModel
import com.example.bilim.course_page.presentation.view.CoursePageViewHolder
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class CreateContentAdapter(
    options: FirestoreRecyclerOptions<CourseNameListModel>,
    private val courseClickListener: CourseClickListener
) :
    FirestoreRecyclerAdapter<CourseNameListModel, CreateContentViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateContentViewHolder {
        return CreateContentViewHolder(
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
        holder: CreateContentViewHolder,
        position: Int,
        model: CourseNameListModel
    ) {
        val snapshot = snapshots.getSnapshot(position)
        holder.onBind(model, snapshot)
    }
}