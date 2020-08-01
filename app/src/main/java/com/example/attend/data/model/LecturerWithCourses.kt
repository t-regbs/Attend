package com.example.attend.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class LecturerWithCourses(
    @Embedded val lecturer: Lecturer,
    @Relation(parentColumn = "lecturerId", entityColumn = "lecturer_taking_id")
    val courseList: List<Course>
)