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
import ch.hslu.kanbanboard.viewmodel.TaskViewModel

enum class ScreenType { KANBAN, ADDTASK}

@Composable
fun Navigation(taskViewModel: TaskViewModel) {
    var currentScreen by rememberSaveable {
        mutableStateOf(ScreenType.KANBAN)
    }

    fun navigateTo(screen: ScreenType) {
        currentScreen = screen
    }

    Scaffold(
        topBar = {
            val screenTitle = when (currentScreen) {
                ScreenType.KANBAN -> "Mein Kanban Board"
                ScreenType.ADDTASK -> "Aufgabe hinzufügen"
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
                paddingValues = paddingValues
            )

            ScreenType.ADDTASK -> AddTaskScreen(
                taskViewModel = taskViewModel,
                paddingValues = paddingValues
            )

        }
    }
}