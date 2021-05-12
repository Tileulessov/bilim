package com.example.bilim.course_page.presentation.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bilim.R
import com.example.bilim.common.listeners.CourseClickListener
import com.example.bilim.common.view.BaseViewHolder
import com.example.bilim.course_page.data.models.CourseNameListModel
import com.google.firebase.firestore.DocumentSnapshot

class CoursePageViewHolder(
    itemView: View,
    private val courseClickListener: CourseClickListener
) : BaseViewHolder<CourseNameListModel>(itemView) {

    private var courseName: TextView = itemView.findViewById(R.id.course_item_title_text_view)
    private var courseIcon: ImageView = itemView.findViewById(R.id.course_item_number_text_view)

    override fun onBind(model: CourseNameListModel, snapshot: DocumentSnapshot) {
            courseName.text = model.courseName
            Glide.with(courseIcon.context)
                .load(model.iconUrl)
                .into(courseIcon)
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    courseClickListener.onCourseClick(snapshot, position, model)
                }
            }
    }
}