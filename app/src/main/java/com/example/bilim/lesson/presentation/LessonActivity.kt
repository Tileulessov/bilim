package com.example.bilim.lesson.presentation

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.bilim.R
import com.example.bilim.common.Constants
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URLEncoder

class LessonActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var contentPdf: WebView
    private val mDataBase: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var collectionReference: CollectionReference
    private lateinit var progressBar: ProgressBar

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)
        initViews()
    }

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViews() {
        titleTextView = findViewById(R.id.activity_lesson_title_text_view)
        contentPdf = findViewById(R.id.activity_lesson_webview)
        contentPdf.settings.javaScriptEnabled = true
        contentPdf.settings.builtInZoomControls = true
        progressBar = findViewById(R.id.activity_lesson_progress_bar)
        getLessonContent()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getLessonContent() {
        val title = intent.getStringExtra(Constants.LESSON_TITLE)
        val courseName = intent.getStringExtra(Constants.COURSE_NAME)
        titleTextView.text = title
        val pdfUrl = intent.getStringExtra(Constants.COURSE_CONTENT_PDF_URL)
        getPdfFromFirebase(pdfUrl, courseName)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPdfFromFirebase(pdfUrl: String?, courseName: String?) {
        collectionReference = mDataBase.collection("course")
                .document(courseName!!)
                .collection(courseName)
        loadProgressBar()
        contentPdf.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                loadProgressBar()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                goneProgressBar()
            }
        }

        var url = ""

        try {
            url = URLEncoder.encode(pdfUrl)
        } catch (ex: Exception) {
            Toast.makeText(this, getString(R.string.error_pdf_url), Toast.LENGTH_LONG).show()
        }

        contentPdf.loadUrl(Constants.GOOGLE_PDF_READ + url)
    }

    private fun loadProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun goneProgressBar() {
        progressBar.visibility = View.GONE
    }
}