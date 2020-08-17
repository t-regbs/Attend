package com.example.attend.ui.attendancereport

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.gridlayout.widget.GridLayout
import androidx.gridlayout.widget.GridLayout.LayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.attend.R
import com.example.attend.data.model.CourseWithAttendances
import com.example.attend.data.model.DownloadableCourseItem
import com.example.attend.data.model.StudentWithAttendances
import com.example.attend.databinding.CourseReportHeaderBinding
import com.example.attend.databinding.CourseReportItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter


private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class ReportCourseAdapter :
        ListAdapter<ReportItem, RecyclerView.ViewHolder>(COMPARATOR){

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    lateinit var studentWithAttendanceList: List<StudentWithAttendances>
    lateinit var startDate: String
    lateinit var endDate: String
    val downloadableList: MutableList<DownloadableCourseItem> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.fromParent(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    fun addHeaderAndSubmitList(list: List<CourseWithAttendances>) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(ReportItem.Header)
                else -> listOf(ReportItem.Header) + list.map { ReportItem.CourseWithAttendanceItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ViewHolder -> {
                val course = getItem(position) as ReportItem.CourseWithAttendanceItem
                course.let {
                    holder.bind(it.courseWithAttendances, studentWithAttendanceList, startDate, endDate, downloadableList)
                }
            }
            is TextViewHolder -> {
                val start = startDate.substring(0, 10)
                val end = endDate.substring(0, 10)
                val text = "Attendance Report from $start to $end"
                holder.bind(text)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ReportItem.Header -> ITEM_VIEW_TYPE_HEADER
            is ReportItem.CourseWithAttendanceItem -> ITEM_VIEW_TYPE_ITEM
            else -> -1
        }
    }

    class TextViewHolder private constructor(private val headerTextItemBinding: CourseReportHeaderBinding):
            RecyclerView.ViewHolder(headerTextItemBinding.root) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CourseReportHeaderBinding.inflate(layoutInflater, parent, false)
                return TextViewHolder(binding)
            }
        }

        fun bind(headerText: String) {
            headerTextItemBinding.headerText = headerText
            headerTextItemBinding.executePendingBindings()
        }
    }

    class ViewHolder private constructor(private val courseReportItemBinding: CourseReportItemBinding):
            RecyclerView.ViewHolder(courseReportItemBinding.root) {
        companion object{
            fun fromParent(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CourseReportItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }

        fun bind(
            courseWithAttendances: CourseWithAttendances,
            studentWithAttendanceList: List<StudentWithAttendances>,
            startDate: String,
            endDate: String,
            downloadableList: MutableList<DownloadableCourseItem>
        ){
            courseReportItemBinding.courseWithAttendances = courseWithAttendances
            val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            val start = formatter.parse(startDate, OffsetDateTime::from)
            val end = formatter.parse(endDate, OffsetDateTime::from)
            val courseId = courseWithAttendances.course.courseId
            var rows = studentWithAttendanceList.size
            var row: Int = 1
            val downloadableCourseItem = DownloadableCourseItem(
                courseCode = courseWithAttendances.course.courseCode,
                students = mutableListOf(),
                presentList = mutableListOf(),
                absentList = mutableListOf(),
                excusedList = mutableListOf()
                )
            for(student in studentWithAttendanceList) {
                if (!student.attendanceList.any { it.courseId == courseId }) {
                    rows--
                    continue
                }
                var presCount = 0
                var absCount = 0
                var exCount = 0
                for (item in student.attendanceList) {
                    val withinRange = start < item.date && item.date < end
                    if (item.attendanceStatus == "Present" && item.courseId == courseId && withinRange) presCount++
                    else if (item.attendanceStatus == "Absent" && item.courseId == courseId && withinRange) absCount++
                    else if (item.attendanceStatus == "Excused" && item.courseId == courseId && withinRange) exCount++
                }

                val columns = 4
                courseReportItemBinding.grid.columnCount = columns


                for (j in 0 until columns) {
                    val titleStudentTv = TextView(courseReportItemBinding.root.context)
                    val param = LayoutParams(
                        GridLayout.spec(0, 1f),
                        GridLayout.spec(j, 1f)).apply {
                        width = ViewGroup.LayoutParams.WRAP_CONTENT
                        height = ViewGroup.LayoutParams.WRAP_CONTENT
                        setGravity(Gravity.FILL)
                    }
                    when (j) {
                        0 -> titleStudentTv.text = courseReportItemBinding.root.context.getString(R.string.menu_student)
                        1 -> titleStudentTv.text = courseReportItemBinding.root.context.getString(R.string.present)
                        2 -> titleStudentTv.text = courseReportItemBinding.root.context.getString(R.string.absent)
                        3 -> titleStudentTv.text = courseReportItemBinding.root.context.getString(R.string.excused)
                    }
                    courseReportItemBinding.grid.addView(titleStudentTv, param)
                }

                val studentTv = TextView(courseReportItemBinding.root.context)
                val presentTv = TextView(courseReportItemBinding.root.context)
                val absentTv = TextView(courseReportItemBinding.root.context)
                val excusedTv = TextView(courseReportItemBinding.root.context)
                val param1 = LayoutParams(
                    GridLayout.spec(row, 1f),
                    GridLayout.spec(0, 1f)).apply {
                    width = ViewGroup.LayoutParams.WRAP_CONTENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    setGravity(Gravity.FILL)
                }
                val param2 = LayoutParams(
                    GridLayout.spec(row, 1f),
                    GridLayout.spec(1, 1f)).apply {
                    width = ViewGroup.LayoutParams.WRAP_CONTENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    setGravity(Gravity.FILL)
                }
                val param3 = LayoutParams(
                    GridLayout.spec(row, 1f),
                    GridLayout.spec(2, 1f)).apply {
                    width = ViewGroup.LayoutParams.WRAP_CONTENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    setGravity(Gravity.FILL)
                }
                val param4 = LayoutParams(
                    GridLayout.spec(row, 1f),
                    GridLayout.spec(3, 1f)).apply {
                    width = ViewGroup.LayoutParams.WRAP_CONTENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    setGravity(Gravity.FILL)
                }
                val fullStudentName = "${student.student.firstName} ${student.student.lastName}"
                studentTv.text = fullStudentName
                presentTv.text = presCount.toString()
                absentTv.text = absCount.toString()
                excusedTv.text = exCount.toString()
                downloadableCourseItem.apply {
                    students.add(fullStudentName)
                    presentList.add(presCount.toString())
                    absentList.add(absCount.toString())
                    excusedList.add(exCount.toString())
                }
                courseReportItemBinding.grid.apply {
                    addView(studentTv, param1)
                    addView(presentTv, param2)
                    addView(absentTv, param3)
                    addView(excusedTv, param4)
                }
                row++
        }
            courseReportItemBinding.executePendingBindings()
            downloadableList.add(downloadableCourseItem)
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<ReportItem>() {
            override fun areItemsTheSame(oldItem: ReportItem, newItem: ReportItem): Boolean =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ReportItem, newItem: ReportItem): Boolean =
                    oldItem == newItem
        }
    }

}