package com.example.attend.data.db

import androidx.room.*
import com.example.attend.data.model.Course

@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course)

    @Update
    suspend fun updateCourse(course: Course)

    @Delete
    suspend fun deleteCourse(course: Course)

    @Query("DELETE FROM courses")
    suspend fun clearCourses()

    @Query("SELECT * FROM courses WHERE courseId = :id LIMIT 1")
    suspend fun getCourse(id: Int): Course
}