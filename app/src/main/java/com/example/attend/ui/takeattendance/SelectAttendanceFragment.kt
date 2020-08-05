package com.example.attend.ui.takeattendance

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.attend.R

class SelectAttendanceFragment : Fragment() {

    companion object {
        fun newInstance() = SelectAttendanceFragment()
    }

    private lateinit var viewModel: SelectAttendanceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.select_attendance_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SelectAttendanceViewModel::class.java)
        // TODO: Use the ViewModel
    }

}