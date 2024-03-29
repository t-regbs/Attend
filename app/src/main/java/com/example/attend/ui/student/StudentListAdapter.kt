package com.example.attend.ui.student

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.attend.data.model.StudentWithCourses
import com.example.attend.databinding.StudentListItemBinding

class StudentListAdapter :
        ListAdapter<StudentWithCourses, StudentListAdapter.ViewHolder>(COURSE_COMPARATOR){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.fromParent(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val studentWithCourses = getItem(position)
        studentWithCourses?.let {
            holder.bind(it)
        }
    }


    class ViewHolder private constructor(val studentListItemBinding: StudentListItemBinding):
            RecyclerView.ViewHolder(studentListItemBinding.root) {
        companion object{
            fun fromParent(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StudentListItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }

        fun bind(studentWithCourses: StudentWithCourses){
            val empty = ""
            studentListItemBinding.studentWithCourses = studentWithCourses
            for (course in studentWithCourses.courses) {
                val courseTv = TextView(studentListItemBinding.root.context)

                courseTv.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT)


                courseTv.text = course.courseCode
                courseTv.setPadding(4, 4, 4, 4)
                studentListItemBinding.chipGroupCourses.addView(courseTv)
            }
            studentListItemBinding.executePendingBindings()
        }
    }

    companion object {
        private val COURSE_COMPARATOR = object : DiffUtil.ItemCallback<StudentWithCourses>() {
            override fun areItemsTheSame(oldItem: StudentWithCourses, newItem: StudentWithCourses): Boolean =
                    oldItem.student.studentId == newItem.student.studentId

            override fun areContentsTheSame(oldItem: StudentWithCourses, newItem: StudentWithCourses): Boolean =
                    oldItem == newItem
        }
    }

}