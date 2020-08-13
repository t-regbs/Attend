package com.example.attend.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class StudentWithAttendances(
        @Embedded val student: Student,
        @Relation(parentColumn = "studentId", entityColumn = "studentId")
        val attendanceList: List<Attendance>
)