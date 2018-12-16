package ch.snipy.thingyClientYellow.routes

import ch.snipy.thingyClientYellow.Animal
import ch.snipy.thingyClientYellow.Id
import ch.snipy.thingyClientYellow.Token
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface DyrAnimalService {
    @GET("allOfUser/{userId}")
    fun getAllAnimals(@Header("token") token: Token, @Path("userId") userId: Id): Observable<List<Animal>>

    @GET("all/{envId}")
    fun getAllAnimalsForAnEnvironment(@Header("token") token: Token, @Path("envId") environmentId: Id): Observable<List<Animal>>

    @GET("one/{animalId}")
    fun getOneAnimal(@Header("token") token: Token, @Path("animalId") animalId: Id): Observable<Animal>

    @POST("{envId}")
    fun createAnimal(@Header("token") token: Token, @Path("envId") environmentId: Id, @Body body: Animal): Observable<Animal>

    @PATCH("{animalId}")
    fun updateAnimal(@Header("token") token: Token, @Path("animalId") animalId: Id, @Body body: Animal): Observable<Animal>

    @DELETE("{animalId}")
    fun deleteAnimal(@Header("token") token: Token, @Path("animalId") animalId: Id): Observable<ResponseBody>

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