package com.mobdeve.s19.group5.mco.main

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s19.group5.mco.main.ui.theme.TestForMCO2Theme
import com.mobdeve.s19.group5.mco.main.databinding.ActivityMainBinding
import java.util.ArrayList

class MainActivity : ComponentActivity() {

    private lateinit var data: ArrayList<Task>

    private lateinit var taskAdapter: TasksAdapter
    private lateinit var recyclerView: RecyclerView

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
        this.taskAdapter = TasksAdapter(data, this)
        this.recyclerView.adapter = this.taskAdapter
        this.recyclerView.layoutManager = LinearLayoutManager(this)
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