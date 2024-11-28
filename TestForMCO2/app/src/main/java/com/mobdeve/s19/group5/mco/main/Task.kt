package com.mobdeve.s19.group5.mco.main

class Task {
    var taskName: String
        private set
    var user: String
        private set
    var taskDescription: String
        private set
    var taskStatus: String
    var deadlineYear: String
        private set
    var deadlineMonth: String
        private set
    var deadlineDay: String
        private set
    var id: Long
        private set

    constructor(taskName: String, user: String, taskDescription: String, taskStatus: String, deadlineYear: String, deadlineMonth: String, deadlineDay: String) {
        this.taskName = taskName
        this.user = user
        this.taskDescription = taskDescription
        this.taskStatus = taskStatus
        this.deadlineYear = deadlineYear
        this.deadlineMonth = deadlineMonth
        this.deadlineDay = deadlineDay
        this.id = -1
    }

    constructor(taskName: String, user: String, taskDescription: String, taskStatus: String, deadlineYear: String, deadlineMonth: String, deadlineDay: String, id: Long) {
        this.taskName = taskName
        this.user = user
        this.taskDescription = taskDescription
        this.taskStatus = taskStatus
        this.deadlineYear = deadlineYear
        this.deadlineMonth = deadlineMonth
        this.deadlineDay = deadlineDay
        this.id = id
    }

    override fun toString(): String {
        return "Task(taskName='$taskName', user='$user', taskDescription='$taskDescription', taskStatus='$taskStatus', deadlineYear=$deadlineYear, deadlineMonth=$deadlineMonth, deadlineDay=$deadlineDay, id=$id)"
    }

}