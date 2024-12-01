package com.mobdeve.s19.group5.mco.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FlashcardAdapter(private val flashcards: List<Flashcard>) : RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder>() {

    inner class FlashcardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questionTextView: TextView = view.findViewById(R.id.flashcard_question)
        val answerTextView: TextView = view.findViewById(R.id.flashcard_answer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.flashcard_item, parent, false)
        return FlashcardViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlashcardViewHolder, position: Int) {
        val flashcard = flashcards[position]
        holder.questionTextView.text = flashcard.question
        holder.answerTextView.text = flashcard.answer
    }

    override fun getItemCount(): Int {
        return flashcards.size
    }
}
