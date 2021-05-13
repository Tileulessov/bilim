package com.example.bilim.common.dialogFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.bilim.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.common.base.CharMatcher.any
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_common_bottom_sheet_dialog.*

class CommonBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var fStore: FirebaseFirestore
    private lateinit var textWatcher: TextWatcher
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
        if (checkFields()) {
            create_button.isEnabled = true
        }
        create_button.setOnClickListener {
            createCourse()
            dismiss()
        }
    }

    private fun createCourse() {
        val courseName = course_name_edit_text.text.toString()
        val courseIcon = course_icon_edit_text.text.toString()
        val df = fStore.collection("course").document(courseName)
        val courseInfo = HashMap<String, Any>()
        courseInfo.put("courseName", courseName)
        courseInfo.put("courseIcon", courseIcon)
        courseInfo.put("isChecked", false)
        courseInfo.put("isFavorite", false)
        df.set(courseInfo)
    }

    private fun checkFields(): Boolean {
        val courseIcon = course_icon_edit_text.text.toString()
        val illegalCharacters = setOf("https://", "http://", '/', "png")
        var isValid: Boolean = false

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                isValid = (validFields(course_name_edit_text) || validFields(course_icon_edit_text))
                isValid = if (courseIcon.any(illegalCharacters::contains)
                ) {
                    course_icon_edit_text.error = null
                    true
                } else {
                    course_icon_edit_text.error =
                        "Адресс ссылки должен содержать : (https://), (http://), '/', (png)"
                    false
                }
            }
        }
        course_name_edit_text.addTextChangedListener(textWatcher)
        course_icon_edit_text.addTextChangedListener(textWatcher)
        return isValid
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