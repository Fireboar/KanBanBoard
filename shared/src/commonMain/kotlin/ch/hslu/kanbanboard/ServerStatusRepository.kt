package ch.hslu.kanbanboard

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ServerStatusRepository {

    private val _isServerOnline = MutableStateFlow(false)
    val isServerOnline: StateFlow<Boolean> = _isServerOnline

    fun updateServerStatus(online: Boolean) {
        _isServerOnline.value = online
    }
}