package ch.hslu.kanbanboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.hslu.kanbanboard.ServerStatusRepository
import ch.hslu.kanbanboard.TaskSDK
import ch.hslu.kanbanboard.model.SyncMessage
import ch.hslu.kanbanboard.network.TaskApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SyncViewModel(
    private val api: TaskApi,
    private val sdk: TaskSDK,
    private val serverStatusRepo: ServerStatusRepository
) : ViewModel() {

    val isServerOnline: StateFlow<Boolean> = serverStatusRepo.isServerOnline
    private val _syncMessage = MutableStateFlow(
        SyncMessage("", isPositive = false, priority = 0)
    )
    val syncMessage: StateFlow<SyncMessage> = _syncMessage
    private var clearMessageJob: kotlinx.coroutines.Job? = null

    init {
        checkServerLoop()
    }

    private fun checkServerLoop() {
        viewModelScope.launch {
            while (true) {
                checkServerStatus()
                checkSync()
                delay(8000)
            }
        }
    }

    private fun checkServerStatus() {
        viewModelScope.launch {
            val online = api.isServerOnline()
            serverStatusRepo.updateServerStatus(online)
        }
    }

    private fun checkSync() {
        viewModelScope.launch {
            serverStatusRepo.isServerOnline.collect { online ->
                if (online && !sdk.isInSync()) {
                    setSyncMessage(
                        "Aufgaben nicht synchron oder leer",
                        positive = false,
                        priority = 1
                    )
                }
            }
        }
    }

    fun setSyncMessage(message: String, positive: Boolean, priority: Int = 2) {
        viewModelScope.launch {
            if(isServerOnline.value){
                if (priority < _syncMessage.value.priority) {
                    return@launch
                }
                clearMessageJob?.cancel()

                _syncMessage.value = SyncMessage(message, positive, priority)

                clearMessageJob = viewModelScope.launch {
                    delay(8000)
                    _syncMessage.value = SyncMessage("", true, priority = 0)
                }
            }
        }
    }

}