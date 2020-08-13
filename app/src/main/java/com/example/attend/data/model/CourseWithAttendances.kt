package com.example.attend.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class CourseWithAttendances(
    @Embedded val course: Course,
    @Relation(parentColumn = "courseId", entityColumn = "courseId")
    val attendanceList: List<Attendance>
)