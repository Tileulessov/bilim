package com.example.bilim.course_page.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bilim.R
import com.example.bilim.course_page.data.models.CourseNameListModel
import com.example.bilim.course_page.view.CoursePageAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class CoursePageActivity : AppCompatActivity() {
    private lateinit var coursePageRecyclerView: RecyclerView
    private lateinit var coursePageAdapter: CoursePageAdapter
    private val mDataBase: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference:CollectionReference = mDataBase.collection("course")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_page)
        initViews()
    }

    override fun onStart() {
        super.onStart()
        coursePageAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        coursePageAdapter.stopListening()
    }

    private fun initViews() {
        coursePageRecyclerView = findViewById(R.id.activity_course_page_recycler_view)
        val query:Query = collectionReference
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<CourseNameListModel> = FirestoreRecyclerOptions.Builder<CourseNameListModel>()
            .setQuery(query, CourseNameListModel::class.java)
            .build()
        coursePageAdapter = CoursePageAdapter(firestoreRecyclerOptions)
        coursePageRecyclerView.layoutManager = LinearLayoutManager(this)
        coursePageRecyclerView.adapter = coursePageAdapter
    }
}