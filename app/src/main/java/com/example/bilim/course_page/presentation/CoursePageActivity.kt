package com.example.bilim.course_page.presentation

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
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
    private lateinit var searchEditText: EditText
    private lateinit var userNameTextView: TextView
    private lateinit var coursePageRecyclerView: RecyclerView
    private lateinit var coursePageAdapter: CoursePageAdapter
    private val mDataBase: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = mDataBase.collection("course")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_page)
        initViews()
        getCourseList("")
        searchEditText.addTextChangedListener(object : TextWatcher {
            lateinit var query: Query
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                query = if(s.toString().isBlank()){
                    collectionReference
                    searchEditText.error  = getString(R.string.search_error_text)
                    searchEditText.requestFocus()
                    return
                } else{
                    collectionReference.orderBy("courseName").startAt(s.toString()).endAt(s.toString()+"\uf8ff")
                }

                val firestoreRecyclerOptions: FirestoreRecyclerOptions<CourseNameListModel> =
                        FirestoreRecyclerOptions.Builder<CourseNameListModel>()
                                .setQuery(query, CourseNameListModel::class.java)
                                .build()
                coursePageAdapter.updateOptions(firestoreRecyclerOptions)
            }

        })
    }

    override fun onStart() {
        super.onStart()
        coursePageAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        coursePageAdapter.stopListening()
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

    private fun initViews() {
        coursePageRecyclerView = findViewById(R.id.activity_course_content_recycler_view)
        searchEditText = findViewById(R.id.activity_course_page_search_edit_text)
        userNameTextView = findViewById(R.id.activity_course_page_user_name_text_view)
    }

    private fun getCourseList(text: String) {
        val query: Query = collectionReference.orderBy("courseName").startAt(text).endAt(text+"\uf8ff")
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<CourseNameListModel> =
            FirestoreRecyclerOptions.Builder<CourseNameListModel>()
                .setQuery(query, CourseNameListModel::class.java)
                .build()

        coursePageAdapter = CoursePageAdapter(firestoreRecyclerOptions, this)
        coursePageRecyclerView.layoutManager = LinearLayoutManager(this)
        coursePageRecyclerView.adapter = coursePageAdapter
    }
}