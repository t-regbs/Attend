package com.example.attend.data.db

import androidx.room.*
import com.example.attend.data.model.StudentCourseCrossRef
import com.example.attend.data.model.StudentWithCourses

@Dao
interface StudentCourseCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudentAndCourseId(studentCourseCrossRef: StudentCourseCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMultipleStudentAndCourseId(studentCourseCrossRefs: List<StudentCourseCrossRef>): Array<Long>

    @Transaction
    @Query("SELECT * FROM students")
    suspend fun getStudentsWithCourses(): List<StudentWithCourses>
}