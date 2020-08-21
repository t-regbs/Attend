package com.example.attend.data.db

import androidx.room.*
import com.example.attend.data.model.Student
import com.example.attend.data.model.StudentWithCourses

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student): Long

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("DELETE FROM students")
    suspend fun clearStudents()

    @Query("SELECT * FROM students WHERE studentId = :id LIMIT 1")
    suspend fun getStudent(id: Int): Student

    @Query("SELECT * FROM students WHERE matric_number = :matNo LIMIT 1")
    suspend fun getStudentFromMatNo(matNo: String): Student?

    @Query("SELECT * FROM students ORDER BY studentId DESC")
    suspend fun getStudents(): List<Student>
}