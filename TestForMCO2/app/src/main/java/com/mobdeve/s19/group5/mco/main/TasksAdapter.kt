package com.mobdeve.s19.group5.mco.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView.Adapter
import android.content.Intent
import androidx.activity.ComponentActivity
import com.mobdeve.s19.group5.mco.main.databinding.ItemTasksLayoutBinding
import java.util.ArrayList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TasksAdapter(private val data: ArrayList<Task>, private val activity: MainActivity, private val newTaskResultLauncher: ActivityResultLauncher<Intent>): Adapter<TasksViewHolder>() {

    private lateinit var  myDbHelper: MyDbHelper
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    @SuppressLint("NotifyDataSetChanged")
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

        tasksViewHolder.itemView.setOnClickListener {
            val intent: Intent = Intent(tasksViewHolder.itemView.context, ViewTaskActivity::class.java)
            intent.putExtra("TASK_NAME_KEY", data[tasksViewHolder.bindingAdapterPosition].taskName)
            intent.putExtra("TASK_DESC_KEY", data[tasksViewHolder.bindingAdapterPosition].taskDescription)
            intent.putExtra("TASK_DEADLINE_KEY", data[tasksViewHolder.bindingAdapterPosition].deadlineDate)
            intent.putExtra("TASK_STATUS_KEY", data[tasksViewHolder.bindingAdapterPosition].taskStatus)
            intent.putExtra("TASK_ID_KEY", data[tasksViewHolder.bindingAdapterPosition].id)
            intent.putExtra("VIEW_HOLDER_POSITION_KEY", tasksViewHolder.bindingAdapterPosition)

            tasksViewHolder.itemView.context.startActivity(intent)
        }

        tasksViewHolder.setEditBtnOnClickListener {
            val editIntent = Intent(tasksViewHolder.itemView.context, AddTaskActivity::class.java)
            editIntent.putExtra("TASK_NAME_KEY", data[tasksViewHolder.bindingAdapterPosition].taskName)
            editIntent.putExtra("TASK_DESC_KEY", data[tasksViewHolder.bindingAdapterPosition].taskDescription)
            editIntent.putExtra("TASK_DEADLINE_KEY", data[tasksViewHolder.bindingAdapterPosition].deadlineDate)
            editIntent.putExtra("TASK_ID_KEY", data[tasksViewHolder.bindingAdapterPosition].id)
            editIntent.putExtra("VIEW_HOLDER_POSITION_KEY", tasksViewHolder.bindingAdapterPosition)
            this.newTaskResultLauncher.launch(editIntent)
        }

        tasksViewHolder.setCheckBtnOnClickListener {
            executorService.execute(Runnable {
                myDbHelper = MyDbHelper.getInstance(activity)
                val task = data[tasksViewHolder.bindingAdapterPosition]
                task.taskStatus = "Done"
                myDbHelper.deleteTask(task)
                activity.runOnUiThread {
                    Toast.makeText(activity, "Task Completed", Toast.LENGTH_SHORT).show()
                    data.removeAt(tasksViewHolder.bindingAdapterPosition)
                    notifyDataSetChanged()
                }
            })
        }
        return tasksViewHolder
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }


}