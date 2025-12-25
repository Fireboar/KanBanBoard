package ch.hslu.kanbanboard

import ch.hslu.kanbanboard.cache.Database
import ch.hslu.kanbanboard.entity.Task
import ch.hslu.kanbanboard.network.TaskApi

class TaskSDK(val database: Database, val api: TaskApi) {

    suspend fun getTasks(): List<Task> {
        return database.getTasks()
    }

    suspend fun addTask(task: Task) : Boolean{
        database.insertTask(task)
        return true;
    }

    suspend fun deleteTask(task: Task) : Boolean{
        database.deleteTask(task)
        return true;
    }

    suspend fun updateTask(task: Task) : Boolean{
        database.updateTask(task)
        return true;
    }
}
