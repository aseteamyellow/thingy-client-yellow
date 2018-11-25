package ch.snipy.thingyClientYellow.routes

import ch.snipy.thingyClientYellow.Animal
import ch.snipy.thingyClientYellow.Id
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface DyrAnimalService {
    @GET("all/{envId}")
    fun getAllAnimals(@Path("userId") userId: Id): Observable<List<Animal>>

    @GET("one/{animalId}")
    fun getOneAnimal(@Path("animalId") animalId: Id): Observable<Animal>

    @POST("{envId}")
    fun createAnimal(@Path("envId") environmentId: Id, @Body body: Animal): Observable<Animal>

    @PATCH("{animalId}")
    fun updateAnimal(@Path("animalId") animalId: Id): Observable<Animal>

    @DELETE("{animalId}")
    fun deleteAnimal(@Path("animalId") animalId: Id): Observable<String>

    companion object Factory : DyrServiceFactory {
        fun create(): DyrAnimalService {
            val retrofit = Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("$baseUrl/animal/")
                .build()
            return retrofit.create(DyrAnimalService::class.java)
        }
    }
}