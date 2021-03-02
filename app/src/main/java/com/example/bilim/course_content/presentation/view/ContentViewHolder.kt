package com.example.bilim.course_content.presentation.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bilim.R
import com.example.bilim.common.listeners.ContentClickListener
import com.example.bilim.common.view.BaseViewHolder
import com.example.bilim.course_content.data.model.CourseContentModel
import com.google.firebase.firestore.DocumentSnapshot

class ContentViewHolder(
        itemView: View,
        private val contentClickListener: ContentClickListener
) : BaseViewHolder<CourseContentModel>(itemView) {
    private val sequenceNumber: TextView = itemView.findViewById(R.id.course_list_item_number_text_view)
    private val title: TextView = itemView.findViewById(R.id.course_list_item_title_text_view)
    private val contentType: TextView = itemView.findViewById(R.id.course_list_item_type_text_view)

    override fun onBind(model: CourseContentModel, snapshot: DocumentSnapshot) {
        sequenceNumber.text = model.courseId.toString()
        title.text = model.courseTitle
        contentType.text = model.courseType
        itemView.setOnClickListener {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                contentClickListener.onContentClick(snapshot, position, model)
            }
        }
    }
}