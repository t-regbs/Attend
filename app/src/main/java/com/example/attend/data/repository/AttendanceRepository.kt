package com.example.attend.data.repository

import com.example.attend.data.db.*
import com.example.attend.data.model.*
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.OffsetTime
import timber.log.Timber

class AttendanceRepository(private val lecturerDao: LecturerDao,
                           private val courseDao: CourseDao,
                           private val studentDao: StudentDao,
                           private val studentCourseCrossRefDao: StudentCourseCrossRefDao,
                           private val attendanceDao: AttendanceDao
) {

    suspend fun getLecturers(): List<Lecturer> {
        return lecturerDao.getLecturers()
    }

    suspend fun addNewLecturer(firstName: String, lastName: String, contactNo: String, email: String) {
        val newLecturer = Lecturer(firstName = firstName,
            lastName = lastName,
            email = email,
            contactNum = contactNo)
        lecturerDao.insertLecturer(newLecturer)
    }

    suspend fun addNewCourse(courseCode: String, courseName: String, lecturerId: Int) {
        val newCourse = Course(courseCode = courseCode,
            courseName = courseName,
            lecturerId = lecturerId)
        courseDao.insertCourse(newCourse)
    }

    suspend fun getCourses(): List<Course> {
        return courseDao.getCourses()
    }

    suspend fun getStudentWithCourses(): List<StudentWithCourses> {
        return studentCourseCrossRefDao.getStudentsWithCourses()
    }
    suspend fun getCourseWithStudentsFromCode(courseCode: String): CourseWithStudents {
        return studentCourseCrossRefDao.getCoursesWithStudentsFromCode(courseCode)
    }

    suspend fun addNewAttendance(
        hmAttendance: HashMap<Student, String>,
        courseId: Int
    ): Array<Long> {
        val list = ArrayList<Attendance>()
        for (item in hmAttendance) {
            list.add(Attendance(date = OffsetDateTime.now(),
                studentId = item.key.studentId,
                courseId = courseId,
                attendanceStatus = item.value
            ))
        }
        return attendanceDao.insertMultipleAttendance(list)
    }

    suspend fun addStudentWithSelectedCourses(
            firstName: String,
            lastName: String,
            contactNo: String,
            matricNo: String,
            selectedCourses: MutableList<Course>): Array<Long> {
        val newStudent = Student(firstName = firstName,
                lastName = lastName,
                contactNum = contactNo,
                matNo = matricNo)
        val studentInserted = studentDao.insertStudent(newStudent)
        Timber.d("newStudentId: $studentInserted")

        var crossRefs = ArrayList<StudentCourseCrossRef>()
        for (course in selectedCourses) {
            crossRefs.add(StudentCourseCrossRef(studentInserted.toInt(), course.courseId))
        }
        val crossRefsInserted = studentCourseCrossRefDao.insertMultipleStudentAndCourseId(crossRefs)
        crossRefsInserted.forEach { Timber.d("id: $it") }
        return crossRefsInserted
    }

    suspend fun getCoursesWithId(lecturerId: Int): List<Course> {
        return courseDao.getCourseFromLecturerId(lecturerId)
    }

    suspend fun getCourseIdFromCourseCode(courseCode: String): Int {
        return courseDao.getCourseIdFromCourseCode(courseCode)
    }
}