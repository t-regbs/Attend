package com.example.attend.ui.takeattendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.attend.R
import com.example.attend.data.model.Student
import com.example.attend.databinding.FragmentTakeAttendanceBinding
import com.example.attend.ui.takeattendance.TakeAttendanceAdapter.OnItemClickedListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class TakeAttendanceFragment : Fragment() {

    private val listViewModel by viewModel<SelectAttendanceViewModel>()
    private lateinit var binding: FragmentTakeAttendanceBinding
    private lateinit var courseCode: String
    private lateinit var studentListAdapter: TakeAttendanceAdapter
    private lateinit var biometricPrompt: BiometricPrompt

    private val hmAttendance = HashMap<Student, String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        studentListAdapter = TakeAttendanceAdapter(object : OnItemClickedListener {
            override fun onRbClicked(status: String, student: Student) {
                Timber.d("Student: ${student.firstName}, Status: $status")
                hmAttendance[student] = status
            }

            override fun onButtonClicked(student: Student) {
                val promptInfo = createPromptInfo()
                biometricPrompt.authenticate(promptInfo)
            }

        })
        binding = FragmentTakeAttendanceBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = listViewModel
            rvStudents.adapter = studentListAdapter
        }

        biometricPrompt = createBiometricPrompt()
        val args: TakeAttendanceFragmentArgs by navArgs()
        courseCode = args.courseCode

        return binding.root
    }

    private fun createBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(requireContext())

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Timber.d("$errorCode :: $errString")
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    Toast.makeText(context, "Dismissed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Timber.d("Authentication failed for an unknown reason")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Timber.d("Authentication was successful")
//                onPurchased(true, result.cryptoObject)
                showConfirmation()
            }
        }

        val biometricPrompt = BiometricPrompt(this, executor, callback)
        return biometricPrompt
    }

    private fun showConfirmation() {
        Toast.makeText(context, "Print Authentication successful", Toast.LENGTH_SHORT).show()
    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.prompt_info_title))
            .setDescription(getString(R.string.prompt_info_description))
            .setConfirmationRequired(false)
            .setNegativeButtonText(getString(R.string.dismiss))
            // .setDeviceCredentialAllowed(true) // Allow PIN/pattern/password authentication.
            // Also note that setDeviceCredentialAllowed and setNegativeButtonText are
            // incompatible so that if you uncomment one you must comment out the other
            .build()
    }

    private fun setupObserver() {
        listViewModel.courseAndStudents.observe(viewLifecycleOwner, Observer { courseWithStudents ->
            val list = courseWithStudents.students
            studentListAdapter.submitList(list)
        })

        listViewModel.inserted.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "Attendance Inserted", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listViewModel.getCourseWithStudents(courseCode)
        setupObserver()
        binding.btnSubmit.setOnClickListener { listViewModel.takeAttendance(hmAttendance, courseCode) }
    }
}