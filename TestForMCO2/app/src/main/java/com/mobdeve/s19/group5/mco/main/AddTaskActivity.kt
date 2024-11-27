package com.mobdeve.s19.group5.mco.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import com.mobdeve.s19.group5.mco.main.databinding.ActivityAddTasksBinding
import org.w3c.dom.Text
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AddTaskActivity : ComponentActivity() {

    private lateinit var submitBtn: Button
    private lateinit var taskTitleEv: EditText
    private lateinit var deadlineEv: EditText
    private lateinit var descriptionEv: EditText
    private lateinit var headerTv: TextView

    private lateinit var myDbHelper: MyDbHelper
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    private var tempTask: Task? = null
    private var viewHolderPosition: Int = -1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityAddTasksBinding = ActivityAddTasksBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        this.submitBtn = viewBinding.submitBtn
        this.taskTitleEv = viewBinding.taskTitleEv
        this.deadlineEv = viewBinding.deadlineDateEv
        this.descriptionEv = viewBinding.textDescriptionEv
        this.headerTv = viewBinding.headerTv

        val intent = intent
        Log.d("Id", "Id: ${intent.getLongExtra("TASK_ID_KEY", -1)}")
        if(intent.getLongExtra("TASK_ID_KEY", -1) != -1L) {
            this.tempTask = Task(
                intent.getStringExtra("TASK_NAME_KEY").toString() ?: "",
                "User1" ?: "",
                intent.getStringExtra("TASK_DESC_KEY").toString() ?: "",
                "Pending" ?: "",
                intent.getStringExtra("TASK_CREATED_AT_KEY").toString() ?: "",
                intent.getStringExtra("TASK_DEADLINE_KEY").toString() ?: "",
                intent.getLongExtra("TASK_ID_KEY", -1)
            )
            Log.d("TempTask", "Task: $tempTask")

            taskTitleEv.setText(tempTask?.taskName)
            deadlineEv.setText(tempTask?.deadlineDate)
            descriptionEv.setText(tempTask?.taskDescription)

            submitBtn.text = getString(R.string.update)
            headerTv.text = getString(R.string.update_task)
        }

        this.submitBtn.setOnClickListener(View.OnClickListener {
            if(this.taskTitleEv.text.toString().isNotEmpty() && this.deadlineEv.text.toString().isNotEmpty() && this.descriptionEv.text.toString().isNotEmpty()) {
               if(tempTask != null) {
                   executorService.execute(Runnable {
                       myDbHelper = MyDbHelper.getInstance(this)
                       myDbHelper.updateTask(
                            Task(
                                 this.taskTitleEv.text.toString(),
                                 "User1",
                                 this.descriptionEv.text.toString(),
                                 "Pending",
                                //fix dates here
                                 this.deadlineEv.text.toString(),
                                 this.deadlineEv.text.toString(),
                                 tempTask!!.id
                            )
                       )
                       runOnUiThread(Runnable {
                           val editIntent = Intent()
                           editIntent.putExtra("TASK_NAME_KEY", this.taskTitleEv.text.toString())
                           editIntent.putExtra("DEADLINE", this.deadlineEv.text.toString())
                           editIntent.putExtra("TASK_DESC_KEY", this.descriptionEv.text.toString())
                           editIntent.putExtra("TASK_ID_KEY", tempTask?.id)
                           editIntent.putExtra("VIEW_HOLDER_POSITION_KEY", intent.getIntExtra("VIEW_HOLDER_POSITION_KEY", -1))
                           setResult(ResultCodes.EDIT_RESULT.ordinal, editIntent)
                           finish()
                       })
                   })
               } else {
                   executorService.execute(Runnable {
                       myDbHelper = MyDbHelper.getInstance(this)
                       val id = myDbHelper.addTask(
                           Task(
                               this.taskTitleEv.text.toString(),
                               "User1",
                               this.descriptionEv.text.toString(),
                               "Pending",
                               this.deadlineEv.text.toString(),
                               this.deadlineEv.text.toString(),
                           )
                       )
                       runOnUiThread(Runnable {
                           val addIntent = Intent()
                           addIntent.putExtra("TASK_NAME_KEY", this.taskTitleEv.text.toString())
                           addIntent.putExtra("DEADLINE", this.deadlineEv.text.toString())
                           addIntent.putExtra("TASK_DESC_KEY", this.descriptionEv.text.toString())
                           addIntent.putExtra("TASK_ID_KEY", id)
                           setResult(ResultCodes.ADD_RESULT.ordinal, addIntent)
                           finish()
                       })
                   })
               }
            }
            else {
                Toast.makeText(this, "Please make sure all information is provided.", Toast.LENGTH_LONG).show()
            }
        })
    }
}