package com.mobdeve.s19.group5.mco.main

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyDbHelper private constructor(context: Context) : SQLiteOpenHelper(context, DbReferences.DATABASE_NAME, null, DbReferences.DATABASE_VERSION) {

    init {
        instance = this
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DbReferences.CREATE_TABLE_STATEMENT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DbReferences.DROP_TABLE_STATEMENT)
        onCreate(db)
    }

    //get all tasks from user
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
    fun getAllTasks(user: String): ArrayList<Task> {
        val tasks = ArrayList<Task>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM ${DbReferences.TABLE_NAME} WHERE ${DbReferences.USER} = '$user'", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(DbReferences._ID))
                val taskName = cursor.getString(cursor.getColumnIndex(DbReferences.TASK_NAME))
                val taskDescription = cursor.getString(cursor.getColumnIndex(DbReferences.TASK_DESCRIPTION))
                val taskStatus = cursor.getString(cursor.getColumnIndex(DbReferences.TASK_STATUS))
                val taskCreatedAt = cursor.getString(cursor.getColumnIndex(DbReferences.TASK_CREATED_AT))
                val deadlineDate = cursor.getString(cursor.getColumnIndex(DbReferences.DEADLINE_DATE))

                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val taskCreatedAtDateTime = LocalDateTime.parse(taskCreatedAt, formatter)
                val deadlineDateTime = LocalDateTime.parse(deadlineDate, formatter)

                val taskCreatedCustomDate = CustomDate(taskCreatedAtDateTime.year, taskCreatedAtDateTime.monthValue, taskCreatedAtDateTime.dayOfMonth)
                val deadlineCustomDate = CustomDate(deadlineDateTime.year, deadlineDateTime.monthValue, deadlineDateTime.dayOfMonth)

                tasks.add(Task(taskName, user, taskDescription, taskStatus, taskCreatedCustomDate, deadlineCustomDate, id))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return tasks
    }

    @Synchronized
    fun addTask(task: Task): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(DbReferences.TASK_NAME, task.taskName)
        values.put(DbReferences.USER, task.user)
        values.put(DbReferences.TASK_DESCRIPTION, task.taskDescription)
        values.put(DbReferences.TASK_STATUS, task.taskStatus)
        values.put(DbReferences.TASK_CREATED_AT, task.taskCreatedAt.databaseFormat())
        values.put(DbReferences.DEADLINE_DATE, task.deadlineDate.databaseFormat())
        val id = db.insert(DbReferences.TABLE_NAME, null, values)
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
        values.put(DbReferences.TASK_CREATED_AT, task.taskCreatedAt.databaseFormat())
        values.put(DbReferences.DEADLINE_DATE, task.deadlineDate.databaseFormat())
        val updatedRows = db.update(DbReferences.TABLE_NAME, values, "${DbReferences._ID} = ?", arrayOf(task.id.toString()))
        db.close()
        return updatedRows
    }

    fun deleteTask(task: Task): Int {
        val db = writableDatabase
        val deletedRows = db.delete(DbReferences.TABLE_NAME, "${DbReferences._ID} = ?", arrayOf(task.id.toString()))
        db.close()
        return deletedRows
    }





    private object DbReferences {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "my_database.db"

        const val TABLE_NAME = "tasks"
        const val _ID = "id"
        const val TASK_NAME = "task_name"
        const val USER = "user"
        const val TASK_DESCRIPTION = "task_description"
        const val TASK_STATUS = "task_status"
        const val TASK_CREATED_AT = "task_created_at"
        const val DEADLINE_DATE = "deadline_date"

        const val CREATE_TABLE_STATEMENT = """
            CREATE TABLE $TABLE_NAME (
                $_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $TASK_NAME TEXT,
                $USER TEXT,
                $TASK_DESCRIPTION TEXT,
                $TASK_STATUS TEXT,
                $TASK_CREATED_AT TEXT,
                $DEADLINE_DATE TEXT
            )
        """

        const val DROP_TABLE_STATEMENT = "DROP TABLE IF EXISTS $TABLE_NAME"
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