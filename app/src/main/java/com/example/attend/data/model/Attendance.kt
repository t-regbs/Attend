package com.example.attend.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "attendance")
data class Attendance(
        @PrimaryKey(autoGenerate = true)
        val attendanceId: Int,
        val date: String,
        @ColumnInfo(name = "attendance_status")
        val attendanceStatus: String,
        val studentId: Int,
        val courseId: Int
)