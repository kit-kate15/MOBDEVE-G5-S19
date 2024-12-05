package com.mobdeve.s19.group5.mco.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s19.group5.mco.main.databinding.ActivityCalendarViewBinding

class CalendarViewActivity : ComponentActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskAdapter: TasksAdapter
    private lateinit var dbHelper: MyDbHelper
    private lateinit var data: ArrayList<Task>

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityCalendarViewBinding = ActivityCalendarViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize views
        calendarView = binding.calendarView
        taskRecyclerView = binding.taskListRv

        // Load tasks from database
        dbHelper = MyDbHelper.getInstance(this)
        data = dbHelper.getAllTasks("User1")

        // Set up RecyclerView
        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TasksAdapter(data, this, newTaskResultLauncher)
        taskRecyclerView.adapter = taskAdapter

        // Handle calendar date changes
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            var monthString = ""
            when(month) {
                0 -> monthString = "January"
                1 -> monthString = "February"
                2 -> monthString = "March"
                3 -> monthString = "April"
                4 -> monthString = "May"
                5 -> monthString = "June"
                6 -> monthString = "July"
                7 -> monthString = "August"
                8 -> monthString = "September"
                9 -> monthString = "October"
                10 -> monthString = "November"
                11 -> monthString = "December"
            }
            val selectedDate = "$year-$monthString-$dayOfMonth"
            Log.d("SelectedDate", selectedDate)
            showTasksForDate(selectedDate)
        }

        // Show all tasks initially
        showTasksForDate(null)
    }

    private fun showTasksForDate(date: String?) {
        val filteredTasks = if (date == null) {
            data // Show all tasks if no specific date
        } else {
            data.filter { task ->
                val taskDate = "${task.deadlineYear}-${task.deadlineMonth}-${task.deadlineDay}"
                Log.d("TaskDate", taskDate)
                taskDate == date
            }
        }

        if (filteredTasks.isEmpty()) {
            Toast.makeText(this, "No tasks for this date", Toast.LENGTH_SHORT).show()
        }

        // Update the RecyclerView
        taskAdapter = TasksAdapter(ArrayList(filteredTasks), this, newTaskResultLauncher)
        taskRecyclerView.adapter = taskAdapter
    }
}
