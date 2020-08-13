package com.example.attend.ui.attendancereport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.attend.data.model.Course
import com.example.attend.databinding.AttendanceReportFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AttendanceReportFragment : Fragment() {

    private val reportViewModel by viewModel<AttendanceReportViewModel>()
    private lateinit var binding: AttendanceReportFragmentBinding
    private val adapter = ReportCourseAdapter()
    private lateinit var courseList: Array<Course>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val safeArgs: AttendanceReportFragmentArgs by navArgs()
        courseList = safeArgs.courseList
        reportViewModel.getStudents(courseList)

        binding = AttendanceReportFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = reportViewModel
            rvCourses.adapter = adapter
        }

        return binding.root
    }

    private fun setupObserver() {
        reportViewModel.studentWithAttendanceList.observe(viewLifecycleOwner, Observer { list ->
            adapter.studentWithAttendanceList = list
            reportViewModel.courseWithAttendanceList.observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
            })
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reportViewModel.getCourseWithAttendances(courseList)
        setupObserver()

    }
}