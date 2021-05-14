package com.example.bilim.favoriteCourse.presentattion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bilim.R
import com.example.bilim.common.Constants
import com.example.bilim.common.listeners.CourseClickListener
import com.example.bilim.course_content.presentation.CourseContentActivity
import com.example.bilim.course_page.data.models.CourseNameListModel
import com.example.bilim.course_page.presentation.CoursePageActivity
import com.example.bilim.favoriteCourse.presentattion.view.FavoriteAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FavoriteActivity : AppCompatActivity(), CourseClickListener {

    private lateinit var toolbar: Toolbar
    private lateinit var emptyCourseLinear: LinearLayout
    private lateinit var favoriteCourseLinear: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var coursePageRecyclerView: RecyclerView
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var homeButton: Button
    private lateinit var textView: TextView
    private val mDataBase: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = mDataBase.collection("course")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        initViews()
        getFavoriteCourseList()
        onBackPress()
        navigateToHome()
    }

    override fun onStart() {
        super.onStart()
        favoriteAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        favoriteAdapter.stopListening()
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
        coursePageRecyclerView = findViewById(R.id.activity_favorite_recycler_view)
        toolbar = findViewById(R.id.activity_favorite_toolbar)
        emptyCourseLinear = findViewById(R.id.activity_favorite_empty_course_linear)
        favoriteCourseLinear = findViewById(R.id.activity_favorite_has_course_linear)
        progressBar = findViewById(R.id.activity_favorite_progressBar)
        homeButton = findViewById(R.id.activity_favorite_home_button)
        textView = findViewById(R.id.activity_favorite_no_course_text_view)
    }

    private fun onBackPress() {
        toolbar.setOnClickListener {
            finish()
        }
    }

    private fun getFavoriteCourseList() {
        val query: Query =
            collectionReference
                .whereEqualTo("isFavorite", true)
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<CourseNameListModel> =
            FirestoreRecyclerOptions.Builder<CourseNameListModel>()
                .setQuery(query, CourseNameListModel::class.java)
                .build()

        favoriteAdapter = FavoriteAdapter(firestoreRecyclerOptions, this)
        coursePageRecyclerView.layoutManager = LinearLayoutManager(this)
        coursePageRecyclerView.adapter = favoriteAdapter
        checkFavoriteExist()
    }

    private fun navigateToHome() {
        homeButton.setOnClickListener {
            startActivity(Intent(this, CoursePageActivity::class.java))
        }
    }

    private fun checkFavoriteExist() {
        favoriteAdapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                if (itemCount == 0) {
                    emptyCourseLinear.visibility = View.VISIBLE
                    favoriteCourseLinear.visibility = View.GONE
                    homeButton.isVisible = true
                } else {
                    favoriteCourseLinear.visibility = View.VISIBLE
                    emptyCourseLinear.visibility = View.GONE
                    homeButton.isVisible = false
                }
            }
        })
    }
}