package com.example.attend.data.model

data class DownloadableCourseItem(
    val courseCode: String,
    val students: MutableList<String>,
    val presentList: MutableList<String>,
    val absentList: MutableList<String>,
    val excusedList: MutableList<String>
)