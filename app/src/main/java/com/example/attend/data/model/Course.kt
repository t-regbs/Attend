package com.example.attend.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "courses")
@Parcelize
data class Course(
    @PrimaryKey(autoGenerate = true)
    val courseId: Int = 0,
    @ColumnInfo(name = "course_code")
    val courseCode: String,
    @ColumnInfo(name = "course_name")
    val courseName: String,
    @ColumnInfo(name = "lecturer_taking_id")
    val lecturerId: Int
): Parcelable