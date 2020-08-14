package com.example.attend.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "students")
@Parcelize
data class Student(
    @PrimaryKey(autoGenerate = true)
    val studentId: Int = 0,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    @ColumnInfo(name = "phone_number")
    val contactNum: String?,
    @ColumnInfo(name = "matric_number")
    val matNo: String
): Parcelable