package ch.snipy.thingyClientYellow.routes

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

interface DyrServiceFactory {
    val baseUrl: String
        get() = "http://134.21.125.236"
    val client: OkHttpClient
        get() = OkHttpClient().newBuilder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(3, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)
            .build()
}