package com.example.attend.ui.attendancereport

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.attend.R
import com.example.attend.data.model.*
import com.example.attend.databinding.AttendanceReportFragmentBinding
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class AttendanceReportFragment : Fragment() {

    private val reportViewModel by viewModel<AttendanceReportViewModel>()
    private lateinit var binding: AttendanceReportFragmentBinding
    private val reportCourseAdapter = ReportCourseAdapter()
    private val reportStudentAdapter = ReportStudentAdapter()
    private lateinit var courseList: Array<Course>
    private lateinit var studentList: Array<Student>
    private lateinit var startDate: String
    private lateinit var endDate: String
    private lateinit var workbook: Workbook
    private var isByCourse: Boolean = false
    private val isUnsupportedDevice by lazy { Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val safeArgs: AttendanceReportFragmentArgs by navArgs()
        courseList = safeArgs.courseList ?: emptyArray()
        studentList = safeArgs.studentList ?: emptyArray()
        isByCourse = safeArgs.studentList == null
        formatAndAssignDate(safeArgs)

        if (isByCourse) {
            reportViewModel.getStudents(courseList)
        } else {
            reportViewModel.getCourses(studentList)
        }


        binding = AttendanceReportFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = reportViewModel
            if (isByCourse){
                rvCourses.adapter = reportCourseAdapter
            } else {
                rvCourses.adapter = reportStudentAdapter
            }
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun formatAndAssignDate(safeArgs: AttendanceReportFragmentArgs) {
        val format = "T00:00:00.000+01:00"
        val startList = safeArgs.startDate.split("/")
        val formattedStart = "20${startList[2]}-${startList[0]}-${startList[1]}"
        val endList = safeArgs.endDate.split("/")
        val formattedEnd = "20${endList[2]}-${endList[0]}-${endList[1]}"
        startDate = formattedStart + format
        endDate = formattedEnd + format
    }

    private fun setupCourseObserver() {
        reportViewModel.studentWithAttendanceList.observe(viewLifecycleOwner, Observer { list ->
            reportCourseAdapter.studentWithAttendanceList = list
            reportCourseAdapter.endDate = endDate
            reportCourseAdapter.startDate = startDate
            reportViewModel.courseWithAttendanceList.observe(viewLifecycleOwner, Observer {
                reportCourseAdapter.addHeaderAndSubmitList(it)
            })
        })

    }

    private fun setupStudentObserver() {
        reportViewModel.courseWithAttendanceList.observe(viewLifecycleOwner, Observer { list ->
            reportStudentAdapter.courseWithAttendanceList = list
            reportStudentAdapter.endDate = endDate
            reportStudentAdapter.startDate = startDate
            reportViewModel.studentWithAttendanceList.observe(viewLifecycleOwner, Observer {
                reportStudentAdapter.addHeaderAndSubmitList(it)
            })
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isByCourse) {
            reportViewModel.getCourseWithAttendances(courseList)
            setupCourseObserver()
        } else {
            reportViewModel.getStudentWithAttendances(studentList)
            setupStudentObserver()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_download -> {
                downloadAttendance()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun downloadAttendance() {
        val downloadableAttendance: Any
        downloadableAttendance = if (isByCourse) {
            val list = reportCourseAdapter.downloadableList
            DownloadableCourseAttendance(startDate, endDate, list)
        } else {
            val list = reportStudentAdapter.downloadableList
            DownloadableStudentAttendance(startDate, endDate, list)
        }

        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl")
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl")

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                (context as Activity?)!!,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
            return
        }

        workbook = if (isUnsupportedDevice) {
            Toast.makeText(context, "Unsupported device....", Toast.LENGTH_SHORT).show()
            HSSFWorkbook()
        } else {
            XSSFWorkbook()
        }

        if (isByCourse){
            createAndSaveCourseFile(downloadableAttendance as DownloadableCourseAttendance)
        } else {
            createAndSaveStudentFile(downloadableAttendance as DownloadableStudentAttendance)
        }
    }

    private fun createAndSaveCourseFile(downloadableAttendance: DownloadableCourseAttendance) {
        val fromDate = downloadableAttendance.fromDate.take(10)
        val toDate = downloadableAttendance.toDate.take(10)
        for (course in downloadableAttendance.courses) {
            val sheet = workbook.createSheet(course.courseCode)
            val header = sheet.header
            header.center =
                "Attendance Report from $fromDate to $toDate"
            val row1 = sheet.createRow(0)
            row1.createCell(0).setCellValue("Student")
            row1.createCell(1).setCellValue("Present")
            row1.createCell(2).setCellValue("Absent")
            row1.createCell(3).setCellValue("Excused")
            var rowNum = 1
            for (index in 0 until course.students.size) {
                val row = sheet.createRow(rowNum)
                row.createCell(0).setCellValue(course.students[index])
                row.createCell(1).setCellValue(course.presentList[index])
                row.createCell(2).setCellValue(course.absentList[index])
                row.createCell(3).setCellValue(course.excusedList[index])
                rowNum++
            }
        }
        val fileName = "attendance-$fromDate-$toDate.xlsx"
        val extStorageDirectory = Environment.getExternalStorageDirectory().toString()
        val folder = File(extStorageDirectory, "DownloadedAttendance")
        folder.mkdir()
        val file = File(folder, fileName)
        try {
            file.createNewFile()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        try {
            val fileOut =
                FileOutputStream(file) //Opening the file
            workbook.write(fileOut) //Writing all your row column inside the file
            fileOut.close() //closing the file and done
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun createAndSaveStudentFile(downloadableAttendance: DownloadableStudentAttendance) {
        val fromDate = downloadableAttendance.fromDate.take(10)
        val toDate = downloadableAttendance.toDate.take(10)
        for (student in downloadableAttendance.students) {
            val sheet = workbook.createSheet(student.studentName)
            val header = sheet.header
            header.center =
                "Attendance Report from $fromDate to $toDate"
            val row1 = sheet.createRow(0)
            row1.createCell(0).setCellValue("Course")
            row1.createCell(1).setCellValue("Present")
            row1.createCell(2).setCellValue("Absent")
            row1.createCell(3).setCellValue("Excused")
            var rowNum = 1
            for (index in 0 until student.courses.size) {
                val row = sheet.createRow(rowNum)
                row.createCell(0).setCellValue(student.courses[index])
                row.createCell(1).setCellValue(student.presentList[index])
                row.createCell(2).setCellValue(student.absentList[index])
                row.createCell(3).setCellValue(student.excusedList[index])
                rowNum++
            }
        }
        val fileName = "attendance-$fromDate-$toDate.xlsx"
        val extStorageDirectory = Environment.getExternalStorageDirectory().toString()
        val folder = File(extStorageDirectory, "DownloadedAttendance")
        folder.mkdir()
        val file = File(folder, fileName)
        try {
            file.createNewFile()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        try {
            val fileOut =
                FileOutputStream(file) //Opening the file
            workbook.write(fileOut) //Writing all your row column inside the file
            fileOut.close() //closing the file and done
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}