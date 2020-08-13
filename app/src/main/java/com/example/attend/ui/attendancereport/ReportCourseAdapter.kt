package com.example.attend.ui.attendancereport

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.attend.data.model.CourseWithAttendances
import com.example.attend.data.model.StudentWithAttendances
import com.example.attend.databinding.CourseReportItemBinding


class ReportCourseAdapter :
        ListAdapter<CourseWithAttendances, ReportCourseAdapter.ViewHolder>(COMPARATOR){

    lateinit var studentWithAttendanceList: List<StudentWithAttendances>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.fromParent(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val course = getItem(position)
        course?.let {
            holder.bind(it, studentWithAttendanceList)
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

        fun bind(courseWithAttendances: CourseWithAttendances, studentWithAttendanceList: List<StudentWithAttendances>){
            courseReportItemBinding.courseWithAttendances = courseWithAttendances

            for(student in studentWithAttendanceList) {
                var presCount = 0
                var absCount = 0
                var exCount = 0
                for (item in student.attendanceList) {
                    when (item.attendanceStatus) {
                        "Present" -> presCount++
                        "Absent" -> absCount++
                        "Excused" -> exCount++
                    }
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
                presentTv.text = presCount.toString()
                absentTv.text = absCount.toString()
                excusedTv.text = exCount.toString()
                courseReportItemBinding.lnrStudent.addView(studentTv)
                courseReportItemBinding.lnrPresent.addView(presentTv)
                courseReportItemBinding.lnrAbsent.addView(absentTv)
                courseReportItemBinding.lnrExcused.addView(excusedTv)
            }

//            for (item in courseWithAttendances.attendanceList) {
//
//            }
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