package com.example.attend.ui.lecstudents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.attend.data.model.Student
import com.example.attend.data.model.StudentWithCourses
import com.example.attend.databinding.StudentListItemBinding
import com.example.attend.databinding.StudentNoCourseListItemBinding

class StudentAdapter :
    ListAdapter<Student, StudentAdapter.ViewHolder>(COMPARATOR){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.fromParent(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = getItem(position)
        student?.let {
            holder.bind(it)
        }
    }


    class ViewHolder private constructor(val studentListItemBinding: StudentNoCourseListItemBinding):
        RecyclerView.ViewHolder(studentListItemBinding.root) {
        companion object{
            fun fromParent(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StudentNoCourseListItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }

        fun bind(student: Student){
            val empty = ""
            studentListItemBinding.student = student
            studentListItemBinding.executePendingBindings()
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Student>() {
            override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean =
                oldItem.studentId == newItem.studentId

            override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean =
                oldItem == newItem
        }
    }

}