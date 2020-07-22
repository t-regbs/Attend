package com.example.attend.ui.lecturer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.attend.databinding.FragmentLecturerBinding

class LecturerFragment : Fragment() {

    private lateinit var lecturerViewModel: LecturerViewModel
    private lateinit var binding: FragmentLecturerBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        lecturerViewModel =
                ViewModelProvider(this).get(LecturerViewModel::class.java)
        binding = FragmentLecturerBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = lecturerViewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener {
            val action = LecturerFragmentDirections.actionNavLecturerToLecturerAddFragment()
            findNavController().navigate(action)
        }
    }
}