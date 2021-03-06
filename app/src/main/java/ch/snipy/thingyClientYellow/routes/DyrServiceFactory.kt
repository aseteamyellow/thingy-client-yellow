package ch.snipy.thingyClientYellow.routes

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

interface DyrServiceFactory {
    val defaultIpAddress: String
        get() = "192.168.43.34:8080"
    //"163.172.130.246:8080" // VPS @ UniFR
    //"10.0.2.2:8080" // localhost from android emulator
    val client: OkHttpClient
        get() {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            return OkHttpClient().newBuilder()
                .addInterceptor(logging)
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .build()
        }
}