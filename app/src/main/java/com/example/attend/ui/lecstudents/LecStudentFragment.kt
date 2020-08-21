package com.example.attend.ui.lecstudents

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.attend.R
import com.example.attend.app.AuthenticationManager
import com.example.attend.data.model.Course
import com.example.attend.databinding.HomeFragmentBinding
import com.example.attend.databinding.LecStudentFragmentBinding
import com.example.attend.ui.course.CourseAdapter
import com.example.attend.ui.home.HomeViewModel
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LecStudentFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val lecStudentViewModel by viewModel<LecStudentViewModel>()
    private lateinit var binding: LecStudentFragmentBinding
    private lateinit var userId:String
    private lateinit var courseCode:String
    private val courseCodeList = ArrayList<String>()
    private val authenticationManager by inject<AuthenticationManager>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LecStudentFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = lecStudentViewModel
        }
        binding.spinnerCourses.onItemSelectedListener = this
        userId = authenticationManager.getUserId()
//        userId = "1"

        return binding.root
    }

    private fun setupObserver() {
        lecStudentViewModel.courses.observe(viewLifecycleOwner, { courses ->
            courses.forEach {
                if (!courseCodeList.contains(it.courseCode))
                    courseCodeList.add(it.courseCode)
            }
            createArrayAdapter()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lecStudentViewModel.getCoursesFromId(userId.toInt())
        setupObserver()
        binding.btnGetStudent.setOnClickListener {
            findNavController().navigate(LecStudentFragmentDirections
                .actionNavLecturerStudentsToNavStudentsList(courseCode)
            )
        }

    }

    private fun createArrayAdapter() {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            courseCodeList
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCourses.adapter = adapter
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //Nothing to do
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        courseCode = parent?.getItemAtPosition(position).toString()
    }

}