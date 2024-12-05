package com.mobdeve.s19.group5.mco.main

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyDbHelper private constructor(context: Context) : SQLiteOpenHelper(context, DbReferences.DATABASE_NAME, null, DbReferences.DATABASE_VERSION) {

    init {
        instance = this
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DbReferences.CREATE_TABLE_STATEMENT)
        db.execSQL(DbReferences.CREATE_FLASHCARDS_TABLE)
        Log.d("DbHelper", "Tables created successfully")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DbReferences.DROP_TABLE_STATEMENT)
        db.execSQL(DbReferences.DROP_FLASHCARDS_TABLE_STATEMENT)
        onCreate(db)
        Log.d("DbHelper", "Database upgraded and tables recreated")

    }

    //get all tasks from user
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
    fun getAllTasks(user: String): ArrayList<Task> {
        val tasks = ArrayList<Task>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM ${DbReferences.TASKS_TABLE_NAME} WHERE ${DbReferences.USER} = '$user'", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(DbReferences.TASK_ID))
                val taskName = cursor.getString(cursor.getColumnIndex(DbReferences.TASK_NAME))
                val taskDescription = cursor.getString(cursor.getColumnIndex(DbReferences.TASK_DESCRIPTION))
                val taskStatus = cursor.getString(cursor.getColumnIndex(DbReferences.TASK_STATUS))
                val taskDeadlineYear = cursor.getString(cursor.getColumnIndex(DbReferences.TASK_DEADLINE_YEAR))
                val taskDeadlineMonth = cursor.getString(cursor.getColumnIndex(DbReferences.TASK_DEADLINE_MONTH))
                val taskDeadlineDay = cursor.getString(cursor.getColumnIndex(DbReferences.TASK_DEADLINE_DAY))
                tasks.add(Task(taskName, user, taskDescription, taskStatus, taskDeadlineYear, taskDeadlineMonth, taskDeadlineDay, id))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return tasks
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Synchronized
    fun addTask(task: Task): Long {
        val db = writableDatabase
        val values = ContentValues()
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        values.put(DbReferences.TASK_NAME, task.taskName)
        values.put(DbReferences.USER, task.user)
        values.put(DbReferences.TASK_DESCRIPTION, task.taskDescription)
        values.put(DbReferences.TASK_STATUS, task.taskStatus)
        values.put(DbReferences.TASK_DEADLINE_YEAR, task.deadlineYear)
        values.put(DbReferences.TASK_DEADLINE_MONTH, task.deadlineMonth)
        values.put(DbReferences.TASK_DEADLINE_DAY, task.deadlineDay)
        val id = db.insert(DbReferences.TASKS_TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun updateTask(task: Task): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(DbReferences.TASK_NAME, task.taskName)
        values.put(DbReferences.USER, task.user)
        values.put(DbReferences.TASK_DESCRIPTION, task.taskDescription)
        values.put(DbReferences.TASK_STATUS, task.taskStatus)
        values.put(DbReferences.TASK_DEADLINE_YEAR, task.deadlineYear)
        values.put(DbReferences.TASK_DEADLINE_MONTH, task.deadlineMonth)
        values.put(DbReferences.TASK_DEADLINE_DAY, task.deadlineDay)
        val updatedRows = db.update(DbReferences.TASKS_TABLE_NAME, values, "${DbReferences.TASK_ID} = ?", arrayOf(task.id.toString()))
        db.close()
        return updatedRows
    }

    fun deleteTask(task: Task): Int {
        val db = writableDatabase
        val deletedRows = db.delete(DbReferences.TASKS_TABLE_NAME, "${DbReferences.TASK_ID} = ?", arrayOf(task.id.toString()))
        db.close()
        return deletedRows
    }

    // Flashcard CRUD Functions
    // add flashcard
    fun addFlashcard(flashcard: Flashcard): Long {
        val db = writableDatabase
        val values = ContentValues().apply{
            put(DbReferences.FLASHCARD_QUESTION, flashcard.question)
            put(DbReferences.FLASHCARD_ANSWER, flashcard.answer)
        }
        val id = db.insert(DbReferences.FLASHCARDS_TASKS_TABLE_NAME, null, values)
        db.close()
        return id
    }

    // get all flashcards
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
    fun getAllFlashcards(): ArrayList<Flashcard> {
        val flashcards = ArrayList<Flashcard>()
        val db = readableDatabase
        val cursor: Cursor =
            db.rawQuery("SELECT * FROM ${DbReferences.FLASHCARDS_TASKS_TABLE_NAME}", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(DbReferences.FLASHCARD_ID))
                val question =
                    cursor.getString(cursor.getColumnIndex(DbReferences.FLASHCARD_QUESTION))
                val answer = cursor.getString(cursor.getColumnIndex(DbReferences.FLASHCARD_ANSWER))
                flashcards.add(Flashcard(question, answer, id))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return flashcards
    }

    // update flashcards
    fun updateFlashcard(flashcard: Flashcard): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(DbReferences.FLASHCARD_QUESTION, flashcard.question)
        values.put(DbReferences.FLASHCARD_ANSWER, flashcard.answer)
        val updatedRows = db.update(DbReferences.FLASHCARDS_TASKS_TABLE_NAME, values, "${DbReferences.FLASHCARD_ID} = ?", arrayOf(flashcard.id.toString()))
        db.close()
        return updatedRows
    }


    // delete flashcards
    fun deleteFlashcard(flashcard: Flashcard): Int {
        val db = writableDatabase
        val deletedRows = db.delete(DbReferences.FLASHCARDS_TASKS_TABLE_NAME, "${DbReferences.FLASHCARD_ID} = ?", arrayOf(flashcard.id.toString()))
        db.close()
        return deletedRows
    }

    private object DbReferences {
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "my_database.db"

        const val TASKS_TABLE_NAME = "tasks"
        const val TASK_ID = "id"
        const val TASK_NAME = "task_name"
        const val USER = "user"
        const val TASK_DESCRIPTION = "task_description"
        const val TASK_STATUS = "task_status"
        const val TASK_DEADLINE_YEAR = "task_deadline_year"
        const val TASK_DEADLINE_MONTH = "task_deadline_month"
        const val TASK_DEADLINE_DAY = "task_deadline_day"

        // Flashcards Table
        const val FLASHCARDS_TASKS_TABLE_NAME = "flashcards"
        const val FLASHCARD_ID = "id"
        const val FLASHCARD_QUESTION = "question"
        const val FLASHCARD_ANSWER = "answer"
        const val FLASHCARD_IS_KNOWN = "isKnown"


        const val CREATE_TABLE_STATEMENT = """
            CREATE TABLE $TASKS_TABLE_NAME (
                $TASK_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $TASK_NAME TEXT,
                $USER TEXT,
                $TASK_DESCRIPTION TEXT,
                $TASK_STATUS TEXT,
                $TASK_DEADLINE_YEAR TEXT,
                $TASK_DEADLINE_MONTH TEXT,
                $TASK_DEADLINE_DAY TEXT
            )
        """

        const val CREATE_FLASHCARDS_TABLE = """
        CREATE TABLE $FLASHCARDS_TASKS_TABLE_NAME (
            $FLASHCARD_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $FLASHCARD_QUESTION TEXT,
            $FLASHCARD_ANSWER TEXT,
        )
    """

        const val DROP_TABLE_STATEMENT = "DROP TABLE IF EXISTS $TASKS_TABLE_NAME"
        const val DROP_FLASHCARDS_TABLE_STATEMENT = "DROP TABLE IF EXISTS $FLASHCARDS_TASKS_TABLE_NAME"
    }

    companion object {
        @Volatile
        private var instance: MyDbHelper? = null

        fun getInstance(context: Context): MyDbHelper {
            return instance ?: synchronized(this) {
                instance ?: MyDbHelper(context).also { instance = it }
            }
        }
    }
}