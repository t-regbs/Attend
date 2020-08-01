package com.example.attend.data.db

import androidx.room.*
import com.example.attend.data.model.Lecturer
import com.example.attend.data.model.LecturerWithCourses

@Dao
interface LecturerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLecturer(lecturer: Lecturer)

    @Delete
    suspend fun deleteLecturer(lecturer: Lecturer)

    @Query("DELETE FROM lecturers")
    suspend fun clearLecturers()

    @Query("SELECT * FROM lecturers WHERE lecturerId = :id LIMIT 1")
    suspend fun getLecturer(id: Int): Lecturer

    @Query("SELECT * FROM lecturers ORDER BY lecturerId DESC")
    suspend fun getLecturers(): List<Lecturer>

    @Transaction
    @Query("SELECT * FROM lecturers")
    suspend fun getLecturersWithCourses(): List<LecturerWithCourses>
}