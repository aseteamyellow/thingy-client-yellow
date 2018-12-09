package ch.snipy.thingyClientYellow.routes

import ch.snipy.thingyClientYellow.Animal
import ch.snipy.thingyClientYellow.Id
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface DyrAnimalService {
    @GET("allOfUser/{userId}")
    fun getAllAnimals(@Path("userId") userId: Id) : Observable<List<Animal>>

    @GET("all/{envId}")
    fun getAllAnimalsForAnEnvironment(@Path("envId") environmentId: Id): Observable<List<Animal>>

    @GET("one/{animalId}")
    fun getOneAnimal(@Path("animalId") animalId: Id): Observable<Animal>

    @POST("{envId}")
    fun createAnimal(@Path("envId") environmentId: Id, @Body body: Animal): Observable<Animal>

    @PATCH("{animalId}")
    fun updateAnimal(@Path("animalId") animalId: Id, @Body body: Animal): Observable<Animal>

    @DELETE("{animalId}")
    fun deleteAnimal(@Path("animalId") animalId: Id): Observable<ResponseBody>

    companion object Factory : DyrServiceFactory {
        fun create(ipAddress: String = defaultIpAddress): DyrAnimalService {
            val retrofit = Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://$ipAddress/animal/")
                .build()
            return retrofit.create(DyrAnimalService::class.java)
        }
    }
}