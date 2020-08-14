package com.example.attend.data.db

import androidx.room.*
import com.example.attend.data.model.CourseWithStudents
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

    @Transaction
    @Query("SELECT * FROM courses")
    suspend fun getCoursesWithStudents(): List<CourseWithStudents>

    @Transaction
    @Query("SELECT * FROM courses WHERE course_code = :courseCode LIMIT 1")
    suspend fun getCoursesWithStudentsFromCode(courseCode: String): CourseWithStudents

    @Transaction
    @Query("SELECT * FROM students WHERE studentId = :studentId LIMIT 1")
    suspend fun getStudentsWithCoursesFromId(studentId: Int): StudentWithCourses

}