package com.example.attend.data.db

import androidx.room.*
import com.example.attend.data.model.Attendance
import com.example.attend.data.model.CourseWithAttendances
import com.example.attend.data.model.StudentWithAttendances

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: Attendance)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMultipleAttendance(attendanceList: List<Attendance>): Array<Long>

    @Delete
    suspend fun deleteAttendance(attendance: Attendance)

    @Query("DELETE FROM attendance")
    suspend fun clearAttendance()

    @Query("SELECT * FROM attendance WHERE courseId = :courseId")
    fun getAttendanceFromCourseId(courseId: Int): List<Attendance>

    @Transaction
    @Query("SELECT * FROM courses WHERE courseId = :courseId LIMIT 1")
    suspend fun getCourseWithAttendanceFromCourseId(courseId: Int): CourseWithAttendances

    @Transaction
    @Query("SELECT * FROM students WHERE studentId = :studentId LIMIT 1")
    suspend fun getStudentWithAttendanceFromId(studentId: Int): StudentWithAttendances

}