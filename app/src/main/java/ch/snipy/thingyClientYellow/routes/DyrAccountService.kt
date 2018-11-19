package ch.snipy.thingyClientYellow.routes

import ch.snipy.thingyClientYellow.User
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface DyrAccountService {
    @POST("register")
    fun register(@Body body: User): Observable<User>

    @POST("connect")
    fun connect(@Body body: User): Observable<User>

    @PATCH("update/{userId}")
    fun update(@Path("userId") userId: Int): Observable<User>

    @DELETE("delete/{userId}")
    fun delete(@Path("userId") userId: Int): Observable<String>

    companion object Factory : DyrServiceFactory {
        fun create(): DyrAccountService {
            val retrofit = Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("$baseUrl/account/")
                .build()
            return retrofit.create(DyrAccountService::class.java)
        }
    }
}