package ch.snipy.thingyClientYellow.routes

import ch.snipy.thingyClientYellow.Token
import ch.snipy.thingyClientYellow.User
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface DyrAccountService {
    @POST("register")
    fun register(@Header("token") token: Token, @Body body: User): Observable<User>

    @POST("connect")
    fun connect(@Header("token") token: Token, @Body body: User): Observable<User>

    @PATCH("update/{userId}")
    fun update(@Header("token") token: Token, @Path("userId") userId: Int, @Body body: User): Observable<User>

    @DELETE("delete/{userId}")
    fun delete(@Header("token") token: Token, @Path("userId") userId: Int): Observable<ResponseBody>

    companion object Factory : DyrServiceFactory {
        fun create(ipAddress: String = defaultIpAddress): DyrAccountService {
            val retrofit = Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://$ipAddress/account/")
                .build()
            return retrofit.create(DyrAccountService::class.java)
        }
    }
}