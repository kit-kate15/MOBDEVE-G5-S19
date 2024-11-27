package com.mobdeve.s19.group5.mco.main

class Task {
    var taskName: String
        private set
    var user: String
        private set
    var taskDescription: String
        private set
    var taskStatus: String
    var taskCreatedAt: String
        private set
    var deadlineDate: String
        private set
    var id: Long
        private set

    constructor(taskName: String, user: String, taskDescription: String, taskStatus: String, taskCreatedAt: String, deadlineDate: String) {
        this.taskName = taskName
        this.user = user
        this.taskDescription = taskDescription
        this.taskStatus = taskStatus
        this.taskCreatedAt = taskCreatedAt
        this.deadlineDate = deadlineDate
        this.id = -1
    }

    constructor(taskName: String, user: String, taskDescription: String, taskStatus: String, taskCreatedAt: String, deadlineDate: String, id: Long) {
        this.taskName = taskName
        this.user = user
        this.taskDescription = taskDescription
        this.taskStatus = taskStatus
        this.taskCreatedAt = taskCreatedAt
        this.deadlineDate = deadlineDate
        this.id = id
    }

    override fun toString(): String {
        return "Task(taskName='$taskName', user='$user', taskDescription='$taskDescription', taskStatus='$taskStatus', taskCreatedAt=$taskCreatedAt, deadlineDate=$deadlineDate, id=$id)"
    }

}