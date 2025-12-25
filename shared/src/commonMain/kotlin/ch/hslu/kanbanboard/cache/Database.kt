package ch.hslu.kanbanboard.cache

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOne
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import app.cash.sqldelight.db.SqlDriver
import ch.hslu.kanbanboard.entity.Task

class Database (val driver: SqlDriver){

    private val database = AppDatabase(driver)
    private val dbQuery get() = database.appDatabaseQueries

    // Mapping
    internal fun mapTask (
        id: Long,
        title: String,
        description: String?,
        dueDate: String,
        dueTime: String,
        status: String?
    ): Task {
        return Task(
            id = id,
            title = title,
            description = description ?: "",
            dueDate = dueDate,
            dueTime = dueTime,
            status = status ?: "To Do"
        )
    }

    // Read
    internal suspend fun getTasks(): List<Task> {
        return dbQuery.selectAllTasks(::mapTask).awaitAsList()
    }

    // Single Read
    internal suspend fun getTaskById(id: Long): Task? {
        return dbQuery
            .selectTaskById(id, ::mapTask)
            .awaitAsOneOrNull()
    }

    internal suspend fun insertTask(task: Task): Task {
        return dbQuery.transactionWithResult {
            dbQuery.insertTask(
                title = task.title,
                description = task.description,
                dueDate = task.dueDate,
                dueTime = task.dueTime,
                status = task.status
            )

            val newId = dbQuery.lastInsertRowId().awaitAsOne()

            dbQuery.selectTaskById(newId, ::mapTask).awaitAsOne()
        }
    }

    internal suspend fun updateTask(task: Task) {
        dbQuery.updateTask(
            id = task.id,
            title = task.title,
            description = task.description,
            dueDate = task.dueDate,
            dueTime = task.dueTime,
            status = task.status
        )
    }

    internal suspend fun deleteTask(task: Task) {
        dbQuery.deleteTaskById(task.id)
    }


    internal suspend fun replaceTasks(tasks: List<Task>) {
        dbQuery.deleteAllTasks()
        dbQuery.transaction {
            tasks.forEach { task ->
                dbQuery.insertOrReplaceTask(
                    id = task.id,
                    title = task.title,
                    description = task.description,
                    dueDate = task.dueDate,
                    dueTime = task.dueTime,
                    status = task.status
                )
            }
        }
    }



}


