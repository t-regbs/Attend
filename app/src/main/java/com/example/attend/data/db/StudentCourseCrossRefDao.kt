package com.example.attend.data.db

import androidx.room.*
import com.example.attend.data.model.StudentCourseCrossRef
import com.example.attend.data.model.StudentWithCourses

@Dao
interface StudentCourseCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentAndCourseId(studentCourseCrossRef: StudentCourseCrossRef)

    @Transaction
    @Query("SELECT * FROM students")
    suspend fun getStudentsWithCourses(): List<StudentWithCourses>
}