package com.example.attend.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class LecturerWithCourses(
    @Embedded val lecturer: Lecturer,
    @Relation(parentColumn = "id", entityColumn = "lecturer")
    val courseList: List<Course>
)