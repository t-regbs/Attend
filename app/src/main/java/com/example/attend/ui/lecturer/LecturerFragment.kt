package com.example.attend.ui.lecturer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.attend.databinding.FragmentLecturerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LecturerFragment : Fragment() {

    private val lecturerViewModel by viewModel<LecturerViewModel>()
    private lateinit var binding: FragmentLecturerBinding
    private val adapter = LecturerAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLecturerBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = lecturerViewModel
            rvLecturers.adapter = adapter
        }

        return binding.root
    }

    private fun setupObserver() {
        lecturerViewModel.lecturers.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lecturerViewModel.getLecturers()
        setupObserver()

        binding.fab.setOnClickListener {
            val action = LecturerFragmentDirections.actionNavLecturerToLecturerAddFragment()
            findNavController().navigate(action)
        }
    }
}