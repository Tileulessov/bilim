package com.example.bilim.course_page.presentation.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.bilim.R
import com.example.bilim.common.view.BaseViewHolder
import com.example.bilim.course_page.data.models.CourseNameListModel

class CoursePageViewHolder(itemView: View) : BaseViewHolder<CourseNameListModel>(itemView) {
   private var courseName: TextView = itemView.findViewById(R.id.course_item_title_text_view)
   private var courseIcon: ImageView = itemView.findViewById(R.id.course_item_number_text_view)

    override fun onBind(model: CourseNameListModel) {
        courseName.text = model.courseName
        Glide.with(courseIcon.context)
                .load(model.iconUrl)
                .into(courseIcon)
    }
}