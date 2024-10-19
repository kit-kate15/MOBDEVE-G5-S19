package com.mobdeve.s19.group5.mco.main

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class LoginActivity: ComponentActivity() {
    //get login button
    private lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        loginBtn = findViewById(R.id.loginBtn)

        //set on click listener for login button
        loginBtn.setOnClickListener {
            //start main activity
            finish()
        }
    }
}