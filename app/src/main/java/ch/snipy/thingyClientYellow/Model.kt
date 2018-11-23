package ch.snipy.thingyClientYellow

import com.google.gson.annotations.SerializedName

typealias Token = String

data class User(
    @SerializedName("token") val token: Token?,
    @SerializedName("id") val id: Int?,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

/* TODO
{
    "id": 1,
    "user_id": 4,
    "name": "example",
    "env_type": "aquarium",
    "humidity": null,
    "temperature": null,
    "air_quality": null,
    "air_pressure": null,
    "light": null,
    "humidity_notif": 0,
    "temperature_notif": 0,
    "air_quality_notif": 0,
    "air_pressure_notif": 0,
    "light_notif": 0,
    "thingy": "",
    "animals": []
}
 */

data class Environment(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String,
    @SerializedName("env_type") val envType: String,
    @SerializedName("animals") val animals: List<Animal>,
    @SerializedName("humidity") val humidity: Double?,
    @SerializedName("temperature") val temperature: Double?,
    @SerializedName("luminosity") val luminosity: Double?,
    @SerializedName("humidity_notif") val humidityNotification: Boolean,
    @SerializedName("temperature_notif") val temperatureNotification: Boolean,
    @SerializedName("luminosity_notif") val luminosityNotification: Boolean
)

data class Animal(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String
)