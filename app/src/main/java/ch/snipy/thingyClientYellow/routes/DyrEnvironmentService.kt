package ch.snipy.thingyClientYellow.routes

import ch.snipy.thingyClientYellow.Environment
import ch.snipy.thingyClientYellow.Id
import ch.snipy.thingyClientYellow.Token
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface DyrEnvironmentService {

    @GET("all/{userId}")
    fun getEnvironments(@Header("token") token: Token, @Path("userId") userId: Id): Observable<List<Environment>>

    @GET("one/{envId}")
    fun getEnvironment(@Header("token") token: Token, @Path("envId") envId: Id): Observable<Environment>

    @POST("{userId}")
    fun createEnvironment(@Header("token") token: Token, @Path("userId") userId: Id, @Body body: Environment): Observable<Environment>

    @PATCH("{envId}")
    fun updateEnvironment(@Header("token") token: Token, @Path("envId") envId: Id, @Body body: Environment): Observable<Environment>

    @DELETE("{envId}")
    fun deleteEnvironment(@Header("token") token: Token, @Path("envId") envId: Id): Observable<ResponseBody>

    @PUT("enableNotif/{envId}")
    fun enableNotification(@Header("token") token: Token, @Path("envId") envId: Id, @Body body: Map<String, String>): Observable<String>

    @GET("sensors/{thingyId}")
    fun getSensorsData(@Header("token") token: Token, @Path("thingyId") thingyId: String): Observable<Map<String, Any>>

    companion object Factory : DyrServiceFactory {
        fun create(ipAddress: String = defaultIpAddress): DyrEnvironmentService {
            val retrofit = Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://$ipAddress/environment/")
                .build()
            return retrofit.create(DyrEnvironmentService::class.java)
        }
    }
}