package com.example.bilim.course_page.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bilim.R
import com.example.bilim.common.Constants
import com.example.bilim.common.listeners.CourseClickListener
import com.example.bilim.course_content.presentation.CourseContentActivity
import com.example.bilim.course_page.data.models.CourseNameListModel
import com.example.bilim.course_page.presentation.view.CoursePageAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class CoursePageActivity : AppCompatActivity(), CourseClickListener {
    private lateinit var coursePageRecyclerView: RecyclerView
    private lateinit var coursePageAdapter: CoursePageAdapter
    private val mDataBase: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = mDataBase.collection("course")

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
        coursePageRecyclerView = findViewById(R.id.activity_course_content_recycler_view)
        val query: Query = collectionReference
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<CourseNameListModel> = FirestoreRecyclerOptions.Builder<CourseNameListModel>()
                .setQuery(query, CourseNameListModel::class.java)
                .build()

        coursePageAdapter = CoursePageAdapter(firestoreRecyclerOptions, this)
        coursePageRecyclerView.layoutManager = LinearLayoutManager(this)
        coursePageRecyclerView.adapter = coursePageAdapter
    }

    override fun onCourseClick(
            documentSnapshot: DocumentSnapshot,
            position: Int,
            model: CourseNameListModel
    ) {
        val id = documentSnapshot.id
        Toast.makeText(this, "Position: $position ID: $id", Toast.LENGTH_SHORT).show()
        val intent: Intent = Intent(this, CourseContentActivity::class.java)
        intent.putExtra(Constants.COURSE_NAME, model.courseName)
        intent.putExtra(Constants.COURSE_ICON, model.iconUrl)
        startActivity(intent)
    }
}