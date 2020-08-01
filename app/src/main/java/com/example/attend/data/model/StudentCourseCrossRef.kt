package com.example.attend.data.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["studentId", "courseId"])
data class StudentCourseCrossRef(
    val studentId: Int,
    val courseId: Int
)