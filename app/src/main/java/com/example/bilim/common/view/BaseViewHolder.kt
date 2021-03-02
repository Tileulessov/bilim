package com.example.bilim.common.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
abstract fun onBind(model: T, snapshot: DocumentSnapshot)
}