package com.example.attend.ui.course

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attend.data.model.Course
import com.example.attend.data.repository.AttendanceRepository
import kotlinx.coroutines.launch

class CoursesViewModel(private val repository: AttendanceRepository) : ViewModel() {

    val courses = MutableLiveData<List<Course>>()

    fun getCourses() {
        viewModelScope.launch {
            val courseList = repository.getCourses()
            courses.postValue(courseList)
        }
    }

    fun submitCourse(courseCode: String, courseName: String, lecturerId: Int) {
        viewModelScope.launch {
            repository.addNewCourse(courseCode, courseName, lecturerId)
        }
    }
}