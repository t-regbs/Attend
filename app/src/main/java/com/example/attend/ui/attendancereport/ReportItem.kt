package com.example.attend.ui.attendancereport

import com.example.attend.data.model.CourseWithAttendances
import com.example.attend.data.model.StudentWithAttendances

sealed class ReportItem {
    abstract val id: Int
    data class CourseWithAttendanceItem(val courseWithAttendances: CourseWithAttendances): ReportItem() {
        override val id = courseWithAttendances.course.courseId
    }

    data class StudentWithAttendanceItem(val studentWithAttendances: StudentWithAttendances): ReportItem() {
        override val id = studentWithAttendances.student.studentId
    }

    object Header: ReportItem() {
        override val id = Int.MIN_VALUE
    }
}