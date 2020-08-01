package com.example.attend.data.repository

import com.example.attend.data.db.LecturerDao
import com.example.attend.data.model.Lecturer

class AttendanceRepository(private val lecturerDao: LecturerDao) {

    suspend fun getLecturers(): List<Lecturer> {
        return lecturerDao.getLecturers()
    }

    suspend fun addNewLecturer(firstName: String, lastName: String, contactNo: String, email: String) {
        val newLecturer = Lecturer(firstName = firstName,
            lastName = lastName,
            email = email,
            contactNum = contactNo)
        lecturerDao.insertLecturer(newLecturer)
    }
}