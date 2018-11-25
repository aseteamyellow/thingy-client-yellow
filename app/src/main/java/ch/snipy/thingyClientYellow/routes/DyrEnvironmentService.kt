package ch.snipy.thingyClientYellow.routes

import ch.snipy.thingyClientYellow.Environment
import ch.snipy.thingyClientYellow.Id
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface DyrEnvironmentService {

    @GET("all/{userId}")
    fun getEnvironments(@Path("userId") userId: Id): Observable<List<Environment>>

    @GET("one/{envId}")
    fun getEnvironment(@Path("envId") envId: Id): Observable<Environment>

    @POST("{userId}")
    fun createEnvironment(@Path("userId") userId: Id, @Body body: Environment): Observable<Environment>

    @PATCH("{envId}")
    fun updateEnvironment(@Path("envId") envId: Id, @Body body: Environment): Observable<Environment>

    @DELETE("{envId}")
    fun deleteEnvironment(@Path("envId") envId: Id): Observable<ResponseBody>

    @PUT("enableNotif/{envId}")
    fun enableNotification(@Path("envId") envId: Id, @Body body: Map<String, String>): Observable<String>

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