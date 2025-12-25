package ch.hslu.kanbanboard.network

import ch.hslu.kanbanboard.SERVER_IP
import ch.hslu.kanbanboard.entity.Task
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class TaskApi() {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    suspend fun getTasks(): List<Task> {
        return httpClient.get(SERVER_IP).body()
    }
}

