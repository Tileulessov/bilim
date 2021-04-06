package com.example.bilim.course_content.presentation

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bilim.R
import com.example.bilim.common.Constants
import com.example.bilim.common.listeners.ContentClickListener
import com.example.bilim.course_content.data.model.CourseContentModel
import com.example.bilim.course_content.presentation.view.ContentAdapter
import com.example.bilim.lesson.presentation.LessonActivity
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener

class CourseContentActivity : AppCompatActivity(), ContentClickListener {

    private lateinit var courseIcon: ImageView
    private lateinit var courseName: TextView
    private lateinit var courseTitleRecycler: RecyclerView
    private val mDataBase: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var collectionReference: CollectionReference
    private lateinit var contentAdapter: ContentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_content)

        initViews()
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
        getCourseDetails()
    }

    private fun getCourseDetails() {
        val name: String? = intent.getStringExtra(Constants.COURSE_NAME)
        courseName.text = name
        val img = intent.extras?.getString(Constants.COURSE_ICON)
        Glide.with(applicationContext)
                .load(img)
                .into(courseIcon)
        getContent(name)
    }

    private fun getContent(name: String?) {
        collectionReference = mDataBase.collection("course")
                .document(name!!)
                .collection(name)
        val query: Query = collectionReference.orderBy("courseId", Query.Direction.ASCENDING)
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<CourseContentModel> = FirestoreRecyclerOptions.Builder<CourseContentModel>()
                .setQuery(query, CourseContentModel::class.java)
                .build()
        contentAdapter = ContentAdapter(firestoreRecyclerOptions, this)
        courseTitleRecycler.layoutManager = LinearLayoutManager(this)
        courseTitleRecycler.adapter = contentAdapter
        Dexter.withContext(this)
            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : BaseMultiplePermissionsListener(){})
    }

    override fun onContentClick(
            documentSnapshot: DocumentSnapshot,
            position: Int,
            model: CourseContentModel
    ) {
        val id = documentSnapshot.id
        Toast.makeText(this, "Position: $position ID: $id", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LessonActivity::class.java)
        intent.putExtra(Constants.LESSON_TITLE, model.courseTitle)
        intent.putExtra(Constants.COURSE_CONTENT_TEXT, model.content)
        intent.putExtra(Constants.COURSE_CONTENT_PDF_URL, model.pdfUrl)
        intent.putExtra(Constants.COURSE_NAME, model.courseName)
        startActivity(intent)
    }
}