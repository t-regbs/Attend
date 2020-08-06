package com.example.attend.ui.takeattendance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attend.data.model.Course
import com.example.attend.data.model.CourseWithStudents
import com.example.attend.data.repository.AttendanceRepository
import kotlinx.coroutines.launch

class SelectAttendanceViewModel(private val repository: AttendanceRepository) : ViewModel() {

    val courseAndStudents = MutableLiveData<CourseWithStudents>()

    fun getCourseWithStudents(courseCode: String) {
        viewModelScope.launch {
            val courseWithStudents = repository.getCourseWithStudentsFromCode(courseCode)
            courseAndStudents.postValue(courseWithStudents)
        }
    }

}