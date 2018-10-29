package ch.snipy.thingyClientYellow

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int?,
    @SerializedName("email") val email: String
)

data class Environment(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String
)

data class Animal(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String
)
