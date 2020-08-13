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
import com.example.attend.databinding.FragmentAttendanceBinding
import com.example.attend.ui.student.CourseSelectAdapter
import com.example.attend.ui.student.CourseSelectAdapter.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AttendanceFragment : Fragment() {

    private val attendanceViewModel by viewModel<AttendanceViewModel>()
    private lateinit var binding: FragmentAttendanceBinding
    private lateinit var adapter: CourseSelectAdapter
    private lateinit var dialog: MaterialDialog

    private lateinit var myCalendar: Calendar
    private val courseSelected = ArrayList<Course>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = CourseSelectAdapter(object: OnItemCheckedListener {
            override fun onItemCheck(course: Course?) {
                courseSelected.add(course!!)
            }

            override fun onItemUncheck(course: Course?) {
                courseSelected.remove(course)
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
            adapter.submitList(it)
        })

        binding.btnCourseAttendance.setOnClickListener {
            attendanceViewModel.getCoursesFromId(1)
            showDialog()
        }
    }

    private fun showDialog() {
        dialog = MaterialDialog(requireContext())
            .show {
                noAutoDismiss()
                customListAdapter(adapter)
                positiveButton(R.string.submit){
                    //Send selected courses to attendance report fragment for display
                    if (courseSelected.isNotEmpty()) {
                        findNavController().navigate(AttendanceFragmentDirections
                                .actionNavViewAttendanceToAttendanceReportFragment(courseSelected.toTypedArray()))
                    } else {
                        Toast.makeText(context, "Nothing is selected", Toast.LENGTH_SHORT).show()
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