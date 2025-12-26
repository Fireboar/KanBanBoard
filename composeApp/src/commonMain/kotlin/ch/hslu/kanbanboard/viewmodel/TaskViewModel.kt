package ch.hslu.kanbanboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.hslu.kanbanboard.TaskSDK
import ch.hslu.kanbanboard.entity.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel (private val sdk: TaskSDK, private val syncViewModel: SyncViewModel) : ViewModel(){

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
            val success = sdk.addTask(task)
            if (success) {
                syncViewModel.setSyncMessage("'${task.title}' erfolgreich hinzugefügt und synchronisiert.", true)
            } else {
                syncViewModel.setSyncMessage("'${task.title}' konnte nicht auf den Server hochgeladen werden.", false)
            }
            loadTasks()
        }
    }

    private fun loadTasks() {
        viewModelScope.launch {
            val loadedTasks = sdk.getTasks()
            _tasks.value = loadedTasks.toList()
        }
    }

    fun updateTask(task: Task){
        viewModelScope.launch {
            val success = sdk.updateTask(task)
            if(success) {
                syncViewModel.setSyncMessage("'${task.title}' erfolgreich aktualisiert.", true)
            } else {
                syncViewModel.setSyncMessage("'${task.title}' konnte nicht synchronisiert werden.", false)
            }

            loadTasks()
        }
    }

    fun moveTask(task: Task, newStatus: String) {
        viewModelScope.launch {
            val updatedTask = task.copy(status = newStatus)

            val success = sdk.updateTask(updatedTask)
            if(success) {
                syncViewModel.setSyncMessage("'${task.title}' erfolgreich aktualisiert.", true)
            } else {
                syncViewModel.setSyncMessage("'${task.title}' konnte nicht synchronisiert werden.", false)
            }

            loadTasks()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            val success = sdk.deleteTask(task)
            if(success){
                syncViewModel.setSyncMessage("'${task.title}' erfolgreich gelöscht und synchronisiert.", true)
            } else {
                syncViewModel.setSyncMessage("'${task.title}' konnte nicht vom Server gelöscht werden.", false)
            }

            loadTasks()
        }
    }

    fun postTasks() {
        viewModelScope.launch {
            val success = sdk.postTasks()
            if(success){
                syncViewModel.setSyncMessage("Tasks wurden auf den Server gepostet.", true)
            } else {
                syncViewModel.setSyncMessage("Fehler beim Posten der Tasks.", false)
            }
            loadTasks()

        }
    }

    fun pullTasks() {
        viewModelScope.launch {
            val success = sdk.pullTasks()
            if(success){
                syncViewModel.setSyncMessage("Tasks vom Server geladen und lokal synchronisiert.", true)
            } else {
                syncViewModel.setSyncMessage("Fehler beim Laden der Tasks.", false)
            }
            loadTasks()

        }
    }

    fun mergeTasks() {
        viewModelScope.launch {
            val success = sdk.mergeTasks()
            if(success){
                syncViewModel.setSyncMessage("Server- und lokale Tasks wurden zusammengeführt.", true)
            } else {
                syncViewModel.setSyncMessage("Fehler beim Mergen der Tasks.", false)
            }
            loadTasks()

        }
    }


}

