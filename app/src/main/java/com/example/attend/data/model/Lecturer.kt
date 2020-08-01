package com.example.attend.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lecturers")
data class Lecturer(
    @PrimaryKey(autoGenerate = true)
    val lecturerId: Int = 0,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    @ColumnInfo(name = "phone_number")
    val contactNum: String?,
    @ColumnInfo(name = "email_address")
    val email: String
)