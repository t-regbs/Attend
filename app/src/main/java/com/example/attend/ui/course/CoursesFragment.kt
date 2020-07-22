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

class CoursesFragment : Fragment() {

    private lateinit var coursesViewModel: CoursesViewModel
    private lateinit var binding: FragmentCoursesBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        coursesViewModel =
                ViewModelProvider(this).get(CoursesViewModel::class.java)
        binding = FragmentCoursesBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = coursesViewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener {
            val action = CoursesFragmentDirections.actionNavCourseToAddCourseFragment()
            findNavController().navigate(action)
        }
    }
}