package com.example.attend.ui.attendance

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.list.customListAdapter
import com.example.attend.R
import com.example.attend.data.model.Course
import com.example.attend.databinding.FragmentAttendanceBinding
import com.example.attend.ui.student.CourseSelectAdapter
import com.example.attend.ui.student.CourseSelectAdapter.*
import com.example.attend.ui.takeattendance.TakeAttendanceAdapter
import com.example.attend.ui.takeattendance.TakeAttendanceAdapter.*
import kotlinx.android.synthetic.main.fragment_course_select_attendance.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class AttendanceFragment : Fragment() {

    private val attendanceViewModel by viewModel<AttendanceViewModel>()
    private lateinit var binding: FragmentAttendanceBinding
    private lateinit var adapter: CourseSelectAdapter
    private lateinit var dialog: MaterialDialog

    private lateinit var myCalendar: Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = CourseSelectAdapter(object: OnItemCheckedListener {
            override fun onItemCheck(course: Course?) {
                TODO("Not yet implemented")
            }

            override fun onItemUncheck(course: Course?) {
                TODO("Not yet implemented")
            }

        })

        binding = FragmentAttendanceBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = attendanceViewModel
        }

        binding.startDate.editText?.setOnClickListener {
            MaterialDialog(requireContext()).show {
                datePicker { dialog, date ->
                    myCalendar = date
                    updateStartLabel()
                }
            }
        }

        binding.endDate.editText?.setOnClickListener {
            MaterialDialog(requireContext()).show { 
                datePicker { dialog, date ->
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
            .noAutoDismiss()
//            .customView(R.layout.fragment_course_select_attendance)
            .show {
                customListAdapter(adapter)
                positiveButton(R.string.submit){

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