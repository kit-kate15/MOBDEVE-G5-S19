package com.mobdeve.s19.group5.mco.main

class Task {
    var taskName: String
        private set
    var user: String
        private set
    var taskDescription: String
        private set
    var taskStatus: String
        private set
    var taskCreatedAt: CustomDate
        private set
    var deadlineDate: CustomDate
        private set
    var id: Long
        private set

    constructor(taskName: String, user: String, taskDescription: String, taskStatus: String, taskCreatedAt: CustomDate, deadlineDate: CustomDate) {
        this.taskName = taskName
        this.user = user
        this.taskDescription = taskDescription
        this.taskStatus = taskStatus
        this.taskCreatedAt = taskCreatedAt
        this.deadlineDate = deadlineDate
        this.id = -1
    }

    constructor(taskName: String, user: String, taskDescription: String, taskStatus: String, taskCreatedAt: CustomDate, deadlineDate: CustomDate, id: Long) {
        this.taskName = taskName
        this.user = user
        this.taskDescription = taskDescription
        this.taskStatus = taskStatus
        this.taskCreatedAt = taskCreatedAt
        this.deadlineDate = deadlineDate
        this.id = id
    }

    fun getTaskName(): String {
        return this.taskName
    }

    fun getUser(): String {
        return this.user
    }

    fun getTaskDescription(): String {
        return this.taskDescription
    }

    fun getTaskStatus(): String {
        return this.taskStatus
    }

    fun getTaskCreatedAt(): CustomDate {
        return this.taskCreatedAt
    }

    fun getDeadlineDate(): CustomDate {
        return this.deadlineDate
    }

    fun getId(): Long {
        return this.id
    }


}