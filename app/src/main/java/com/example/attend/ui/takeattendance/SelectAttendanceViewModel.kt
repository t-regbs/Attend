package com.example.attend.ui.takeattendance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attend.data.model.CourseWithStudents
import com.example.attend.data.model.Student
import com.example.attend.data.repository.AttendanceRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SelectAttendanceViewModel(private val repository: AttendanceRepository) : ViewModel() {

    val courseAndStudents = MutableLiveData<CourseWithStudents>()
    val inserted = MutableLiveData<Array<Long>>()

    fun getCourseWithStudents(courseCode: String) {
        viewModelScope.launch {
            val courseWithStudents = repository.getCourseWithStudentsFromCode(courseCode)
            courseAndStudents.postValue(courseWithStudents)
        }
    }

    fun takeAttendance(hmAttendance: HashMap<Student, String>, courseCode: String) {
        viewModelScope.launch {
            val courseId = getCourseIdFromCourseCode(courseCode)
            val insertedAttendance = repository.addNewAttendance(hmAttendance, courseId, courseCode)
            inserted.postValue(insertedAttendance)
        }

    }

    private suspend fun getCourseIdFromCourseCode(courseCode: String): Int {
        var id: Int? = null
        viewModelScope.async {
            id = repository.getCourseIdFromCourseCode(courseCode)
        }.await()
        return id!!
    }
}