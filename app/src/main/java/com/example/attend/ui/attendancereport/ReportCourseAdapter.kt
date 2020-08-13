package com.example.attend.ui.attendancereport

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.attend.data.model.CourseWithAttendances
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
                    holder.bind(it.courseWithAttendances, studentWithAttendanceList, startDate, endDate)
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

        fun bind(courseWithAttendances: CourseWithAttendances,
                 studentWithAttendanceList: List<StudentWithAttendances>,
                 startDate: String,
                 endDate: String
        ){
            courseReportItemBinding.courseWithAttendances = courseWithAttendances
            val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            val start = formatter.parse(startDate, OffsetDateTime::from)
            val end = formatter.parse(endDate, OffsetDateTime::from)
            val courseId = courseWithAttendances.course.courseId
            for(student in studentWithAttendanceList) {
                if (!student.attendanceList.any { it.courseId == courseId }) {
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
                val studentTv = TextView(courseReportItemBinding.root.context)
                val presentTv = TextView(courseReportItemBinding.root.context)
                val absentTv = TextView(courseReportItemBinding.root.context)
                val excusedTv = TextView(courseReportItemBinding.root.context)

                studentTv.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                presentTv.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                absentTv.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
                excusedTv.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)

                studentTv.text = "${student.student.firstName} ${student.student.lastName}"
                studentTv.gravity = Gravity.CENTER_HORIZONTAL
                presentTv.text = presCount.toString()
                presentTv.gravity = Gravity.CENTER_HORIZONTAL
                absentTv.text = absCount.toString()
                absentTv.gravity = Gravity.CENTER_HORIZONTAL
                excusedTv.text = exCount.toString()
                excusedTv.gravity = Gravity.CENTER_HORIZONTAL
                courseReportItemBinding.lnrStudent.addView(studentTv)
                courseReportItemBinding.lnrPresent.addView(presentTv)
                courseReportItemBinding.lnrAbsent.addView(absentTv)
                courseReportItemBinding.lnrExcused.addView(excusedTv)
        }
            courseReportItemBinding.executePendingBindings()
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