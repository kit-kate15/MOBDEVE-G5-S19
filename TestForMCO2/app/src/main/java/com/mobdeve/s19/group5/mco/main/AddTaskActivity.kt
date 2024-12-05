package com.mobdeve.s19.group5.mco.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.mobdeve.s19.group5.mco.main.databinding.ActivityAddTasksBinding
import org.w3c.dom.Text
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AddTaskActivity : ComponentActivity() {

    private lateinit var submitBtn: Button
    private lateinit var taskTitleEv: EditText
    private lateinit var deadlineYearSp: Spinner
    private lateinit var deadlineMonthSp: Spinner
    private lateinit var deadlineDaySp: Spinner
    private lateinit var descriptionEv: EditText
    private lateinit var headerTv: TextView

    private lateinit var myDbHelper: MyDbHelper
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    private lateinit var firebaseDb: FirebaseFirestore
    private lateinit var firebaseId: String

    private var tempTask: Task? = null
    private var viewHolderPosition: Int = -1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityAddTasksBinding = ActivityAddTasksBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)


        this.submitBtn = viewBinding.submitBtn
        this.taskTitleEv = viewBinding.taskTitleEv
        this.deadlineYearSp = viewBinding.deadlineYearSp
        this.descriptionEv = viewBinding.textDescriptionEv
        this.headerTv = viewBinding.headerTv
        this.deadlineYearSp = viewBinding.deadlineYearSp
        this.deadlineMonthSp = viewBinding.deadlineMonthSp
        this.deadlineDaySp = viewBinding.deadlineDaySp

        firebaseDb = Firebase.firestore

        val intent = intent
        Log.d("Id", "Id: ${intent.getLongExtra("TASK_ID_KEY", -1)}")
        if(intent.getLongExtra("TASK_ID_KEY", -1) != -1L) {
            this.tempTask = Task(
                intent.getStringExtra("TASK_NAME_KEY").toString() ?: "",
                "User1" ?: "",
                intent.getStringExtra("TASK_DESC_KEY").toString() ?: "",
                "Pending" ?: "",
                intent.getStringExtra("TASK_DEADLINE_YEAR_KEY").toString() ?: "",
                intent.getStringExtra("TASK_DEADLINE_MONTH_KEY").toString() ?: "",
                intent.getStringExtra("TASK_DEADLINE_DAY_KEY").toString() ?: "",
                intent.getLongExtra("TASK_ID_KEY", -1)
            )
            Log.d("TempTask", "Task: $tempTask")

            val yearSelection = toIntYear(tempTask?.deadlineYear ?: "2024")
            val monthSelection = toIntMonth(tempTask?.deadlineMonth ?: "January")
            val daySelection = toIntDay(tempTask?.deadlineDay ?: "1")

            taskTitleEv.setText(tempTask?.taskName)

            deadlineYearSp.setSelection(yearSelection)
            deadlineMonthSp.setSelection(monthSelection)
            deadlineDaySp.setSelection(daySelection)
            descriptionEv.setText(tempTask?.taskDescription)

            submitBtn.text = getString(R.string.update)
            headerTv.text = getString(R.string.update_task)
        }

        this.submitBtn.setOnClickListener(View.OnClickListener {
            if(this.taskTitleEv.text.toString().isNotEmpty() && this.descriptionEv.text.toString().isNotEmpty()) {
               if(tempTask != null) {
                   executorService.execute(Runnable {
                       myDbHelper = MyDbHelper.getInstance(this)
                       val tempTask = Task(
                           this.taskTitleEv.text.toString(),
                           "User1",
                           this.descriptionEv.text.toString(),
                           "Pending",
                           //fix dates here
                           this.deadlineYearSp.selectedItem.toString(),
                           this.deadlineMonthSp.selectedItem.toString(),
                           this.deadlineDaySp.selectedItem.toString(),
                           tempTask!!.id
                       )
                       val taskHash = hashMapOf(
                            "taskName" to tempTask.taskName,
                            "user" to tempTask.user,
                            "taskDescription" to tempTask.taskDescription,
                            "taskStatus" to tempTask.taskStatus,
                            "deadlineYear" to tempTask.deadlineYear,
                            "deadlineMonth" to tempTask.deadlineMonth,
                            "deadlineDay" to tempTask.deadlineDay
                       )
                       myDbHelper.updateTask(tempTask)
                       firebaseDb.collection("Tasks").document(tempTask.id.toString()).set(taskHash, SetOptions.merge())
                           .addOnSuccessListener { Log.d("AddTaskActivity", "DocumentSnapshot successfully written!") }
                           .addOnFailureListener { e -> Log.w("AddTaskActivity", "Error writing document", e) }
                       runOnUiThread(Runnable {
                           val editIntent = Intent()
                           editIntent.putExtra("TASK_NAME_KEY", this.taskTitleEv.text.toString())
                           editIntent.putExtra("TASK_DEADLINE_YEAR_KEY", this.deadlineYearSp.selectedItem.toString())
                           editIntent.putExtra("TASK_DEADLINE_MONTH_KEY", this.deadlineMonthSp.selectedItem.toString())
                           editIntent.putExtra("TASK_DEADLINE_DAY_KEY", this.deadlineDaySp.selectedItem.toString())
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
                       val tempTask = Task(
                           this.taskTitleEv.text.toString(),
                           "User1",
                           this.descriptionEv.text.toString(),
                           "Pending",
                           this.deadlineYearSp.selectedItem.toString(),
                           this.deadlineMonthSp.selectedItem.toString(),
                           this.deadlineDaySp.selectedItem.toString()
                       )
                       val taskHash = hashMapOf(
                           "taskName" to tempTask.taskName,
                           "user" to tempTask.user,
                           "taskDescription" to tempTask.taskDescription,
                           "taskStatus" to tempTask.taskStatus,
                           "deadlineYear" to tempTask.deadlineYear,
                           "deadlineMonth" to tempTask.deadlineMonth,
                           "deadlineDay" to tempTask.deadlineDay
                       )
                       Log.d("AddTaskActivity", "Attempting to add to firebase...");
                       val id = myDbHelper.addTask(tempTask)
                       firebaseDb.collection("Tasks").document(id.toString()).set(tempTask)
                           .addOnSuccessListener {
                               Log.d("AddTaskActivity", "DocumentSnapshot written with ID: $id")
                           }
                           .addOnFailureListener() { e -> Log.w("AddTaskActivity", "Error adding document", e) }
                       runOnUiThread(Runnable {
                           val addIntent = Intent()
                           addIntent.putExtra("TASK_NAME_KEY", this.taskTitleEv.text.toString())
                           addIntent.putExtra("TASK_DEADLINE_YEAR_KEY", this.deadlineYearSp.selectedItem.toString())
                           addIntent.putExtra("TASK_DEADLINE_MONTH_KEY", this.deadlineMonthSp.selectedItem.toString())
                           addIntent.putExtra("TASK_DEADLINE_DAY_KEY", this.deadlineDaySp.selectedItem.toString())
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

    fun toIntYear(year: String): Int {
        return when(year) {
            "2024" -> 0
            "2025" -> 1
            "2026" -> 2
            "2027" -> 3
            "2028" -> 4
            "2029" -> 5
            "2030" -> 6
            else -> 0
        }
    }

    fun toIntMonth(month: String): Int {
        return when(month) {
            "January" -> 0
            "February" -> 1
            "March" -> 2
            "April" -> 3
            "May" -> 4
            "June" -> 5
            "July" -> 6
            "August" -> 7
            "September" -> 8
            "October" -> 9
            "November" -> 10
            "December" -> 11
            else -> 0
        }
    }

    fun toIntDay(day: String): Int {
        return when(day) {
            "1" -> 0
            "2" -> 1
            "3" -> 2
            "4" -> 3
            "5" -> 4
            "6" -> 5
            "7" -> 6
            "8" -> 7
            "9" -> 8
            "10" -> 9
            "11" -> 10
            "12" -> 11
            "13" -> 12
            "14" -> 13
            "15" -> 14
            "16" -> 15
            "17" -> 16
            "18" -> 17
            "19" -> 18
            "20" -> 19
            "21" -> 20
            "22" -> 21
            "23" -> 22
            "24" -> 23
            "25" -> 24
            "26" -> 25
            "27" -> 26
            "28" -> 27
            "29" -> 28
            "30" -> 29
            "31" -> 30
            else -> 0
        }
    }
}