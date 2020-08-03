package com.example.attend.ui.course

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.attend.data.model.Course
import com.example.attend.databinding.CoursesRvItemBinding

class CourseAdapter :
ListAdapter<Course, CourseAdapter.ViewHolder>(COURSE_COMPARATOR){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.fromParent(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val course = getItem(position)
        course?.let {
            holder.bind(it)
        }
    }


    class ViewHolder private constructor(val coursesRvItemBinding: CoursesRvItemBinding):
        RecyclerView.ViewHolder(coursesRvItemBinding.root) {
        companion object{
            fun fromParent(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CoursesRvItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }

        fun bind(course: Course){
            coursesRvItemBinding.course = course
            coursesRvItemBinding.executePendingBindings()
        }
    }

    companion object {
        private val COURSE_COMPARATOR = object : DiffUtil.ItemCallback<Course>() {
            override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean =
                oldItem.courseId == newItem.courseId

            override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean =
                oldItem == newItem
        }
    }

}