package com.example.attend.data.model

data class DownloadableStudentAttendance(
    val fromDate: String,
    val toDate: String,
    val students: MutableList<DownloadableStudentItem>
)