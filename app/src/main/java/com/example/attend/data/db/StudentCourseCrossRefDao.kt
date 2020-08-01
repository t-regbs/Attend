package com.example.attend.data.db

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.attend.data.model.StudentCourseCrossRef
import com.example.attend.data.model.StudentWithCourses

interface StudentCourseCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentAndCourseId(studentCourseCrossRef: StudentCourseCrossRef)

    @Transaction
    @Query("SELECT * FROM students")
    suspend fun getStudentsWithCourses(): List<StudentWithCourses>
}