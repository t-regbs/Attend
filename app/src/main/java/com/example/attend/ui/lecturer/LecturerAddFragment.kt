package com.example.attend.ui.lecturer

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.attend.R
import com.example.attend.databinding.FragmentLecturerAddBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LecturerAddFragment : Fragment() {

    private val lecturerViewModel by viewModel<LecturerViewModel>()
    private lateinit var binding: FragmentLecturerAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentLecturerAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAdd.setOnClickListener {
            attemptSubmitForm()
        }
    }

    private fun attemptSubmitForm() {
        val firstName = binding.firstName.editText?.text.toString()
        val lastName = binding.lastName.editText?.text.toString()
        val contactNo = binding.contactNo.editText?.text.toString()
        val email = binding.email.editText?.text.toString()

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
            TextUtils.isEmpty(contactNo) || TextUtils.isEmpty(email)) return

        lecturerViewModel.submitLecturer(firstName, lastName, contactNo, email)
        findNavController().popBackStack()
    }
}