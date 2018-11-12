package ch.snipy.thingyClientYellow.routes

import android.os.Environment
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface DyrEnvironmentService {

    @GET("/all/{userId}")
    fun getEnvironments(@Path("userId") userId: Int): Observable<List<Environment>>

    @GET("/one/{envId}")
    fun getEnvironment(@Path("envId") envId: Int): Observable<Environment>

    @POST("/{userId}")
    fun createEnvironment(@Path("userId") userId: Int): Observable<Environment>

    @PATCH("/{envId}")
    fun updateEnvironment(@Path("envId") envId: Int): Observable<Environment>

    @DELETE("/{envId}")
    fun deleteEnvironment(@Path("envId") envId: Int): Observable<String>

    @PUT("/enableNotif/{envId}")
    fun enableNotification(@Path("envId") envId: Int, @Body body: Map<String, String>): Observable<String>

    companion object Factory : DyrServiceFactory {
        fun create(): DyrEnvironmentService {
            val retrofit = Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("$baseUrl/environment/")
                .build()
            return retrofit.create(DyrEnvironmentService::class.java)
        }
    }
}