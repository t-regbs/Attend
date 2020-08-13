package com.example.attend.ui.attendancereport

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attend.data.model.*
import com.example.attend.data.repository.AttendanceRepository
import kotlinx.coroutines.launch

class AttendanceReportViewModel(private val repository: AttendanceRepository) : ViewModel() {

    val courseWithAttendanceList = MutableLiveData<List<CourseWithAttendances>>()
    val studentWithAttendanceList = MutableLiveData<List<StudentWithAttendances>>()
    private lateinit var courseAndStudents: List<CourseWithStudents>


    fun getCourseWithAttendances(courseList: Array<Course>) {
        viewModelScope.launch {
            val list = repository.getCourseWithAttendancesListFromId(courseList)
            courseWithAttendanceList.postValue(list)
        }
    }


    fun getStudents(courseList: Array<Course>) {
        viewModelScope.launch {
            val list = repository.getCourseWithAttendancesListFromId(courseList)
            getCourseWithStudentsList(list)
        }
    }


    private fun getStudentsWithAttendances(studentList: List<CourseWithStudents>) {
        viewModelScope.launch {
            val list = repository.getStudentsWithAttendanceFromStudent(studentList)
            studentWithAttendanceList.postValue(list)
        }
    }


    private fun getCourseWithStudentsList(list: List<CourseWithAttendances>) {
        viewModelScope.launch {
            val courseWithStudents = repository.getCourseWithStudentsFromCodeList(list)
            courseAndStudents = courseWithStudents
            getStudentsWithAttendances(courseAndStudents)
        }
    }

}