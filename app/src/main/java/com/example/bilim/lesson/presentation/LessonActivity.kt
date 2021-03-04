package com.example.bilim.lesson.presentation

import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bilim.R
import com.example.bilim.common.Constants
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.listener.FileRequestListener
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import java.io.File

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
        val url = intent.getStringExtra(Constants.COURSE_CONTENT_PDF_URL)
        val myUri: Uri = Uri.parse(url)

        FileLoader.with(this)
            .load(url,false)
            .fromDirectory("PDFFile", FileLoader.DIR_INTERNAL)
            .asFile(object: FileRequestListener<File> {
                override fun onLoad(request: FileLoadRequest?, response: FileResponse<File>?) {
                    val pdfFile = response!!.body
                    contentPdf.fromFile(pdfFile)
                        .password(null)
                        .defaultPage(0)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .onDraw { canvas, pageWidth, pageHeight, displayedPage ->
                        }
                        .onDrawAll { canvas, pageWidth, pageHeight, displayedPage ->  }
                        .onPageChange { page, pageCount ->  }
                        .onPageError { page, t ->
                            Toast.makeText(this@LessonActivity, "Error", Toast.LENGTH_SHORT).show()
                        }
                }

                override fun onError(request: FileLoadRequest?, t: Throwable?) {
                    Toast.makeText(this@LessonActivity, "Internet not connection", Toast.LENGTH_SHORT).show()
                }

            })
    }
}