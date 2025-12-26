package ch.hslu.kanbanboard

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ch.hslu.kanbanboard.cache.AppDatabase
import ch.hslu.kanbanboard.cache.Database
import ch.hslu.kanbanboard.cache.provideDbDriver
import ch.hslu.kanbanboard.network.TaskApi
import ch.hslu.kanbanboard.view.Navigation
import ch.hslu.kanbanboard.viewmodel.SyncViewModel
import ch.hslu.kanbanboard.viewmodel.TaskViewModel

@Composable
fun App() {
    var taskViewModel by remember { mutableStateOf<TaskViewModel?>(null) }
    var syncViewModel by remember { mutableStateOf<SyncViewModel?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val driver = provideDbDriver(AppDatabase.Schema)
        val database = Database(driver)
        val api = TaskApi()
        val serverStatusRepo = ServerStatusRepository()
        val sdk = TaskSDK(database, api, serverStatusRepo)

        syncViewModel = SyncViewModel(api, sdk, serverStatusRepo)
        taskViewModel = TaskViewModel(sdk, syncViewModel!!)

        isLoading = false
    }

    if (isLoading) {
        Text("Loading…")
    } else {
        taskViewModel?.let {
            MaterialTheme {
                Navigation(
                    taskViewModel = it,
                    syncViewModel = syncViewModel!!
                )
            }
        }
    }
}



