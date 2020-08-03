package com.example.attend.ui.student

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attend.data.model.Course
import com.example.attend.data.model.StudentWithCourses
import com.example.attend.data.repository.AttendanceRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class StudentViewModel(private val repository: AttendanceRepository) : ViewModel() {

    val studentWithCourses = MutableLiveData<List<StudentWithCourses>>()
    val courses = MutableLiveData<List<Course>>()
    val crossRefs = MutableLiveData<Array<Long>>()

    fun getCourses() {
        viewModelScope.launch {
            val courseList = repository.getCourses()
            courses.postValue(courseList)
        }
    }

    fun getStudentWithCourses() {
        viewModelScope.launch {
            val list = repository.getStudentWithCourses()
            studentWithCourses.postValue(list)
        }
    }

    fun submitStudent(firstName: String, lastName: String, contactNo: String, matricNo: String, selectedCourses: MutableList<Course>) {
        viewModelScope.launch {
            val inserted = repository.addStudentWithSelectedCourses(firstName, lastName, contactNo, matricNo, selectedCourses)
            crossRefs.postValue(inserted)
        }
    }


}