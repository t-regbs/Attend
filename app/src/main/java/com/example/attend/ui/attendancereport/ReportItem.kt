package com.example.attend.ui.attendancereport

import com.example.attend.data.model.CourseWithAttendances

sealed class ReportItem {
    abstract val id: Int
    data class CourseWithAttendanceItem(val courseWithAttendances: CourseWithAttendances): ReportItem() {
        override val id = courseWithAttendances.course.courseId
    }

    object Header: ReportItem() {
        override val id = Int.MIN_VALUE
    }
}