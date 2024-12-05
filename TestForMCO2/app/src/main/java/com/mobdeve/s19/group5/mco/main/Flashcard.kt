package com.mobdeve.s19.group5.mco.main

data class Flashcard (
    val question: String,
    val answer: String,
    val id: Long = -1,
)