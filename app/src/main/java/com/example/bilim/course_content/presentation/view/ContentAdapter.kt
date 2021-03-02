package com.example.bilim.course_content.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.bilim.R
import com.example.bilim.common.listeners.ContentClickListener
import com.example.bilim.course_content.data.model.CourseContentModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ContentAdapter(
        options: FirestoreRecyclerOptions<CourseContentModel>,
        private val contentClickListener: ContentClickListener
) : FirestoreRecyclerAdapter<CourseContentModel, ContentViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        return ContentViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(
                                R.layout.course_list_item,
                                parent,
                                false
                        ),
                contentClickListener = contentClickListener
        )
    }

    override fun onBindViewHolder(
            holder: ContentViewHolder,
            position: Int,
            model: CourseContentModel
    ) {
        val snapshot = snapshots.getSnapshot(position)
        holder.onBind(model, snapshot)
    }
}