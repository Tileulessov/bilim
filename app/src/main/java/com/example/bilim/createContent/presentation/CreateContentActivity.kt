package com.example.bilim.createContent.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.bilim.R
import com.example.bilim.common.Constants
import com.example.bilim.common.listeners.CourseClickListener
import com.example.bilim.course_content.presentation.CourseContentActivity
import com.example.bilim.course_page.data.models.CourseNameListModel
import com.example.bilim.createContent.presentation.view.CreateContentAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class CreateContentActivity : AppCompatActivity(), CourseClickListener {

    private lateinit var contentAdapter: CreateContentAdapter
    private lateinit var contentRecycler: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val fStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val fAuth = FirebaseAuth.getInstance()
    private val collectionReference: CollectionReference = fStore.collection("course")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_content)

        initViews()
        onBack()
        onClickListener()
        getUserCourse()
    }

    override fun onStart() {
        super.onStart()
        contentAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        contentAdapter.stopListening()
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
        contentRecycler = findViewById(R.id.activity_create_content_recycler_view)
        toolbar = findViewById(R.id.activity_create_content_toolbar)
        swipeRefreshLayout = findViewById(R.id.activity_create_content_swipeRefresh)
    }

    private fun onClickListener() {
    }

    private fun onBack() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun getUserCourse() {
        val fUser = fAuth.currentUser!!
        val query: Query = collectionReference.whereEqualTo("contentMaker", fUser.uid)
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<CourseNameListModel> =
            FirestoreRecyclerOptions.Builder<CourseNameListModel>()
                .setQuery(query, CourseNameListModel::class.java)
                .build()

        contentAdapter = CreateContentAdapter(firestoreRecyclerOptions, this)
        contentRecycler.layoutManager = LinearLayoutManager(this)
        contentRecycler.adapter = contentAdapter
    }
}