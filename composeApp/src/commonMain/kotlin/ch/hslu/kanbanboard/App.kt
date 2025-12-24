package ch.hslu.kanbanboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ch.hslu.kanbanboard.view.task.addTaskScreen.AddTaskScreen
import ch.hslu.kanbanboard.viewmodel.TaskViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import kanbanboard.composeapp.generated.resources.Res
import kanbanboard.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    val taskViewModel = TaskViewModel()
    MaterialTheme {
        AddTaskScreen(taskViewModel)
    }
}


