package com.example.attend.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attend.data.model.Course
import com.example.attend.data.repository.AttendanceRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AttendanceRepository) : ViewModel() {

    val courses = MutableLiveData<List<Course>>()

    fun getCoursesFromId(lecturerId: Int) {
        viewModelScope.launch {
            val courseList = repository.getCoursesWithId(lecturerId)
            courses.postValue(courseList)
        }
    }

}