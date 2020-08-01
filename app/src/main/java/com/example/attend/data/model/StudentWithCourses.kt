package com.example.attend.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class StudentWithCourses(
        @Embedded val student: Student,
        @Relation(
                parentColumn = "studentId",
                entityColumn = "courseId",
                associateBy = Junction(StudentCourseCrossRef::class)
        )
        val courses: List<Course>
)