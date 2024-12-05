package com.mobdeve.s19.group5.mco.main

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddFlashcardActivity : AppCompatActivity() {

    private lateinit var questionEditText: EditText
    private lateinit var answerEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var dbHelper: MyDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_flashcard_activity)

        // Initialize views
        questionEditText = findViewById(R.id.flashcardQuestionEditText)
        answerEditText = findViewById(R.id.flashcardAnswerEditText)
        saveButton = findViewById(R.id.saveFlashcardButton)
        dbHelper = MyDbHelper.getInstance(this)

        // Handle save button click
        saveButton.setOnClickListener {
            val question = questionEditText.text.toString()
            val answer = answerEditText.text.toString()
            if (question.isNotBlank() && answer.isNotBlank()) {
                val flashcard = Flashcard(question, answer)
                dbHelper.addFlashcard(flashcard)
                finish()  // Close activity and return to FlashcardsActivity
            } else {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}