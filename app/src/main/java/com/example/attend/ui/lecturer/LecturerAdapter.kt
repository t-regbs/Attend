package com.example.attend.ui.lecturer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.attend.data.model.Lecturer
import com.example.attend.databinding.LecturerListItemBinding

class LecturerAdapter():
    ListAdapter<Lecturer, LecturerAdapter.ViewHolder>(LECTURER_COMPARATOR){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.fromParent(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lecturer = getItem(position)
        lecturer?.let {
            holder.bind(it)
        }
    }


    class ViewHolder private constructor(val lecturerListItemBinding: LecturerListItemBinding):
        RecyclerView.ViewHolder(lecturerListItemBinding.root) {
        companion object{
            fun fromParent(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LecturerListItemBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }

        fun bind(lecturer: Lecturer){
            lecturerListItemBinding.lecturer = lecturer
            lecturerListItemBinding.executePendingBindings()
        }
    }

    companion object {
        private val LECTURER_COMPARATOR = object : DiffUtil.ItemCallback<Lecturer>() {
            override fun areItemsTheSame(oldItem: Lecturer, newItem: Lecturer): Boolean =
                oldItem.lecturerId == newItem.lecturerId

            override fun areContentsTheSame(oldItem: Lecturer, newItem: Lecturer): Boolean =
                oldItem == newItem
        }
    }

}