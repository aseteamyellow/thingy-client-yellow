package ch.snipy.thingyClientYellow

import com.google.gson.annotations.SerializedName

typealias Token = String
typealias Id = Int
typealias IP = Tuple4<Int, Int, Int, Int>

data class User(
    @SerializedName("token") val token: Token?,
    @SerializedName("id") val id: Id?,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class Environment(
    @SerializedName("id") val id: Id? = null,
    @SerializedName("pi_camera") val piCamera: String = Raspberry.ipString,
    @SerializedName("thingy") val thingy: String = Thingy.name,

    @SerializedName("name") val name: String,
    @SerializedName("env_type") val envType: String,
    @SerializedName("animals") val animals: List<Animal>? = null,

    @SerializedName("humidity_min") val humidity_min: Double? = null,
    @SerializedName("humidity_max") val humidity_max: Double? = null,
    @SerializedName("temperature_min") val temperature_min: Double? = null,
    @SerializedName("temperature_max") val temperature_max: Double? = null,
    @SerializedName("air_quality_min") val air_quality_min: Double? = null,
    @SerializedName("air_quality_max") val air_quality_max: Double? = null,
    @SerializedName("air_pressure_min") val air_pressure_min: Double? = null,
    @SerializedName("air_pressure_max") val air_pressure_max: Double? = null,
    @SerializedName("light_min") val light_min: Double? = null,
    @SerializedName("light_max") val light_max: Double? = null,

    @SerializedName("humidity_notif") val humidityNotification: Int = 0,
    @SerializedName("temperature_notif") val temperatureNotification: Int = 0,
    @SerializedName("air_quality_notif") val air_qualityNotification: Int = 0,
    @SerializedName("air_pressure_notif") val air_pressureNotification: Int = 0,
    @SerializedName("light_notif") val lightNotification: Int = 0
)

data class Animal(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String
)