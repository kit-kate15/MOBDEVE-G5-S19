package com.mobdeve.s19.group5.mco.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FlashcardAdapter(
    private val flashcards: MutableList<Flashcard>,
    private val dbHelper: MyDbHelper
) : RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder>() {

    inner class FlashcardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questionTextView: TextView = itemView.findViewById(R.id.tv_question)
        val answerTextView: TextView = itemView.findViewById(R.id.tv_answer)
        val container: FrameLayout = itemView.findViewById(R.id.flashcard_container)
        val deleteButton: Button = itemView.findViewById(R.id.btn_delete_flashcard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.flashcard_item, parent, false)
        return FlashcardViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlashcardViewHolder, position: Int) {
        val flashcard = flashcards[position]

        holder.questionTextView.text = flashcard.question
        holder.answerTextView.text = flashcard.answer

        var isFlipped = false

        holder.container.setOnClickListener {
            isFlipped = !isFlipped
            holder.questionTextView.visibility = if (isFlipped) View.GONE else View.VISIBLE
            holder.answerTextView.visibility = if (isFlipped) View.VISIBLE else View.GONE
        }

        holder.deleteButton.setOnClickListener {
            dbHelper.deleteFlashcard(flashcard)
            flashcards.removeAt(position)

            notifyItemRemoved(position)
            notifyItemRangeChanged(position, flashcards.size)
        }
    }


    fun updateFlashcards(newFlashcards: List<Flashcard>) {
        flashcards.clear()
        flashcards.addAll(newFlashcards)
        notifyDataSetChanged() // Refresh the RecyclerView
    }

    override fun getItemCount(): Int = flashcards.size


}
