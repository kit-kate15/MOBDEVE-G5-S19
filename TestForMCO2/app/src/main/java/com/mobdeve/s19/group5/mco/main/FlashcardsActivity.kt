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
    private var flashcards: MutableList<Flashcard> = mutableListOf()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcards)

        // Initialize UI components
        recyclerView = findViewById(R.id.flashcardRecyclerView)
        addButton = findViewById(R.id.addFlashcardButton)
        dbHelper = MyDbHelper.getInstance(this)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.flashcard_spacing)
        recyclerView.addItemDecoration(SpaceItemDecoration(spacingInPixels))

        // Load flashcards from the database
        flashcards = dbHelper.getAllFlashcards()

        // Set up the adapter
        adapter = FlashcardAdapter(flashcards, dbHelper)
        recyclerView.adapter = adapter

        // Handle Add Flashcard button click
        addButton.setOnClickListener {
            val intent = Intent(this, AddFlashcardActivity::class.java)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        refreshFlashcards()
    }

    // Refresh the flashcard list when the activity resumes
    @RequiresApi(Build.VERSION_CODES.O)
    private fun refreshFlashcards() {
        flashcards.clear()
        flashcards.addAll(dbHelper.getAllFlashcards())
        adapter.notifyDataSetChanged() // Notify the adapter of data changes
    }
}
