package com.example.attend.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "attendance")
data class Attendance(
        @PrimaryKey(autoGenerate = true)
        val attendanceId: Int = 0,
        @ColumnInfo(name = "date_taken")
        val date: OffsetDateTime,
        @ColumnInfo(name = "attendance_status")
        val attendanceStatus: String,
        val studentId: Int,
        val courseId: Int,
        val courseCode: String,
        val studentFirstName: String,
        val studentLastName: String
)