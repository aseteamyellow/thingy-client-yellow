package ch.snipy.thingyClientYellow

object Model {
    data class User(val id: Int?, val email: String)
    data class Environment(val id: Int?, val name: String)
    data class Animal(val id: Int?, val name: String)
}