package ch.hslu.kanbanboard

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform