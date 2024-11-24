package com.mobdeve.s19.group5.mco.main

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat

class PomodoroActivity : ComponentActivity() {

    private lateinit var tvTimer: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnStartPause: Button
    private lateinit var btnReset: Button

    private var timer: CountDownTimer? = null
    private var isRunning = false
    private var isFocusMode = true
    private var timeLeftInMillis = 25 * 60 * 1000L
    private val totalFocusTimeInMillis = 25 * 60 * 1000L
    private val totalBreakTimeInMillis = 5 * 60 * 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pomodoro)
        Log.d("PomodoroActivity", "onCreate called")

        tvTimer = findViewById(R.id.tv_timer)
        progressBar = findViewById(R.id.progress_bar)
        btnStartPause = findViewById(R.id.btn_start_pause)
        btnReset = findViewById(R.id.btn_reset)

        btnStartPause.setOnClickListener {
            if (isRunning) pauseTimer() else startTimer()
        }

        btnReset.setOnClickListener { resetTimer() }

        updateTimerText()
        updateProgressBar()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText()
                updateProgressBar()
            }

            override fun onFinish() {
                isRunning = false
                updateTimerText()
                updateProgressBar()
                playAlarmSound()

                val message = if (isFocusMode) {
                    "Focus mode is complete! It's time for a 5-minute break."
                } else {
                    "Break time is over! It's time to concentrate for the next 25 minutes."
                }

                showDialog(message,
                    onOkClicked = {
                        isFocusMode = !isFocusMode
                        resetTimer()
                        startTimer()
                    },
                    onCancelClicked = {
                        resetTimer()
                    }
                )
            }
        }.start()

        isRunning = true
        btnStartPause.text = "Pause"
        btnStartPause.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.holo_red_dark)
    }

    private fun pauseTimer() {
        timer?.cancel()
        isRunning = false
        btnStartPause.text = "Start"
        btnStartPause.backgroundTintList = ContextCompat.getColorStateList(this, R.color.green_button)
    }

    private fun resetTimer() {
        timer?.cancel()
        timeLeftInMillis = if (isFocusMode) totalFocusTimeInMillis else totalBreakTimeInMillis
        isRunning = false
        updateTimerText()
        updateProgressBar()
        btnStartPause.text = "Start"
        btnStartPause.backgroundTintList = ContextCompat.getColorStateList(this, R.color.green_button)
    }

    private fun updateTimerText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        tvTimer.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun updateProgressBar() {
        val totalTime = if (isFocusMode) totalFocusTimeInMillis else totalBreakTimeInMillis
        val progress = (timeLeftInMillis.toFloat() / totalTime * 100).toInt()
        progressBar.progress = progress
    }

    private fun showDialog(message: String, onOkClicked: () -> Unit, onCancelClicked: () -> Unit) {
        Log.d("PomodoroActivity", "showDialog: $message")
        AlertDialog.Builder(this)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ -> onOkClicked() }
            .setNegativeButton("Cancel") { _, _ -> onCancelClicked() }
            .create()
            .show()
    }

    private fun playAlarmSound() {
        MediaPlayer.create(this, android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI)?.apply {
            start()
            setOnCompletionListener { release() }
        }
    }
}


