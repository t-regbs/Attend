package com.example.attend.data.db

import androidx.room.*
import com.example.attend.data.model.Course
import com.example.attend.data.model.Lecturer

@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course)

    @Update
    suspend fun updateCourse(course: Course)

    @Delete
    suspend fun deleteCourse(course: Course)

    @Query("SELECT * FROM courses WHERE courseId = :id LIMIT 1")
    suspend fun getCourse(id: Int): Course

    @Query("SELECT courseId FROM courses WHERE course_code = :courseCode LIMIT 1")
    suspend fun getCourseIdFromCourseCode(courseCode: String): Int

    @Query("SELECT * FROM courses WHERE lecturer_taking_id = :id ORDER BY courseId DESC")
    suspend fun getCourseFromLecturerId(id: Int): List<Course>

    @Query("SELECT * FROM courses ORDER BY courseId DESC")
    suspend fun getCourses(): List<Course>
}