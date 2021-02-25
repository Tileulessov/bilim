package com.example.bilim.common.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.bilim.course_page.data.models.CourseNameListModel

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
abstract fun onBind(model: T)
}