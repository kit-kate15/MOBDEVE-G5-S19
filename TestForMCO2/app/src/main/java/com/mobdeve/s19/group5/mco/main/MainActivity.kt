package com.mobdeve.s19.group5.mco.main

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mobdeve.s19.group5.mco.main.ui.theme.TestForMCO2Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val intent = Intent(applicationContext, LoginActivity::class.java)
        this.startActivity(intent)

        val pomodoroButton = findViewById<ImageButton>(R.id.pomodoroBtn)
        pomodoroButton.setOnClickListener {
            val intent = Intent(this, PomodoroActivity::class.java)
            startActivity(intent)
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