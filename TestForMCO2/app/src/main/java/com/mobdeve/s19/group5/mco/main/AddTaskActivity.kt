package com.mobdeve.s19.group5.mco.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.mobdeve.s19.group5.mco.main.databinding.ActivityAddTasksBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AddTaskActivity : ComponentActivity() {

    private lateinit var submitBtn: Button
    private lateinit var taskTitleEv: EditText
    private lateinit var deadlineEv: EditText
    private lateinit var descriptionEv: EditText

    private lateinit var myDbHelper: MyDbHelper
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityAddTasksBinding = ActivityAddTasksBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        this.submitBtn = viewBinding.submitBtn
        this.taskTitleEv = viewBinding.taskTitleEv
        this.deadlineEv = viewBinding.deadlineDateEv
        this.descriptionEv = viewBinding.textDescriptionEv

//        this.submitBtn.setOnClickListener(View.OnClickListener {
//            if(this.taskTitleEv.text.toString().isNotEmpty() && this.deadlineEv.text.toString().isNotEmpty() && this.descriptionEv.text.toString().isNotEmpty()) {
//                val intent = Intent()
//                intent.putExtra("TASK_NAME_KEY", this.taskTitleEv.text.toString())
//                intent.putExtra("DEADLINE", this.deadlineEv.text.toString())
//                intent.putExtra("TASK_DESC_KEY", this.descriptionEv.text.toString())
//                setResult(RESULT_OK, intent)
//                finish()
//            }
//            else {
//                Toast.makeText(this, "Please make sure all information is provided.", Toast.LENGTH_LONG).show()
//            }
//        })

        this.submitBtn.setOnClickListener(View.OnClickListener {
            if(this.taskTitleEv.text.toString().isNotEmpty() && this.deadlineEv.text.toString().isNotEmpty() && this.descriptionEv.text.toString().isNotEmpty()) {
                executorService.execute(Runnable {
                    myDbHelper = MyDbHelper.getInstance(this)
                    val id = myDbHelper.addTask(
                        Task(
                            this.taskTitleEv.text.toString(),
                            "User1",
                            this.descriptionEv.text.toString(),
                            "Pending",
                            CustomDate(2022, 1, 1),
                            CustomDate(2022, 1, 1)
                        )
                    )
                    runOnUiThread(Runnable {
                        val intent = Intent()
                        intent.putExtra("TASK_NAME_KEY", this.taskTitleEv.text.toString())
                        intent.putExtra("DEADLINE", this.deadlineEv.text.toString())
                        intent.putExtra("TASK_DESC_KEY", this.descriptionEv.text.toString())
                        intent.putExtra("TASK_ID_KEY", id)
                        setResult(RESULT_OK, intent)
                        finish()
                    })
                })
            }
            else {
                Toast.makeText(this, "Please make sure all information is provided.", Toast.LENGTH_LONG).show()
            }
        })
    }
}