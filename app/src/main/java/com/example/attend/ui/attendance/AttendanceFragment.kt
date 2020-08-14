package com.example.attend.ui.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.list.customListAdapter
import com.example.attend.R
import com.example.attend.data.model.Course
import com.example.attend.data.model.Student
import com.example.attend.databinding.FragmentAttendanceBinding
import com.example.attend.ui.student.CourseSelectAdapter
import com.example.attend.ui.student.CourseSelectAdapter.*
import com.example.attend.ui.attendance.StudentSelectAdapter.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AttendanceFragment : Fragment() {

    private val attendanceViewModel by viewModel<AttendanceViewModel>()
    private lateinit var binding: FragmentAttendanceBinding
    private lateinit var courseSelectAdapter: CourseSelectAdapter
    private lateinit var studentSelectAdapter: StudentSelectAdapter
    private lateinit var dialog: MaterialDialog

    private lateinit var myCalendar: Calendar
    private val courseSelected = ArrayList<Course>()
    private val studentSelected = ArrayList<Student>()
    private val studentList = ArrayList<Student>()
    private lateinit var courseList: List<Course>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        attendanceViewModel.getCoursesFromId(1)
        courseSelectAdapter = CourseSelectAdapter(object:
            CourseSelectAdapter.OnItemCheckedListener {
            override fun onItemCheck(course: Course?) {
                courseSelected.add(course!!)
            }

            override fun onItemUncheck(course: Course?) {
                courseSelected.remove(course)
            }

        })

        studentSelectAdapter = StudentSelectAdapter(object:
            StudentSelectAdapter.OnItemCheckedListener {
            override fun onItemCheck(student: Student?) {
                studentSelected.add(student!!)
            }

            override fun onItemUncheck(student: Student?) {
                studentSelected.remove(student)
            }

        })

        binding = FragmentAttendanceBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = attendanceViewModel
        }

        binding.startDate.editText?.setOnClickListener {
            MaterialDialog(requireContext()).show {
                datePicker { _, date ->
                    myCalendar = date
                    updateStartLabel()
                }
            }
        }

        binding.endDate.editText?.setOnClickListener {
            MaterialDialog(requireContext()).show { 
                datePicker { _, date ->
                    myCalendar = date
                    updateEndLabel()
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        attendanceViewModel.courses.observe(viewLifecycleOwner, Observer {
            courseList = it
            courseSelectAdapter.submitList(it)
        })

        attendanceViewModel.students.observe(viewLifecycleOwner, Observer { list ->
            for (item in list) {
                for (student in item.students) {
                    if (!studentList.contains(student)) studentList.add(student)
                }
            }
            studentSelectAdapter.submitList(studentList)
        })

        binding.btnCourseAttendance.setOnClickListener {
            showCoursesDialog()
        }
        binding.btnStudentAttendance.setOnClickListener {
            attendanceViewModel.getStudentsFromCourses(courseList)
            showStudentsDialog()
        }
    }

    private fun showCoursesDialog() {
        dialog = MaterialDialog(requireContext())
            .show {
                noAutoDismiss()
                customListAdapter(courseSelectAdapter)
                positiveButton(R.string.submit){
                    val startDate = binding.startDate.editText?.text
                    val endDate = binding.endDate.editText?.text
                    //Send selected courses to attendance report fragment for display
                    if (courseSelected.isNotEmpty() && startDate!!.isNotBlank() && endDate!!.isNotBlank()) {
                        findNavController().navigate(AttendanceFragmentDirections
                                .actionNavViewAttendanceToAttendanceReportFragment(
                                        courseSelected.toTypedArray(),
                                        startDate.toString(),
                                        endDate.toString(),
                                        null
                                ))
                        dismiss()
                    } else {
                        Toast.makeText(context, "Course not selected or date field empty", Toast.LENGTH_SHORT).show()
                    }
                }
            }

    }

    private fun showStudentsDialog() {
        dialog = MaterialDialog(requireContext())
            .show {
                noAutoDismiss()
                customListAdapter(studentSelectAdapter)
                positiveButton(R.string.submit){
                    val startDate = binding.startDate.editText?.text
                    val endDate = binding.endDate.editText?.text
                    //Send selected courses to attendance report fragment for display
                    if (studentSelected.isNotEmpty() && startDate!!.isNotBlank() && endDate!!.isNotBlank()) {
                        findNavController().navigate(AttendanceFragmentDirections
                            .actionNavViewAttendanceToAttendanceReportFragment2(
                                studentSelected.toTypedArray(),
                                startDate.toString(),
                                endDate.toString(),
                                null
                            ))
                        dismiss()
                    } else {
                        Toast.makeText(context, "Student not selected or date field empty", Toast.LENGTH_SHORT).show()
                    }
                }
            }

    }

    private fun updateStartLabel() {
        val myFormat = "MM/dd/yy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.startDate.editText?.setText(sdf.format(myCalendar.time))
    }
    private fun updateEndLabel() {
        val myFormat = "MM/dd/yy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.endDate.editText?.setText(sdf.format(myCalendar.time))
    }
}