package com.example.attend.data.model

data class DownloadableCourseAttendance(
    val fromDate: String,
    val toDate: String,
    val courses: MutableList<DownloadableCourseItem>
)