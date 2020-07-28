package com.example.attend.ui.attendance

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.attend.databinding.FragmentAttendanceBinding
import java.text.SimpleDateFormat
import java.util.*


class AttendanceFragment : Fragment() {

    private lateinit var attendanceViewModel: AttendanceViewModel
    private lateinit var binding: FragmentAttendanceBinding

    private val myCalendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        attendanceViewModel = ViewModelProvider(this).get(AttendanceViewModel::class.java)

        binding = FragmentAttendanceBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = attendanceViewModel
        }

        val date = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateStartLabel()
        }

        val dateEnd = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateEndLabel()
        }

        binding.startDate.editText?.setOnClickListener {
            DatePickerDialog(
                requireContext(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.endDate.editText?.setOnClickListener {
            DatePickerDialog(
                requireContext(), dateEnd, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        return binding.root
    }

    private fun updateStartLabel() {
        val myFormat = "MM/dd/yy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.startDate.editText?.setText(sdf.format(myCalendar.time))
    }
    private fun updateEndLabel() {
        val myFormat = "MM/dd/yy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.endDate.editText?.setText(sdf.format(myCalendar.time))
    }
}