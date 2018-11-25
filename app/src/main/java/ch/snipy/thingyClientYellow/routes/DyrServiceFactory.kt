package ch.snipy.thingyClientYellow.routes

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

interface DyrServiceFactory {
    val baseUrl: String
        get() = "http://134.21.124.156:8080"
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