package ch.hslu.kanbanboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.hslu.kanbanboard.TaskSDK
import ch.hslu.kanbanboard.entity.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel (private val sdk: TaskSDK) : ViewModel(){

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    init {
        loadTasks()
    }

    fun addTask(title: String, description: String?, dueDate: String, dueTime: String, status: String?) {
        viewModelScope.launch {
            val task = Task(
                id = 0,
                title = title,
                description = description,
                dueDate = dueDate,
                dueTime = dueTime,
                status = status ?: "To Do"
            )
            addTask(task)
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            sdk.addTask(task)
            loadTasks()
        }
    }

    // Folgender Code

    private fun loadTasks() {
        viewModelScope.launch {
            val loadedTasks = sdk.getTasks()
            _tasks.value = loadedTasks.toList()
        }
    }

    fun updateTask(task: Task){
        viewModelScope.launch {
            sdk.updateTask(task)
            loadTasks()
        }
    }

    fun moveTask(task: Task, newStatus: String) {
        viewModelScope.launch {
            val updatedTask = task.copy(status = newStatus)
            sdk.updateTask(updatedTask)
            loadTasks()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            sdk.deleteTask(task)
            loadTasks()
        }
    }

}

