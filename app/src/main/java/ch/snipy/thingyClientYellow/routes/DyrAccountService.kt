package ch.snipy.thingyClientYellow.routes

import ch.snipy.thingyClientYellow.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface DyrAccountService {
    @POST("/register")
    fun register(): Call<User>

    @POST("/connect")
    fun connect(): Call<User>

    @PATCH("/update/{userId}")
    fun update(@Path("userId") userId: String): Call<User>

    @DELETE("/delete/{userId}")
    fun delete(@Path("userId") userId: String): Call<Any>

    companion object Factory {
        fun create(): DyrAccountService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(null) // TODO
                .baseUrl("/account")
                .build()
            return retrofit.create(DyrAccountService::class.java)
        }
    }
}