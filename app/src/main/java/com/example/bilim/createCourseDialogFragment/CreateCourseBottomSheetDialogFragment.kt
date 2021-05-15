package com.example.bilim.createCourseDialogFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.bilim.R
import com.example.bilim.common.Constants
import com.example.bilim.common.dataSourse.SharedPrefDataSource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_common_bottom_sheet_dialog.*
import org.koin.android.ext.android.inject

class CreateCourseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var fStore: FirebaseFirestore
    private lateinit var textWatcher: TextWatcher
    private val userUidSharedPref: SharedPrefDataSource by inject()

    override fun getTheme() = R.style.CommonBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_common_bottom_sheet_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fStore = FirebaseFirestore.getInstance()
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                course_icon_edit_text.error = null
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (checkFields()) {
                    create_button.isEnabled = true
                }
            }
        }
        course_name_edit_text.addTextChangedListener(textWatcher)
        course_icon_edit_text.addTextChangedListener(textWatcher)

        create_button.setOnClickListener {
            createCourse()
            dismiss()
        }
    }

    private fun createCourse() {
        val contentMaker = userUidSharedPref.getValue(Constants.CONTENT_MAKER)
        val courseName = course_name_edit_text.text.toString()
        val courseIcon = course_icon_edit_text.text.toString()
        val df = fStore.collection("course").document(courseName)
        try {
            val courseInfo = HashMap<String, Any>()
            courseInfo.put("contentMaker", contentMaker.toString())
            courseInfo.put("courseName", courseName)
            courseInfo.put("iconUrl", courseIcon)
            courseInfo.put("isChecked", false)
            courseInfo.put("isFavorite", false)
            df.set(courseInfo)
            Toast.makeText(requireActivity(), "Course create success", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    private fun checkFields(): Boolean {
        val courseIcon = course_icon_edit_text.text.toString()
        val illegalCharacters = setOf("https://", "http://", '/', "png")
        var isValid: Boolean = false

        return if (validFields(course_name_edit_text) && validFields(course_icon_edit_text)) {
            true
        } else if (!courseIcon.any(illegalCharacters::contains)
        ) {
            course_icon_edit_text.error =
                "Адресс ссылки должен содержать : (https://), (http://), '/', (png)"
            false
        } else {
            false
        }
    }

    private fun validFields(textField: EditText): Boolean {
        return if (textField.text.toString().isBlank()) {
            textField.error = "This is a required field"
            false
        } else true
    }

    companion object {

        const val CREATE_COURSE = "create_course"
    }
}