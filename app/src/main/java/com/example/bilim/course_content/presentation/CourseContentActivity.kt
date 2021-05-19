package com.example.bilim.course_content.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bilim.R
import com.example.bilim.common.Constants
import com.example.bilim.common.listeners.ContentClickListener
import com.example.bilim.course_content.data.model.CourseContentModel
import com.example.bilim.course_content.presentation.view.ContentAdapter
import com.example.bilim.create_lesson.presentation.CreateLessonActivity
import com.example.bilim.lesson.presentation.LessonActivity
import com.example.bilim.lesson.videoLesson.presentation.VideoLessonActivity
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class CourseContentActivity : AppCompatActivity(), ContentClickListener {

    private lateinit var courseIcon: ImageView
    private lateinit var courseName: TextView
    private lateinit var courseTitleRecycler: RecyclerView
    private val fStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var collectionReference: CollectionReference
    private lateinit var documentReference: DocumentReference
    private lateinit var contentAdapter: ContentAdapter
    private lateinit var toolbar: Toolbar
    private lateinit var followButton: Button
    private var isFavorite: Boolean? = false
    private lateinit var bgImageView: ImageView
    private lateinit var addFab: FloatingActionButton
    private val fAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_content)

        initViews()
        getCourseDetails()
        onBack()
    }

    override fun onStart() {
        super.onStart()
        contentAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        contentAdapter.stopListening()
    }

    private fun initViews() {
        courseIcon = findViewById(R.id.activity_course_content_course_icon_image_view)
        courseName = findViewById(R.id.activity_course_content_course_name_text_view)
        courseTitleRecycler = findViewById(R.id.activity_course_content_recycler_view)
        toolbar = findViewById(R.id.activity_course_content_toolbar)
        followButton = findViewById(R.id.activity_course_content_follow_button)
        bgImageView = findViewById(R.id.activity_course_content_bg_image_view)
        addFab = findViewById(R.id.activity_course_content_fab)
    }

    private fun onBack() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun getCourseDetails() {
        val name: String? = intent.getStringExtra(Constants.COURSE_NAME)
        courseName.text = name
        val img = intent.extras?.getString(Constants.COURSE_ICON)
        Glide.with(applicationContext)
            .load(img)
            .into(courseIcon)
        getContent(name)
        onClickListener(name)
        checkState(name)
        setBackgroundImage(img)
        addButtonVisible(name)
    }

    private fun getContent(name: String?) {
        collectionReference = fStore.collection("course")
            .document(name!!)
            .collection(name)
        val query: Query = collectionReference.orderBy("courseId", Query.Direction.ASCENDING)
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<CourseContentModel> =
            FirestoreRecyclerOptions.Builder<CourseContentModel>()
                .setQuery(query, CourseContentModel::class.java)
                .build()
        contentAdapter = ContentAdapter(firestoreRecyclerOptions, this)
        courseTitleRecycler.layoutManager = LinearLayoutManager(this)
        courseTitleRecycler.adapter = contentAdapter
    }

    override fun onContentClick(
        documentSnapshot: DocumentSnapshot,
        position: Int,
        model: CourseContentModel
    ) {
        val id = documentSnapshot.id
        when (model.courseType) {
            "lecture" -> {
                val intent = Intent(this, LessonActivity::class.java)
                intent.putExtra(Constants.LESSON_TITLE, model.courseTitle)
                intent.putExtra(Constants.COURSE_CONTENT_TEXT, model.content)
                intent.putExtra(Constants.COURSE_CONTENT_PDF_URL, model.pdfUrl)
                intent.putExtra(Constants.COURSE_NAME, model.courseName)
                startActivity(intent)
            }
            "video" -> {
                val intent = Intent(this, VideoLessonActivity::class.java)
                intent.putExtra(Constants.LESSON_TITLE, model.courseTitle)
                intent.putExtra(Constants.COURSE_CONTENT_PDF_URL, model.pdfUrl)
                startActivity(intent)
            }
            "test" -> {
            }
        }
        Toast.makeText(this, "Position: $position ID: $id", Toast.LENGTH_SHORT).show()
    }

    private fun onClickListener(name: String?) {
        followButton.setOnClickListener {
            favoriteCourseState(name)
            checkState(name)
        }
        addFab.setOnClickListener {
            startActivity(Intent(this, CreateLessonActivity::class.java))
        }
    }

    private fun favoriteCourseState(name: String?) {
        if (!isFavorite!!) {
            fStore.collection("course").document(name!!)
                .update("isFavorite", true)
        } else {
            fStore.collection("course").document(name!!)
                .update("isFavorite", false)
        }
    }

    private fun changeButtonState(name: String?) {
        if (isFavorite!!) {
            followButton.text = "Unfollow"
        } else {
            followButton.text = "Follow"
        }
    }

    private fun checkState(name: String?) {
        fStore.collection("course").document(name!!)
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val documentSnapshot = it.result
                    if (documentSnapshot != null) {
                        isFavorite = documentSnapshot.getBoolean("isFavorite")
                        changeButtonState(name)
                    }
                } else {
                    Toast.makeText(this, "Failed to follow", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun setBackgroundImage(image: String?) {
        Glide.with(applicationContext)
            .load(image)
            .into(bgImageView)
    }

    private fun addButtonVisible(name: String?) {
        val fUser = fAuth.currentUser!!
        documentReference = fStore.collection("course").document(name!!)
        documentReference.get().addOnSuccessListener {
            if (it.getString("contentMaker") == fUser.uid) {
                addFab.visibility = View.VISIBLE
            }
        }
    }
}