package com.mobdeve.s19.group5.mco.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView.Adapter
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s19.group5.mco.main.databinding.ItemTasksLayoutBinding
import java.util.ArrayList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TasksAdapter(private val data: ArrayList<Task>, private val activity: ComponentActivity, private val newTaskResultLauncher: ActivityResultLauncher<Intent>): Adapter<TasksViewHolder>() {

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
            intent.putExtra("TASK_DEADLINE_YEAR_KEY", data[tasksViewHolder.bindingAdapterPosition].deadlineYear)
            intent.putExtra("TASK_DEADLINE_MONTH_KEY", data[tasksViewHolder.bindingAdapterPosition].deadlineMonth)
            intent.putExtra("TASK_DEADLINE_DAY_KEY", data[tasksViewHolder.bindingAdapterPosition].deadlineDay)
            intent.putExtra("TASK_STATUS_KEY", data[tasksViewHolder.bindingAdapterPosition].taskStatus)
            intent.putExtra("TASK_ID_KEY", data[tasksViewHolder.bindingAdapterPosition].id)
            intent.putExtra("VIEW_HOLDER_POSITION_KEY", tasksViewHolder.bindingAdapterPosition)

            tasksViewHolder.itemView.context.startActivity(intent)
        }

        tasksViewHolder.setEditBtnOnClickListener {
            val editIntent = Intent(tasksViewHolder.itemView.context, AddTaskActivity::class.java)
            editIntent.putExtra("TASK_NAME_KEY", data[tasksViewHolder.bindingAdapterPosition].taskName)
            editIntent.putExtra("TASK_DESC_KEY", data[tasksViewHolder.bindingAdapterPosition].taskDescription)
            editIntent.putExtra("TASK_DEADLINE_YEAR_KEY", data[tasksViewHolder.bindingAdapterPosition].deadlineYear)
            editIntent.putExtra("TASK_DEADLINE_MONTH_KEY", data[tasksViewHolder.bindingAdapterPosition].deadlineMonth)
            editIntent.putExtra("TASK_DEADLINE_DAY_KEY", data[tasksViewHolder.bindingAdapterPosition].deadlineDay)
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
            val firebaseDb = FirebaseFirestore.getInstance()

            firebaseDb.collection("Tasks").document(data[tasksViewHolder.bindingAdapterPosition].id.toString())
                .delete()
                .addOnSuccessListener {
                    Log.d("TAG", "DocumentSnapshot successfully deleted!")
                }
                .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }
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