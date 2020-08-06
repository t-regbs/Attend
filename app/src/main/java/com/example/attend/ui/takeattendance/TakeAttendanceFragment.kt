package com.example.attend.ui.takeattendance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        studentListAdapter = TakeAttendanceAdapter(object: OnItemClickedListener {
            override fun onRbClicked(status: String, student: Student) {
                Timber.d("Student: ${student.firstName}, Status: $status")
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listViewModel.getCourseWithStudents(courseCode)
        setupObserver()
    }
}