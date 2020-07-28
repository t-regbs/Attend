package com.example.attend.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "course_code")
    val courseCode: String,
    @ColumnInfo(name = "course_name")
    val courseName: String,
    @ColumnInfo(name = "lecturer")
    val lecturer: String
)