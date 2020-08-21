package com.example.attend.ui.lecstudents

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.attend.R
import com.example.attend.data.model.Student
import com.example.attend.databinding.LecStudentAddFragmentBinding
import ir.androidexception.filepicker.dialog.SingleFilePickerDialog
import ir.androidexception.filepicker.interfaces.OnCancelPickerDialogListener
import ir.androidexception.filepicker.interfaces.OnConfirmDialogListener
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File
import java.io.FileInputStream


class LecStudentAddFragment : Fragment() {

    private val attendanceViewModel by viewModel<LecStudentViewModel>()
    private lateinit var binding: LecStudentAddFragmentBinding
    private lateinit var userId:String
    private lateinit var courseCode:String
    private lateinit var studentList: MutableList<Student>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = LecStudentAddFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = attendanceViewModel
        }
        val args: LecStudentAddFragmentArgs by navArgs()
        courseCode = args.courseCode

        return binding.root
    }

    private fun setupObserver() {
        attendanceViewModel.showSuccessToast.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), "Students Added From Excel", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        binding.btnAddStudents.setOnClickListener {
            insertStudents(studentList)
        }

        binding.btnFindDoc.setOnClickListener {
            findDoc()
        }

    }

    private fun findDoc() {
        if (permissionGranted()) {
            val singleFilePickerDialog = SingleFilePickerDialog(requireContext(),
                    OnCancelPickerDialogListener { Toast.makeText(context, getString(R.string.canceled), Toast.LENGTH_SHORT).show() },
                    OnConfirmDialogListener { files: Array<File> ->
                        binding.doc.editText?.setText(files[0].path)
                        readExcelFile(files[0])
                    })
            singleFilePickerDialog.show()
        } else {
            requestPermission()
        }

    }

    private fun permissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1)
    }

    private fun readExcelFile(file: File) {
        studentList = mutableListOf()
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl")
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl")
        try {
            val fileOut = FileInputStream(file)
            // Create a POI File System object

            // Create a POI File System object
//            val myFileSystem = POIFSFileSystem(fileOut)

            // Create a workbook using the File System
            val myWorkBook = XSSFWorkbook(fileOut)

            // Get the first sheet from workbook
            val mySheet = myWorkBook.getSheetAt(0)
            // We now need something to iterate through the cells.

            // We now need something to iterate through the cells.
            val rowIter: Iterator<Row> = mySheet.rowIterator()
            var rowno = 0
            while (rowIter.hasNext()) {
                Timber.e(" row no $rowno")
                var newStudent = Student(firstName = "", lastName = "", contactNum = "", matNo = "")
                if (rowno == 0) {
                    rowIter.next()
                } else {
                    val cellIter = rowIter.next().cellIterator()
                    var colno = 0
                    while (cellIter.hasNext()) {
                        val myCell = cellIter.next()
                        when (colno) {
                            0 -> newStudent.firstName = myCell.toString()
                            1 -> newStudent.lastName = myCell.toString()
                            2 -> {
                                val fmt = DataFormatter()
                                newStudent.contactNum = fmt.formatCellValue(myCell)
                            }
                            3 -> newStudent.matNo = myCell.toString()
                        }
                        colno++
                        Timber.d("%s%s", " Index :" + myCell.columnIndex + " -- ", myCell.toString())
                        Timber.d("$newStudent")
                    }
                    studentList.add(newStudent)
                }
                rowno++
            }
        }  catch (e: Exception) {
            Timber.e("error $e")
        }
    }

    private fun insertStudents(studentList: MutableList<Student>) {
        attendanceViewModel.addNewStudents(studentList, courseCode)
    }

}