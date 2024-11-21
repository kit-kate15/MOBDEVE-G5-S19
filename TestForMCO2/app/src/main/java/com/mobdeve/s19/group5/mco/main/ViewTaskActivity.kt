package com.mobdeve.s19.group5.mco.main

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import com.mobdeve.s19.group5.mco.main.databinding.ActivityViewTasksBinding



class ViewTaskActivity: ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityViewTasksBinding = ActivityViewTasksBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        window.setLayout((width * .8).toInt(), (height * .7).toInt())

        val params: WindowManager.LayoutParams = window.attributes
        params.gravity = Gravity.CENTER
        params.x = 0
        params.y = -20

        window.attributes = params

        viewBinding.taskHeaderTv.text = intent.getStringExtra("TASK_NAME_KEY")
        viewBinding.descriptionTextTv.text = intent.getStringExtra("TASK_DESC_KEY")
        viewBinding.deadlineTv.text = intent.getStringExtra("TASK_DEADLINE_KEY")

        val position = intent.getIntExtra("VIEW_HOLDER_POSITION_KEY", 0)
    }
}