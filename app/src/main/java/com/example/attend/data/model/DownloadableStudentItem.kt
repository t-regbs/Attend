package com.example.attend.data.model

data class DownloadableStudentItem(
    val studentName: String,
    val courses: MutableList<String>,
    val presentList: MutableList<String>,
    val absentList: MutableList<String>,
    val excusedList: MutableList<String>
)