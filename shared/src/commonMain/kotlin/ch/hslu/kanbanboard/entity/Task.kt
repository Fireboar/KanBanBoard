package ch.hslu.kanbanboard.entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    @SerialName("id")
    val id: Long = 0,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String? = "",
    @SerialName("dueDate")
    val dueDate: String,
    @SerialName("dueTime")
    val dueTime: String,
    @SerialName("status")
    val status: String? = "To Do"
)