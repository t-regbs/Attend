package com.example.attend.ui.takeattendance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.attend.data.model.Student
import com.example.attend.databinding.FragmentTakeAttendanceBinding
import com.example.attend.ui.takeattendance.TakeAttendanceAdapter.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class TakeAttendanceFragment : Fragment() {

    private val listViewModel by viewModel<SelectAttendanceViewModel>()
    private lateinit var binding: FragmentTakeAttendanceBinding
    private lateinit var courseCode: String
    private lateinit var studentListAdapter: TakeAttendanceAdapter

    private val hmAttendance = HashMap<Student, String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        studentListAdapter = TakeAttendanceAdapter(object: OnItemClickedListener {
            override fun onRbClicked(status: String, student: Student) {
                Timber.d("Student: ${student.firstName}, Status: $status")
                hmAttendance[student] = status
            }

        })
        binding = FragmentTakeAttendanceBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = listViewModel
            rvStudents.adapter = studentListAdapter
        }

        val args: TakeAttendanceFragmentArgs by navArgs()
        courseCode = args.courseCode

        return binding.root
    }

    private fun setupObserver() {
        listViewModel.courseAndStudents.observe(viewLifecycleOwner, Observer {courseWithStudents ->
            val list = courseWithStudents.students
            studentListAdapter.submitList(list)
        })

        listViewModel.inserted.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "Attendance Inserted", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listViewModel.getCourseWithStudents(courseCode)
        setupObserver()
        binding.btnSubmit.setOnClickListener { listViewModel.takeAttendance(hmAttendance, courseCode) }
    }
}