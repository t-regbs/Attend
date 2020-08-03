package com.example.attend.ui.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.attend.databinding.FragmentStudentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class StudentFragment : Fragment() {

    private val studentViewModel by viewModel<StudentViewModel>()
    private lateinit var binding: FragmentStudentBinding
    private val studentListAdapter = StudentListAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = studentViewModel
            rvStudents.adapter = studentListAdapter
        }

        return binding.root
    }

    private fun setupObserver() {
        studentViewModel.studentWithCourses.observe(viewLifecycleOwner, Observer {
            studentListAdapter.submitList(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        studentViewModel.getStudentWithCourses()
        setupObserver()
        binding.fab.setOnClickListener {
            val action = StudentFragmentDirections.actionNavStudentToStudentAddFragment()
            findNavController().navigate(action)
        }
    }
}