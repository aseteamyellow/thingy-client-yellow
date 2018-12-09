package ch.snipy.thingyClientYellow.routes

import ch.snipy.thingyClientYellow.AnimalType
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface DyrAnimalTypeService {

    @GET("animalType")
    fun getAnimalTypes(): Observable<List<AnimalType>>

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