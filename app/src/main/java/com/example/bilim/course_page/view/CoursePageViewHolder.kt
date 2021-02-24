package com.example.bilim.course_page.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bilim.R

class CoursePageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var courseName: TextView = itemView.findViewById(R.id.course_item_course_name_text_view)
    var courseIcon: ImageView = itemView.findViewById(R.id.course_item_icon_image_view)
}