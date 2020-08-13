package com.example.attend.ui.attendancereport

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.attend.data.db.Converters
import com.example.attend.data.model.Attendance
import com.example.attend.data.model.CourseWithAttendances
import com.example.attend.data.model.StudentWithAttendances
import com.example.attend.databinding.CourseReportItemBinding
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter


class ReportCourseAdapter :
        ListAdapter<CourseWithAttendances, ReportCourseAdapter.ViewHolder>(COMPARATOR){

    lateinit var studentWithAttendanceList: List<StudentWithAttendances>
    lateinit var startDate: String
    lateinit var endDate: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.fromParent(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val course = getItem(position)
        course?.let {
            holder.bind(it, studentWithAttendanceList, startDate, endDate)
        }
    }


    class ViewHolder private constructor(val courseReportItemBinding: CourseReportItemBinding):
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
        private val COMPARATOR = object : DiffUtil.ItemCallback<CourseWithAttendances>() {
            override fun areItemsTheSame(oldItem: CourseWithAttendances, newItem: CourseWithAttendances): Boolean =
                    oldItem.course.courseId == newItem.course.courseId

            override fun areContentsTheSame(oldItem: CourseWithAttendances, newItem: CourseWithAttendances): Boolean =
                    oldItem == newItem
        }
    }

}