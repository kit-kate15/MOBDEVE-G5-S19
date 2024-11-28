package com.mobdeve.s19.group5.mco.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.mobdeve.s19.group5.mco.main.ui.theme.TestForMCO2Theme
import com.mobdeve.s19.group5.mco.main.databinding.ActivityMainBinding
import java.util.ArrayList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {

    private lateinit var data: ArrayList<Task>

    private lateinit var taskAdapter: TasksAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var addTaskBtn: Button

    @SuppressLint("NotifyDataSetChanged")
    private val newTaskResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if(data != null) {
            val data = result.data
            if(data != null) {
                val tempTask = Task(
                    data.getStringExtra("TASK_NAME_KEY").toString(),
                    //data.getStringExtra("TASK_USER_KEY").toString(),
                    "User1",
                    data.getStringExtra("TASK_DESC_KEY").toString(),
                    "Pending",
                    data.getStringExtra("TASK_DEADLINE_YEAR_KEY").toString(),
                    data.getStringExtra("TASK_DEADLINE_MONTH_KEY").toString(),
                    data.getStringExtra("TASK_DEADLINE_DAY_KEY").toString(),
                    data.getLongExtra("TASK_ID_KEY", -1)
                )
                Log.d("ResultsFromAddTask", "Task: $tempTask")
                Log.d("ResultsFromAddTask", "ResultCode: ${result.resultCode}")
                when(result.resultCode) {
                    ResultCodes.ADD_RESULT.ordinal -> {
                        this.data.add(0, tempTask)
                        this.taskAdapter.notifyDataSetChanged()
                    }
                    ResultCodes.EDIT_RESULT.ordinal -> {
                        val position = data.getIntExtra("VIEW_HOLDER_POSITION_KEY", -1)
                        if(position != -1) {
                            this.data[position] = tempTask
                            this.taskAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    private lateinit var  myDbHelper: MyDbHelper
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        data = DataHelper.loadTaskData()
        val viewBinding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root);

        val intent = Intent(applicationContext, LoginActivity::class.java)
        this.startActivity(intent)

        val pomodoroButton = findViewById<ImageButton>(R.id.pomodoroBtn)
        pomodoroButton.setOnClickListener {
            val pomIntent = Intent(applicationContext, PomodoroActivity::class.java)
            startActivity(pomIntent)
        }

        this.recyclerView = viewBinding.tasksRv;
        this.recyclerView.layoutManager = LinearLayoutManager(this)

        executorService.execute(Runnable {
            myDbHelper = MyDbHelper.getInstance(this)
            data = myDbHelper.getAllTasks("User1")

            runOnUiThread(Runnable {
                taskAdapter = TasksAdapter(data, this, newTaskResultLauncher)
                recyclerView.adapter = taskAdapter

                this.taskAdapter.notifyDataSetChanged()
            })
        })

        viewBinding.addTasksBtn.setOnClickListener {
            val taskIntent = Intent(this, AddTaskActivity::class.java)
            newTaskResultLauncher.launch(taskIntent)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestForMCO2Theme {
        Greeting("Android")
    }
}