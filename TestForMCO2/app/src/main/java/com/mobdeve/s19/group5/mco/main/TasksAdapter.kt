package com.mobdeve.s19.group5.mco.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView.Adapter
import android.content.Intent
import androidx.activity.ComponentActivity
import com.mobdeve.s19.group5.mco.main.databinding.ItemTasksLayoutBinding
import java.util.ArrayList

class TasksAdapter(private val data: ArrayList<Task>, private val activity: MainActivity): Adapter<TasksViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val itemTasksViewBinding: ItemTasksLayoutBinding = ItemTasksLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val tasksViewHolder = TasksViewHolder(itemTasksViewBinding)

//        tasksViewHolder.itemView.setOnClickListener {
//            val intent: Intent = Intent(tasksViewHolder.itemView.context, TaskDetailsActivity::class.java)
//            intent.putExtra(TaskDetailsActivity.TASK_NAME_KEY, itemTasksViewBinding.tasksTextTl.text.toString())
//            intent.putExtra(TaskDetailsActivity.TASK_DESC_KEY, itemTasksViewBinding.tasksDescTl.text.toString())
//            intent.putExtra(TaskDetailsActivity.TASK_DATE_KEY, itemTasksViewBinding.tasksDateTl.text.toString())
//            intent.putExtra(TaskDetailsActivity.TASK_TIME_KEY, itemTasksViewBinding.tasksTimeTl.text.toString())
//            intent.putExtra(TaskDetailsActivity.TASK_STATUS_KEY, itemTasksViewBinding.tasksStatusTl.text.toString())
//            tasksViewHolder.itemView.context.startActivity(intent)
//        }
        return tasksViewHolder
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }


}