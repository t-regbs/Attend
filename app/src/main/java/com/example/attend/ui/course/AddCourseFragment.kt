package com.example.attend.ui.course

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.attend.R
import com.example.attend.databinding.FragmentAddCourseBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddCourseFragment : Fragment() {

    private val coursesViewModel by viewModel<CoursesViewModel>()
    private lateinit var binding: FragmentAddCourseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSubmit.setOnClickListener {
            attemptSubmitForm()
        }
    }

    private fun attemptSubmitForm() {
        val courseCode = binding.courseCode.editText?.text.toString()
        val courseName = binding.courseName.editText?.text.toString()
        val lecturerId = binding.lecturer.editText?.text.toString()

        if (TextUtils.isEmpty(courseCode) || TextUtils.isEmpty(courseName) ||
            TextUtils.isEmpty(lecturerId)) return

        val idToInt = Integer.parseInt(lecturerId)
        coursesViewModel.submitCourse(courseCode, courseName, idToInt)
        findNavController().popBackStack()
    }

}