package ch.snipy.thingyClientYellow.routes

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface DyrAccountService {
    @POST("/register")
    fun register(): Call<Any>

    @POST("/connect")
    fun connect(): Call<Any>

    @PATCH("/update/{userId}")
    fun update(@Path("userId") userId: String)

    @DELETE("/delete/{userId}")
    fun delete(@Path("userId") userId: String)

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