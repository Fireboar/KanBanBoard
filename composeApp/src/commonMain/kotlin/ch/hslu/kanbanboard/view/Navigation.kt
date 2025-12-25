package ch.hslu.kanbanboard.view

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import ch.hslu.kanbanboard.view.bars.BottomNavigationBar
import ch.hslu.kanbanboard.view.bars.TopBar
import ch.hslu.kanbanboard.view.task.addTaskScreen.AddTaskScreen
import ch.hslu.kanbanboard.view.task.kanBanScreen.KanbanScreen
import ch.hslu.kanbanboard.view.task.taskDetailScreen.TaskDetailScreen
import ch.hslu.kanbanboard.viewmodel.TaskViewModel

enum class ScreenType { KANBAN, ADDTASK, TASKDETAIL}

@Composable
fun Navigation(taskViewModel: TaskViewModel) {
    var currentScreen by rememberSaveable {
        mutableStateOf(ScreenType.KANBAN)
    }

    var currentTaskId by rememberSaveable { mutableStateOf<Long?>(null) }

    fun navigateTo(screen: ScreenType, taskId: Long? = null) {
        currentScreen = screen
        currentTaskId = taskId
    }

    Scaffold(
        topBar = {
            val screenTitle = when (currentScreen) {
                ScreenType.KANBAN -> "Mein Kanban Board"
                ScreenType.ADDTASK -> "Aufgabe hinzufügen"
                ScreenType.TASKDETAIL -> "Task Detail"
            }
            if(currentScreen != ScreenType.KANBAN){
                TopBar(screenTitle)
            }
        },
        bottomBar = {
            BottomNavigationBar(
                currentScreen = currentScreen,
                onNavigate = { screen ->
                    navigateTo(screen)
                }
            )
        }
    ) { paddingValues ->
        when (currentScreen) {
            ScreenType.KANBAN -> KanbanScreen(
                taskViewModel = taskViewModel,
                paddingValues = paddingValues,
                onTaskClick = { task -> navigateTo(ScreenType.TASKDETAIL, task.id) }
            )

            ScreenType.ADDTASK -> AddTaskScreen(
                taskViewModel = taskViewModel,
                paddingValues = paddingValues
            )

            ScreenType.TASKDETAIL -> currentTaskId?.let { taskId ->
                TaskDetailScreen(
                    taskId = taskId,
                    taskViewModel = taskViewModel,
                    paddingValues = paddingValues,
                    onNavigateBack = { navigateTo(ScreenType.KANBAN) }
                )
            }


        }
    }
}