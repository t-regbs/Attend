package com.example.attend.ui.attendance

import com.example.attend.data.model.Student
import com.example.attend.databinding.StudentSelectItemBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class StudentSelectAdapter(private val onItemCheckedListener: OnItemCheckedListener) :
    ListAdapter<Student, StudentSelectAdapter.ViewHolder>(COMPARATOR) {

    interface OnItemCheckedListener {
        fun onItemCheck(student: Student?)
        fun onItemUncheck(student: Student?)
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


    class ViewHolder private constructor(val studentSelectItemBinding: StudentSelectItemBinding):
        RecyclerView.ViewHolder(studentSelectItemBinding.root) {
        companion object{
            fun fromParent(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StudentSelectItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }

        fun bind(student: Student, onItemCheckedListener: OnItemCheckedListener){
            studentSelectItemBinding.student = student
            studentSelectItemBinding.cardview.setOnClickListener {
                studentSelectItemBinding.checkboxStudentFirstName.isChecked = !studentSelectItemBinding.checkboxStudentFirstName.isChecked
                if (studentSelectItemBinding.checkboxStudentFirstName.isChecked) {
                    onItemCheckedListener.onItemCheck(student)
                } else {
                    onItemCheckedListener.onItemUncheck(student)
                }
            }
            studentSelectItemBinding.executePendingBindings()
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

