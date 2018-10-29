package ch.snipy.thingyClientYellow.routes

import android.os.Environment
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*

interface DyrEnvironmentService {
    // TODO modidy the routes because they are the same...
    @GET("/{userId}")
    fun getEnvironments(@Path("userId") userId: Int): Call<List<Environment>>

    @GET("/{envId}")
    fun getEnvironment(@Path("envId") envId: Int): Call<Environment>

    @POST("/")
    fun createEnvironment(): Call<Environment>

    @PATCH("/{envId}")
    fun updateEnvironment(@Path("envId") envId: Int): Call<Environment>

    @DELETE("/{envId}")
    fun deleteEnvironment(@Path("envId") envId: Int): Call<Any>

    @PUT("/enableNotif/{envId}")
    fun enableNotification(@Path("envId") envId: Int): Call<Any>

    companion object Factory {
        fun create(): DyrEnvironmentService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(null) // TODO
                .baseUrl("/environment")
                .build()
            return retrofit.create(DyrEnvironmentService::class.java)
        }
    }
}