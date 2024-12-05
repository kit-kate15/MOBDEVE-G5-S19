package com.mobdeve.s19.group5.mco.main

import android.os.Bundle
import android.widget.CalendarView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s19.group5.mco.main.databinding.ActivityCalendarViewBinding

class CalendarViewActivity : ComponentActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskAdapter: TasksAdapter
    private lateinit var taskList: ArrayList<Task>
    private lateinit var dbHelper: MyDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityCalendarViewBinding = ActivityCalendarViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize views
        calendarView = binding.calendarView
        taskRecyclerView = binding.taskListRv

        // Load tasks from database
        dbHelper = MyDbHelper.getInstance(this)
        taskList = dbHelper.getAllTasks("User1")

        // Set up RecyclerView
        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TasksAdapter(taskList, this, null)
        taskRecyclerView.adapter = taskAdapter

        // Handle calendar date changes
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            showTasksForDate(selectedDate)
        }

        // Show all tasks initially
        showTasksForDate(null)
    }

    private fun showTasksForDate(date: String?) {
        val filteredTasks = if (date == null) {
            taskList // Show all tasks if no specific date
        } else {
            taskList.filter { task ->
                val taskDate = "${task.deadlineYear}-${task.deadlineMonth.padStart(2, '0')}-${task.deadlineDay.padStart(2, '0')}"
                taskDate == date
            }
        }

        if (filteredTasks.isEmpty()) {
            Toast.makeText(this, "No tasks for this date", Toast.LENGTH_SHORT).show()
        }

        // Update the RecyclerView
        taskAdapter = TasksAdapter(ArrayList(filteredTasks), this, null)
        taskRecyclerView.adapter = taskAdapter
    }
}
