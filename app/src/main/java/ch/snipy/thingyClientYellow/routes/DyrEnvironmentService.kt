package ch.snipy.thingyClientYellow.routes

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*

interface DyrEnvironmentService {
    // TODO modidy the routes because they are the same...
    @GET("/{userId}")
    fun getEnvironments(@Path("userId") userId: String): Call<Any>

    @GET("/{envId}")
    fun getEnvironment(@Path("envId") envId: String): Call<Any>

    @POST("/")
    fun createEnvironment(): Call<Any>

    @PATCH("/{envId}")
    fun updateEnvironment(@Path("envId") envId: String): Call<Any>

    @DELETE("/{envId}")
    fun deleteEnvironment(@Path("envId") envId: String): Call<Any>

    @PUT("/enableNotif/{envId}")
    fun enableNotification(@Path("envId") envId: String): Call<Any>

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