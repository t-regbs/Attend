package com.example.attend.ui.student

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.attend.data.model.Course
import com.example.attend.databinding.CourseSelectItemBinding


class CourseSelectAdapter(private val onItemCheckedListener: OnItemCheckedListener) :
        ListAdapter<Course, CourseSelectAdapter.ViewHolder>(COURSE_COMPARATOR) {

    interface OnItemCheckedListener {
        fun onItemCheck(course: Course?)
        fun onItemUncheck(course: Course?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.fromParent(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val course = getItem(position)
        course?.let {
            holder.bind(it, onItemCheckedListener)
        }
    }


    class ViewHolder private constructor(val courseSelectItemBinding: CourseSelectItemBinding):
            RecyclerView.ViewHolder(courseSelectItemBinding.root) {
        companion object{
            fun fromParent(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CourseSelectItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }

        fun bind(course: Course, onItemCheckedListener: OnItemCheckedListener){
            courseSelectItemBinding.course = course
            courseSelectItemBinding.cardview.setOnClickListener {
                courseSelectItemBinding.checkboxCourseCode.isChecked = !courseSelectItemBinding.checkboxCourseCode.isChecked
                if (courseSelectItemBinding.checkboxCourseCode.isChecked) {
                    onItemCheckedListener.onItemCheck(course)
                } else {
                    onItemCheckedListener.onItemUncheck(course)
                }
            }
            courseSelectItemBinding.executePendingBindings()
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

