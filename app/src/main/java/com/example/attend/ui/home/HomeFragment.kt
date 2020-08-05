package com.example.attend.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.attend.app.AuthenticationManager
import com.example.attend.databinding.HomeFragmentBinding
import com.example.attend.ui.course.CourseAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val homeViewModel by viewModel<HomeViewModel>()
    private lateinit var binding: HomeFragmentBinding
    private lateinit var userId:String
    private val authenticationManager by inject<AuthenticationManager>()
    private val adapter = CourseAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = homeViewModel
            rvCourses.adapter = adapter
        }
//        userId = authenticationManager.getUserId()
        userId = "1"

        return binding.root
    }

    private fun setupObserver() {
        homeViewModel.courses.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getCoursesFromId(userId.toInt())
        setupObserver()

    }
}