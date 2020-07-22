package com.example.attend.ui.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.attend.databinding.FragmentStudentBinding

class StudentFragment : Fragment() {

    private lateinit var studentViewModel: StudentViewModel
    private lateinit var binding: FragmentStudentBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        studentViewModel =
                ViewModelProvider(this).get(StudentViewModel::class.java)
        binding = FragmentStudentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = studentViewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener {
            val action = StudentFragmentDirections.actionNavStudentToStudentAddFragment()
            findNavController().navigate(action)
        }
    }
}