package com.example.attend.data.repository

import com.example.attend.data.db.*
import com.example.attend.data.model.*
import org.threeten.bp.OffsetDateTime
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
    suspend fun getStudentWithCoursesFromId(studentId: Int): StudentWithCourses {
        return studentCourseCrossRefDao.getStudentsWithCoursesFromId(studentId)
    }

    suspend fun getCourseWithStudents(): List<CourseWithStudents> {
        return studentCourseCrossRefDao.getCoursesWithStudents()
    }

    suspend fun getCourseWithStudentsFromCourseList(courseList: List<Course>): List<CourseWithStudents> {
        val ans = ArrayList<CourseWithStudents>()
        for (course in courseList) {
            val thing = getCourseWithStudentsFromCode(course.courseCode)
            ans.add(thing)
        }
        return ans
    }

    suspend fun getCourseWithStudentsFromCodeList(list: List<CourseWithAttendances>): List<CourseWithStudents> {
        val ans = ArrayList<CourseWithStudents>()
        for (item in list) {
            val thing = getCourseWithStudentsFromCode(item.course.courseCode)
            ans.add(thing)
        }
        return ans
    }

    suspend fun getStudentWithCoursesFromIdList(list: List<StudentWithAttendances>): List<StudentWithCourses> {
        val ans = ArrayList<StudentWithCourses>()
        for (item in list) {
            val thing = getStudentWithCoursesFromId(item.student.studentId)
            ans.add(thing)
        }
        return ans
    }

    suspend fun addNewAttendance(
            hmAttendance: HashMap<Student, String>,
            courseId: Int,
            courseCode: String
    ): Array<Long> {
        val list = ArrayList<Attendance>()
        for (item in hmAttendance) {
            list.add(Attendance(date = OffsetDateTime.now(),
                studentId = item.key.studentId,
                courseId = courseId,
                attendanceStatus = item.value,
                studentFirstName = item.key.firstName,
                studentLastName = item.key.lastName,
                courseCode = courseCode
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

    suspend fun getCourseWithAttendancesListFromId(courseList: Array<Course>): List<CourseWithAttendances> {
        val list = ArrayList<CourseWithAttendances>()
        for (course in courseList) {
            val courseWithAttendances = getCourseWithAttendanceFromId(course.courseId)
            list.add(courseWithAttendances)
        }
        return list
    }

    suspend fun getStudentWithAttendancesListFromId(studentList: Array<Student>): List<StudentWithAttendances> {
        val list = ArrayList<StudentWithAttendances>()
        for (student in studentList) {
            val studentWithAttendance = getStudentWithAttendanceFromId(student.studentId)
            list.add(studentWithAttendance)
        }
        return list
    }

    private suspend fun getCourseWithAttendanceFromId(id: Int): CourseWithAttendances {
        return attendanceDao.getCourseWithAttendanceFromCourseId(id)
    }

    suspend fun getStudentsWithAttendanceFromStudent(studentList: List<CourseWithStudents>): List<StudentWithAttendances> {
        val list = ArrayList<StudentWithAttendances>()
        for (item in studentList) {
            for (student in item.students) {
                val studentWithAttendances = getStudentWithAttendanceFromId(student.studentId)
                if (!list.contains(studentWithAttendances)) {
                    list.add(studentWithAttendances)
                }
            }
        }
        return list
    }

    suspend fun getCoursesWithAttendanceFromCourse(courseList: List<StudentWithCourses>): List<CourseWithAttendances> {
        val list = ArrayList<CourseWithAttendances>()
        for (item in courseList) {
            for (course in item.courses) {
                val courseWithAttendances = getCourseWithAttendanceFromId(course.courseId)
                if (!list.contains(courseWithAttendances)) {
                    list.add(courseWithAttendances)
                }
            }
        }
        return list
    }

    private suspend fun getStudentWithAttendanceFromId(studentId: Int): StudentWithAttendances {
        return attendanceDao.getStudentWithAttendanceFromId(studentId)
    }
}