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
    @SerializedName("id") val id: Id?,
    @SerializedName("pi_camera") val pi_camera: IP?,
    @SerializedName("thingy") val thingy: String,

    @SerializedName("name") val name: String,
    @SerializedName("env_type") val envType: String,
    @SerializedName("animals") val animals: List<Animal>,

    @SerializedName("humidity_min") val humidity_min: Double?,
    @SerializedName("humidity_max") val humidity_max: Double?,
    @SerializedName("temperature_min") val temperature_min: Double?,
    @SerializedName("temperature_max") val temperature_max: Double?,
    @SerializedName("air_quality_min") val air_quality_min: Double?,
    @SerializedName("air_quality_max") val air_quality_max: Double?,
    @SerializedName("air_pressure_min") val air_pressure_min: Double?,
    @SerializedName("air_pressure_max") val air_pressure_max: Double?,
    @SerializedName("light_min") val light_min: Double?,
    @SerializedName("light_max") val light_max: Double?,

    @SerializedName("humidity_notif") val humidityNotification: Boolean,
    @SerializedName("temperature_notif") val temperatureNotification: Boolean,
    @SerializedName("air_quality_notif") val air_qualityNotification: Boolean,
    @SerializedName("air_pressure_notif") val air_pressureNotification: Boolean,
    @SerializedName("light_notif") val lightNotification: Boolean
)

data class Animal(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String
)