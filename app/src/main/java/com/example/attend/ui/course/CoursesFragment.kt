package com.example.attend.ui.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.attend.R
import com.example.attend.databinding.FragmentCoursesBinding
import com.example.attend.databinding.FragmentLecturerBinding
import com.example.attend.ui.lecturer.LecturerFragmentDirections
import com.example.attend.ui.lecturer.LecturerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CoursesFragment : Fragment() {

    private val coursesViewModel by viewModel<CoursesViewModel>()
    private lateinit var binding: FragmentCoursesBinding
    private val adapter = CourseAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCoursesBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = coursesViewModel
            rvCourses.adapter = adapter
        }

        return binding.root
    }

    private fun setupObserver() {
        coursesViewModel.courses.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coursesViewModel.getCourses()
        setupObserver()

        binding.fab.setOnClickListener {
            val action = CoursesFragmentDirections.actionNavCourseToAddCourseFragment()
            findNavController().navigate(action)
        }
    }
}