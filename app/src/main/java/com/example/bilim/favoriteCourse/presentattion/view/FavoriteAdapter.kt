package com.example.bilim.favoriteCourse.presentattion.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.bilim.R
import com.example.bilim.common.listeners.CourseClickListener
import com.example.bilim.course_page.data.models.CourseNameListModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

open class FavoriteAdapter(
    options: FirestoreRecyclerOptions<CourseNameListModel>,
    private val courseClickListener: CourseClickListener
) :
    FirestoreRecyclerAdapter<CourseNameListModel, FavoriteViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
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
        holder: FavoriteViewHolder,
        position: Int,
        model: CourseNameListModel
    ) {
        val snapshot = snapshots.getSnapshot(position)
        holder.onBind(model, snapshot)
    }
}