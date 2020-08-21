package com.example.attend.ui.lecstudents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.attend.databinding.LecStudentListFragmentBinding
import com.example.attend.ui.student.StudentFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel

class LecStudentListFragment : Fragment() {

    private val listViewModel by viewModel<LecStudentViewModel>()
    private lateinit var binding: LecStudentListFragmentBinding
    private lateinit var courseCode: String
    private val studentListAdapter = StudentAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LecStudentListFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = listViewModel
            rvStudents.adapter = studentListAdapter
        }

        val args: LecStudentListFragmentArgs by navArgs()
        courseCode = args.courseCode

        return binding.root
    }

    private fun setupObserver() {
        listViewModel.courseAndStudents.observe(viewLifecycleOwner, { courseWithStudents ->
            val list = courseWithStudents.students
            studentListAdapter.submitList(list)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listViewModel.getCourseWithStudents(courseCode)
        setupObserver()
        binding.fab.setOnClickListener {
            findNavController().navigate(LecStudentListFragmentDirections.actionNavStudentsListToLecStudentAddFragment(courseCode))
        }
    }
}