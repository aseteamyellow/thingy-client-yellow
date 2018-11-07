package ch.snipy.thingyClientYellow.routes

interface DyrServiceFactory {
    val baseUrl: String
        get() = "http://localhost:8080"
}