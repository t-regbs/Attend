package com.example.attend.ui.takeattendance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.attend.app.AuthenticationManager
import com.example.attend.databinding.LecStudentFragmentBinding
import com.example.attend.databinding.SelectAttendanceFragmentBinding
import com.example.attend.ui.lecstudents.LecStudentFragmentDirections
import com.example.attend.ui.lecstudents.LecStudentViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectAttendanceFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val attendanceViewModel by viewModel<LecStudentViewModel>()
    private lateinit var binding: SelectAttendanceFragmentBinding
    private lateinit var userId:String
    private lateinit var courseCode:String
    private val courseCodeList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SelectAttendanceFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = attendanceViewModel
        }
        binding.spinnerCourses.onItemSelectedListener = this
//        userId = authenticationManager.getUserId()
        userId = "1"

        return binding.root
    }

    private fun setupObserver() {
        attendanceViewModel.courses.observe(viewLifecycleOwner, Observer { courses ->
            courses.forEach { courseCodeList.add(it.courseCode) }
            createArrayAdapter()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attendanceViewModel.getCoursesFromId(userId.toInt())
        setupObserver()
        binding.btnGetStudent.setOnClickListener {
            findNavController().navigate(
                SelectAttendanceFragmentDirections
                .actionSelectAttendanceFragmentToTakeAttendanceFragment(courseCode)
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