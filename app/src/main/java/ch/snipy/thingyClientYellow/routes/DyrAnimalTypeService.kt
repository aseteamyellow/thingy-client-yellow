package ch.snipy.thingyClientYellow.routes

import ch.snipy.thingyClientYellow.AnimalType
import ch.snipy.thingyClientYellow.Token
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

interface DyrAnimalTypeService {

    @GET("animalType")
    fun getAnimalTypes(@Header("token") token: Token): Observable<List<AnimalType>>

    companion object Factory : DyrServiceFactory {
        fun create(ipAddress: String = defaultIpAddress): DyrAnimalTypeService {
            val retrofit = Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://$ipAddress/")
                .build()
            return retrofit.create(DyrAnimalTypeService::class.java)
        }
    }
}