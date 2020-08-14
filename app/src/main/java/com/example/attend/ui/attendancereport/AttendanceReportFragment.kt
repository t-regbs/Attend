package com.example.attend.ui.attendancereport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.attend.data.model.Course
import com.example.attend.data.model.Student
import com.example.attend.databinding.AttendanceReportFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AttendanceReportFragment : Fragment() {

    private val reportViewModel by viewModel<AttendanceReportViewModel>()
    private lateinit var binding: AttendanceReportFragmentBinding
    private val reportCourseAdapter = ReportCourseAdapter()
    private val reportStudentAdapter = ReportStudentAdapter()
    private lateinit var courseList: Array<Course>
    private lateinit var studentList: Array<Student>
    private lateinit var startDate: String
    private lateinit var endDate: String
    private var isByCourse: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val safeArgs: AttendanceReportFragmentArgs by navArgs()
        courseList = safeArgs.courseList ?: emptyArray()
        studentList = safeArgs.studentList ?: emptyArray()
        isByCourse = safeArgs.studentList == null
        formatAndAssignDate(safeArgs)

        if (isByCourse) {
            reportViewModel.getStudents(courseList)
        } else {
            reportViewModel.getCourses(studentList)
        }


        binding = AttendanceReportFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = reportViewModel
            if (isByCourse){
                rvCourses.adapter = reportCourseAdapter
            } else {
                rvCourses.adapter = reportStudentAdapter
            }
        }

        return binding.root
    }

    private fun formatAndAssignDate(safeArgs: AttendanceReportFragmentArgs) {
        val format = "T00:00:00.000+01:00"
        val startList = safeArgs.startDate.split("/")
        val formattedStart = "20${startList[2]}-${startList[0]}-${startList[1]}"
        val endList = safeArgs.endDate.split("/")
        val formattedEnd = "20${endList[2]}-${endList[0]}-${endList[1]}"
        startDate = formattedStart + format
        endDate = formattedEnd + format
    }

    private fun setupCourseObserver() {
        reportViewModel.studentWithAttendanceList.observe(viewLifecycleOwner, Observer { list ->
            reportCourseAdapter.studentWithAttendanceList = list
            reportCourseAdapter.endDate = endDate
            reportCourseAdapter.startDate = startDate
            reportViewModel.courseWithAttendanceList.observe(viewLifecycleOwner, Observer {
                reportCourseAdapter.addHeaderAndSubmitList(it)
            })
        })

    }

    private fun setupStudentObserver() {
        reportViewModel.courseWithAttendanceList.observe(viewLifecycleOwner, Observer { list ->
            reportStudentAdapter.courseWithAttendanceList = list
            reportStudentAdapter.endDate = endDate
            reportStudentAdapter.startDate = startDate
            reportViewModel.studentWithAttendanceList.observe(viewLifecycleOwner, Observer {
                reportStudentAdapter.addHeaderAndSubmitList(it)
            })
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isByCourse) {
            reportViewModel.getCourseWithAttendances(courseList)
            setupCourseObserver()
        } else {
            reportViewModel.getStudentWithAttendances(studentList)
            setupStudentObserver()
        }

    }
}