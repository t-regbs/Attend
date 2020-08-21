package com.example.attend.ui.takeattendance

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.attend.R
import com.example.attend.data.model.Student
import com.example.attend.databinding.TakeAttendanceListItemBinding

class TakeAttendanceAdapter(private val onItemClickedListener: OnItemClickedListener) :
    ListAdapter<Student, TakeAttendanceAdapter.ViewHolder>(COMPARATOR){

    interface OnItemClickedListener {
        fun onRbClicked(status: String, student: Student)
        fun onButtonClicked(student: Student)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.fromParent(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = getItem(position)
        student?.let {
            holder.bind(it, onItemClickedListener)
        }
    }


    class ViewHolder private constructor(val takeAttendanceListItemBinding: TakeAttendanceListItemBinding):
        RecyclerView.ViewHolder(takeAttendanceListItemBinding.root) {
        companion object{
            fun fromParent(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TakeAttendanceListItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }

        fun bind(student: Student, onItemClickedListener: OnItemClickedListener){
            takeAttendanceListItemBinding.student = student
            takeAttendanceListItemBinding.rdgrpStatus.setOnCheckedChangeListener { group, checkedId ->
                when(checkedId) {
                    R.id.rdbtn_present -> onItemClickedListener.onRbClicked("Present", student)
                    R.id.rdbtn_absent -> onItemClickedListener.onRbClicked("Absent", student)
                    R.id.rdbtn_excused -> onItemClickedListener.onRbClicked("Excused", student)
                }
            }
            if (BiometricManager.from(
                   takeAttendanceListItemBinding.root.context).canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS){
                takeAttendanceListItemBinding.btnBiometric.setOnClickListener {
                    onItemClickedListener.onButtonClicked(student)
                }
            } else {
//                Toast.makeText(context, "Pri", Toast.LENGTH_SHORT).show()
                    //Nothing for now
            }

            takeAttendanceListItemBinding.executePendingBindings()
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