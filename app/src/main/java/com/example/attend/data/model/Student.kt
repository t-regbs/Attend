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
    var firstName: String,
    @ColumnInfo(name = "last_name")
    var lastName: String,
    @ColumnInfo(name = "phone_number")
    var contactNum: String?,
    @ColumnInfo(name = "matric_number")
    var matNo: String
): Parcelable