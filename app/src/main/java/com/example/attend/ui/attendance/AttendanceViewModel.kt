package com.example.attend.ui.attendance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attend.data.model.Course
import com.example.attend.data.model.CourseWithStudents
import com.example.attend.data.model.Student
import com.example.attend.data.repository.AttendanceRepository
import kotlinx.coroutines.launch

class AttendanceViewModel(private val repository: AttendanceRepository) : ViewModel() {

    val courses = MutableLiveData<List<Course>>()
    val students = MutableLiveData<List<CourseWithStudents>>()

    fun getCoursesFromId(lecturerId: Int) {
        viewModelScope.launch {
            val courseList = repository.getCoursesWithId(lecturerId)
            courses.postValue(courseList)
        }
    }

    fun getAllCourses() {
        viewModelScope.launch {
            val courseList = repository.getCourses()
            courses.postValue(courseList)
        }
    }

    fun getAllCourseWithStudents() {
        viewModelScope.launch {
            val coursesWithStudents = repository.getCourseWithStudents()
            students.postValue(coursesWithStudents)
        }
    }

    fun getStudentsFromCourses(courseList: List<Course>) {
        viewModelScope.launch {
            val coursesWithStudents = repository.getCourseWithStudentsFromCourseList(courseList)
            students.postValue(coursesWithStudents)
        }
    }

}