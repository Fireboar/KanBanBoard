package ch.hslu.kanbanboard.model

data class SyncMessage(
    val text: String = "",
    val isPositive: Boolean = true,
    val priority: Int
)