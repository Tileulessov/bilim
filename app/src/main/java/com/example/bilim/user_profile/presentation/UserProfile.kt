package com.example.bilim.user_profile.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.bilim.createCourseDialogFragment.CreateCourseBottomSheetDialogFragment
import com.example.bilim.R
import com.example.bilim.common.Constants
import com.example.bilim.common.dataSourse.SharedPrefDataSource
import com.example.bilim.createContent.presentation.CreateContentActivity
import com.example.bilim.sign.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import org.koin.android.ext.android.inject

class UserProfile : AppCompatActivity() {

    private lateinit var logoutButton: Button
    private lateinit var userNameTextView: TextView
    private lateinit var userAgeTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var userImage: ImageView
    private lateinit var fStore: FirebaseFirestore
    private lateinit var fAuth: FirebaseAuth
    private lateinit var df: DocumentReference
    private lateinit var createCourseButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var toolbar: Toolbar
    private lateinit var fillCourseButton: Button
    private lateinit var collectionReference: CollectionReference
    private lateinit var linearLayout: LinearLayout
    private val userUidSharedPref: SharedPrefDataSource by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        initViews()

        logoutProfile()
        val fUser = fAuth.currentUser!!
        userDetails(fUser.uid)
        checkUserAccessLevel(fUser.uid)
        checkIsCourseCreate()
        onClickListener()
        onBack()
    }

    private fun initViews() {
        userImage = findViewById(R.id.activity_user_profile_user_image_img_view)
        logoutButton = findViewById(R.id.activity_user_profile_logout_button)
        userNameTextView = findViewById(R.id.activity_user_profile_name_text_view)
        userAgeTextView = findViewById(R.id.activity_user_profile_age_text_view)
        userEmailTextView = findViewById(R.id.activity_user_profile_email_text_view)
        createCourseButton = findViewById(R.id.activity_user_profile_create_button)
        progressBar = findViewById(R.id.activity_user_profile_progress_bar)
        toolbar = findViewById(R.id.activity_user_profile_toolbar)
        linearLayout = findViewById(R.id.activity_user_profile_create_linear)
        fillCourseButton = findViewById(R.id.activity_user_profile_fill_course_button)
        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
    }

    private fun logoutProfile() {
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    private fun userDetails(uid: String) {
        progressBar.isVisible = true
        df = fStore.collection("Users").document(uid)
        df.get().addOnSuccessListener {
            userNameTextView.text = it.getString("fullname")
            userAgeTextView.text = it.getString("age")
            userEmailTextView.text = it.getString("email")
        }
        progressBar.isVisible = false
    }

    private fun checkUserAccessLevel(uid: String) {
        df = fStore.collection("Users").document(uid)
        df.get().addOnSuccessListener {
            if (it.getString("isTeacher") != null) {
                linearLayout.visibility = View.VISIBLE
            }
        }
        saveUserUidSharedPref(uid)
    }

    private fun checkIsCourseCreate() {
        val fUser = fAuth.currentUser!!
        collectionReference = fStore.collection("course")
        val query: Query = collectionReference.whereEqualTo("contentMaker", fUser.uid)
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    fillCourseButton.isVisible = true
                    Log.d("TAG", document.id + " => " + document.data)
                }
            } else {
                Log.d("TAG", "Error getting documents: ", task.exception)
            }
        }
    }

    private fun onClickListener() {
        createCourseButton.setOnClickListener {
            showCreateCourseDialog()
        }
        fillCourseButton.setOnClickListener {
            showCreateContentActivity()
        }
    }

    private fun onBack() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun saveUserUidSharedPref(userUid: String) {
        userUidSharedPref.saveValue(Constants.CONTENT_MAKER, userUid)
    }

    private fun showCreateCourseDialog() {
        CreateCourseBottomSheetDialogFragment().show(
            supportFragmentManager,
            CreateCourseBottomSheetDialogFragment.CREATE_COURSE
        )
    }

    private fun showCreateContentActivity() {
        startActivity(Intent(this, CreateContentActivity::class.java))
    }
}