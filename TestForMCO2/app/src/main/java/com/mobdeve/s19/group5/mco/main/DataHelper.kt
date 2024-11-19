package com.mobdeve.s19.group5.mco.main

import java.util.ArrayList

object DataHelper {
    fun loadTaskData(): ArrayList<Task> {
        val data = ArrayList<Task>()
        data.add(Task("Task 1", "User1", "Description 1", "Pending", CustomDate(2021, 12, 5), CustomDate(2021, 12, 5)))
        data.add(Task("Task 2", "User1","Description 2", "Pending", CustomDate(2021, 12, 5), CustomDate(2021, 12, 5)))
        data.add(Task("Task 3", "User1","Description 3", "Pending", CustomDate(2021, 12, 5), CustomDate(2021, 12, 5)))
        data.add(Task("Task 4", "User1","Description 4", "Pending", CustomDate(2021, 12, 5), CustomDate(2021, 12, 5)))
        data.add(Task("Task 5", "User1","Description 5", "Pending", CustomDate(2021, 12, 5), CustomDate(2021, 12, 5)))
        data.add(Task("Task 6", "User1","Description 6", "Pending", CustomDate(2021, 12, 5), CustomDate(2021, 12, 5)))
        data.add(Task("Task 7", "User1","Description 7", "Pending", CustomDate(2021, 12, 5), CustomDate(2021, 12, 5)))
        data.add(Task("Task 8", "User1","Description 8", "Pending", CustomDate(2021, 12, 5), CustomDate(2021, 12, 5)))
        data.add(Task("Task 9", "User1","Description 9", "Pending", CustomDate(2021, 12, 5), CustomDate(2021, 12, 5)))
        data.add(Task("Task 10", "User1","Description 10", "Pending", CustomDate(2021, 12, 5), CustomDate(2021, 12, 5)))

        return data
    }
}