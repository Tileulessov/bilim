package com.example.bilim.lesson.presentation

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.bilim.R
import com.example.bilim.common.Constants
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class LessonActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var contentPdf: PDFView
    private lateinit var contentTextView:TextView
    private val mDataBase: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var collectionReference: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        initViews()


    }

    private fun initViews() {
        titleTextView = findViewById(R.id.activity_lesson_title_text_view)
        contentPdf = findViewById(R.id.activity_lesson_pdf)
        contentTextView = findViewById(R.id.activity_lesson_content_text_view)
        getLessonContent()
    }

    private fun getLessonContent(){
        val title = intent.getStringExtra(Constants.LESSON_TITLE)
        titleTextView.text = title
        val content = intent.getStringExtra(Constants.COURSE_CONTENT_TEXT)
        contentTextView.text = content
    }

    private fun getPdfFromFirebase() {
        collectionReference = mDataBase.collection("course")
            .document("Kotlin")
            .collection("Kotlin")

    }
}