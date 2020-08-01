package com.example.attend.data.db

import androidx.room.*
import com.example.attend.data.model.Attendance

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: Attendance)

    @Delete
    suspend fun deleteAttendance(attendance: Attendance)

    @Query("DELETE FROM attendance")
    suspend fun clearAttendance()

}