package com.mobdeve.s19.group5.mco.main

import android.content.ClipData.Item
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mobdeve.s19.group5.mco.main.databinding.ItemTasksLayoutBinding

class TasksViewHolder(private val viewBinding: ItemTasksLayoutBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    fun bindData(task: Task){
        this.viewBinding.tasksTextTl.text = task.taskName
    }

}