package ch.hslu.kanbanboard.entity

data class Task(
    val id: Long,
    val title: String,
    val description: String,
    val dueDate: String,
    val dueTime: String,
    val status: String = "To Do"
)