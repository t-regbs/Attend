package com.example.attend.ui.student

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.attend.data.model.Course
import com.example.attend.databinding.FragmentStudentAddBinding
import com.example.attend.ui.student.CourseSelectAdapter.OnItemCheckedListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.concurrent.thread


class StudentAddFragment : Fragment() {

    private val studentViewModel by viewModel<StudentViewModel>()
    private lateinit var binding: FragmentStudentAddBinding
    private lateinit var courseSelectAdapter: CourseSelectAdapter

    private val currentSelectedCourses:MutableList<Course> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        courseSelectAdapter = CourseSelectAdapter(object : OnItemCheckedListener {
            override fun onItemCheck(course: Course?) {
                currentSelectedCourses.add(course!!)
                Timber.d("selected course: ${course.courseCode}")
            }

            override fun onItemUncheck(course: Course?) {
                currentSelectedCourses.remove(course)
                Timber.d("unselected course: ${course!!.courseCode}")
            }
        })
        binding = FragmentStudentAddBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = studentViewModel
            rvSelectCourses.adapter = courseSelectAdapter
        }
        return binding.root
    }

    private fun setupObserver() {
        studentViewModel.courses.observe(viewLifecycleOwner, Observer {
            courseSelectAdapter.submitList(it)
        })
        studentViewModel.crossRefs.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        studentViewModel.getCourses()
        setupObserver()
        binding.btnAdd.setOnClickListener {
            attemptSubmitForm()
        }
    }

    private fun attemptSubmitForm() {
        val firstName = binding.firstName.editText?.text.toString()
        val lastName = binding.lastName.editText?.text.toString()
        val contactNo = binding.contactNo.editText?.text.toString()
        val matricNo = binding.matricNo.editText?.text.toString()

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
                TextUtils.isEmpty(matricNo)) return
        studentViewModel.submitStudent(firstName, lastName, contactNo, matricNo, currentSelectedCourses)
    }

}