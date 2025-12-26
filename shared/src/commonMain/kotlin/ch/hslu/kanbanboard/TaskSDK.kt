package ch.hslu.kanbanboard

import ch.hslu.kanbanboard.cache.Database
import ch.hslu.kanbanboard.entity.Task
import ch.hslu.kanbanboard.network.TaskApi

class TaskSDK(
    val database: Database,
    val api: TaskApi,
    private val serverStatusRepo: ServerStatusRepository
) {
    suspend fun isInSync(): Boolean {
        val serverTasks = api.getTasks()
        if (serverTasks.isEmpty()) return false

        val localTasks = database.getTasks()
        return localTasks == serverTasks
    }

    suspend fun pullTasks(): Boolean {
        if (serverStatusRepo.isServerOnline.value){
            val serverTasks = api.getTasks()
            if(serverTasks.isNotEmpty()){
                database.replaceTasks(serverTasks)
                return true
            } else {
                return false
            }
        }
        return false
    }

    suspend fun postTasks(): Boolean {
        if (serverStatusRepo.isServerOnline.value){
            return api.replaceTasks(database.getTasks())
        }
        return false
    }

    suspend fun mergeTasks(): Boolean {
        if(serverStatusRepo.isServerOnline.value){
            val serverTasks = api.getTasks()
            val localTasks = database.getTasks()

            val mergedTasks = (localTasks + serverTasks)
                .distinctBy { it.id }

            database.replaceTasks(mergedTasks)

            return api.replaceTasks(mergedTasks)
        }
        return false
    }

    suspend fun addTask(task: Task): Boolean {
        val newTask = database.insertTask(task)
        if (serverStatusRepo.isServerOnline.value){
            return api.addTask(newTask)
        }
        return false
    }

    suspend fun getTasks(): List<Task> {
        return database.getTasks()
    }

    suspend fun updateTask(task: Task): Boolean {
        database.updateTask(task)
        if(serverStatusRepo.isServerOnline.value){
            return api.updateTask(task)
        }
        return false
    }

    suspend fun deleteTask(task: Task): Boolean {
        database.deleteTask(task)
        if(serverStatusRepo.isServerOnline.value){
            return api.deleteTask(task.id)
        }
        return false
    }
}
