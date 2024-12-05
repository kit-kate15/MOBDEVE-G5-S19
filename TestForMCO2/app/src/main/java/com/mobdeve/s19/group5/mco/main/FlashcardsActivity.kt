package com.mobdeve.s19.group5.mco.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FlashcardsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: Button
    private lateinit var dbHelper: MyDbHelper
    private lateinit var adapter: FlashcardAdapter
    private var flashcards: MutableList<Flashcard> = mutableListOf() // All flashcards list

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcards)

        // Initialize components
        recyclerView = findViewById(R.id.flashcardRecyclerView)
        addButton = findViewById(R.id.addFlashcardButton)
        dbHelper = MyDbHelper.getInstance(this)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadFlashcards()

        val unknownFlashcards = flashcards.filter { !it.isKnown }
        adapter = FlashcardAdapter(unknownFlashcards.toMutableList(), dbHelper) // Use only unknown ones
        recyclerView.adapter = adapter

        // Handle Add Flashcard button click
        addButton.setOnClickListener {
            val intent = Intent(this, AddFlashcardActivity::class.java)  // Launch an activity to add a flashcard
            startActivity(intent)
        }
    }

    // Load flashcards and set the adapter
    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadFlashcards() {
        flashcards = dbHelper.getAllFlashcards().toMutableList()
    }
}
