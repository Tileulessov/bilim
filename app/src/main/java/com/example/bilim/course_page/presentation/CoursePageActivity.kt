package com.example.bilim.course_page.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.bilim.R
import com.example.bilim.common.Constants
import com.example.bilim.common.listeners.CourseClickListener
import com.example.bilim.course_content.presentation.CourseContentActivity
import com.example.bilim.course_page.data.models.CourseNameListModel
import com.example.bilim.course_page.presentation.view.CoursePageAdapter
import com.example.bilim.favoriteCourse.presentattion.FavoriteActivity
import com.example.bilim.user_profile.presentation.UserProfile
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class CoursePageActivity : AppCompatActivity(), CourseClickListener {

    private lateinit var searchEditText: EditText
    private lateinit var userNameTextView: TextView
    private lateinit var userProfileImageView: ImageView
    private lateinit var coursePageRecyclerView: RecyclerView
    private lateinit var coursePageAdapter: CoursePageAdapter
    private lateinit var favoriteButton: Button
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val mDataBase: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = mDataBase.collection("course")
    private lateinit var userName: String

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
                query = if (p0.toString().isBlank()) {
                    searchEditText.error = getString(R.string.search_error_text)
                    return
                } else {
                    collectionReference.orderBy("courseName").startAt(p0.toString())
                        .endAt(p0.toString() + "\uf8ff")
                }
                val firestoreRecyclerOptions: FirestoreRecyclerOptions<CourseNameListModel> =
                    FirestoreRecyclerOptions.Builder<CourseNameListModel>()
                        .setQuery(query, CourseNameListModel::class.java)
                        .build()
                coursePageAdapter.updateOptions(firestoreRecyclerOptions)
                coursePageAdapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(s: Editable?) {
                searchEditText.clearFocus()
                val firestoreRecyclerOptions: FirestoreRecyclerOptions<CourseNameListModel> =
                    FirestoreRecyclerOptions.Builder<CourseNameListModel>()
                        .setQuery(query, CourseNameListModel::class.java)
                        .build()
                coursePageAdapter.updateOptions(firestoreRecyclerOptions)
            }
        })
        navigateToUserProfile()
        navigateToFavorite()
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

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        coursePageRecyclerView = findViewById(R.id.activity_course_content_recycler_view)
        searchEditText = findViewById(R.id.activity_course_page_search_edit_text)
        userNameTextView = findViewById(R.id.activity_course_page_user_name_text_view)
        userProfileImageView = findViewById(R.id.activity_course_page_user_profile_image_view)
        favoriteButton = findViewById(R.id.activity_course_page_favorite_button)
        swipeRefreshLayout = findViewById(R.id.swipeRefresh)
        swipeRefreshLayout.setOnRefreshListener {
            searchEditText.error = null
            coursePageAdapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }
        getUserName()
    }

    private fun getUserName() {
        userName = getSavedUserName()!!
        userNameTextView.text = userNameTextView.context.getString(R.string.user_name, userName)
    }

    private fun getCourseList(text: String) {
        val query: Query =
            collectionReference
                .whereEqualTo("isChecked", true)
        collectionReference.orderBy("courseName").startAt(text).endAt(text + "\uf8ff")
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<CourseNameListModel> =
            FirestoreRecyclerOptions.Builder<CourseNameListModel>()
                .setQuery(query, CourseNameListModel::class.java)
                .build()

        coursePageAdapter = CoursePageAdapter(firestoreRecyclerOptions, this)
        coursePageRecyclerView.layoutManager = LinearLayoutManager(this)
        coursePageRecyclerView.adapter = coursePageAdapter
        updateAdapter(query)
    }

    private fun navigateToUserProfile() {
        userProfileImageView.setOnClickListener {
            startActivity(Intent(this, UserProfile::class.java))
        }
    }

    private fun navigateToFavorite() {
        favoriteButton.setOnClickListener {
            startActivity(Intent(this, FavoriteActivity::class.java))
        }
    }

    private fun getSavedUserName(): String? {
        val sharedPref: SharedPreferences =
            getSharedPreferences(Constants.USER_NAME_SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPref.getString(Constants.USER_NAME, "Welcome to Bilim")
    }

    private fun updateAdapter(query: Query) {
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<CourseNameListModel> =
            FirestoreRecyclerOptions.Builder<CourseNameListModel>()
                .setQuery(query, CourseNameListModel::class.java)
                .build()
        coursePageAdapter.updateOptions(firestoreRecyclerOptions)
    }
}