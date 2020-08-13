package com.example.attend.ui.attendancereport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.attend.data.db.Converters
import com.example.attend.data.model.Course
import com.example.attend.databinding.AttendanceReportFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

class AttendanceReportFragment : Fragment() {

    private val reportViewModel by viewModel<AttendanceReportViewModel>()
    private lateinit var binding: AttendanceReportFragmentBinding
    private val adapter = ReportCourseAdapter()
    private lateinit var courseList: Array<Course>
    private lateinit var startDate: String
    private lateinit var endDate: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val safeArgs: AttendanceReportFragmentArgs by navArgs()
        courseList = safeArgs.courseList
        formatAndAssignDate(safeArgs)
        reportViewModel.getStudents(courseList)

        binding = AttendanceReportFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = reportViewModel
            rvCourses.adapter = adapter
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

    private fun setupObserver() {
        reportViewModel.studentWithAttendanceList.observe(viewLifecycleOwner, Observer { list ->
            adapter.studentWithAttendanceList = list
            adapter.endDate = endDate
            adapter.startDate = startDate
            reportViewModel.courseWithAttendanceList.observe(viewLifecycleOwner, Observer {
                adapter.addHeaderAndSubmitList(it)
            })
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reportViewModel.getCourseWithAttendances(courseList)
        setupObserver()

    }
}