package com.example.attend.ui.lecturer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attend.data.model.Lecturer
import com.example.attend.data.repository.AttendanceRepository
import kotlinx.coroutines.launch

class LecturerViewModel(private val repository: AttendanceRepository) : ViewModel() {

    val lecturers = MutableLiveData<List<Lecturer>>()

    fun getLecturers() {
        viewModelScope.launch {
            val lecturerList = repository.getLecturers()
            lecturers.postValue(lecturerList)
        }
    }

    fun submitLecturer(firstName: String, lastName: String, contactNo: String, email: String) {
        viewModelScope.launch {
            repository.addNewLecturer(firstName, lastName, contactNo, email)
        }
    }
}