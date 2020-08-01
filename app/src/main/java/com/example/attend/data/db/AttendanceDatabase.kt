package com.example.attend.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.attend.data.model.*

@Database(entities = [Course::class,
    Lecturer::class,
    Student::class,
    Attendance::class,
    StudentCourseCrossRef::class],
        version = 1,
        exportSchema = false)
abstract class AttendanceDatabase: RoomDatabase() {
    abstract val attendanceDao: AttendanceDao
    abstract val courseDao: CourseDao
    abstract val lecturerDao: LecturerDao
    abstract val studentDao: StudentDao
    abstract val studentCourseCrossRefDao: StudentCourseCrossRefDao
}