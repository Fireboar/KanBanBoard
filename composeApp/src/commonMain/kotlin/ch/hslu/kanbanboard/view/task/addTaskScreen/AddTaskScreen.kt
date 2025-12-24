package ch.hslu.kanbanboard.view.task.addTaskScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.hslu.kanbanboard.view.task.taskForm.TaskForm
import ch.hslu.kanbanboard.viewmodel.TaskViewModel

@Composable
fun AddTaskScreen(taskViewModel: TaskViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 480.dp)
        ) {
            TaskForm(
                taskViewModel = taskViewModel,
                buttonText = "Aufgabe hinzufügen",
                onSubmit = { task ->
                    taskViewModel.addTask(
                        title = task.title,
                        description = task.description,
                        dueDate = task.dueDate,
                        dueTime = task.dueTime,
                        status = task.status
                    )
                },
                onNavigateBack = { }
            )
        }
    }
}

