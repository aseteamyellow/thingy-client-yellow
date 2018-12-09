package ch.snipy.thingyClientYellow

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import ch.snipy.thingyClientYellow.routes.DyrAccountService
import ch.snipy.thingyClientYellow.routes.DyrAnimalService
import ch.snipy.thingyClientYellow.routes.DyrAnimalTypeService
import ch.snipy.thingyClientYellow.routes.DyrEnvironmentService
import io.reactivex.disposables.Disposable

abstract class UserAbstractFragmentActivity : AppCompatActivity() {

    // Current user
    lateinit var sharedPref: SharedPreferences

    // API call
    lateinit var accountService: DyrAccountService
    lateinit var environmentService: DyrEnvironmentService
    lateinit var animalService: DyrAnimalService
    lateinit var animalTypeService: DyrAnimalTypeService

    // utility to terminate correctly the call to the services
    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    fun userId(): Int = sharedPref.getInt(getString(R.string.userId), -1)
    fun userToken(): Token = sharedPref.getString(getString(R.string.tokenId), "undefined")!!
    fun userEmail(): String = sharedPref.getString(getString(R.string.userEmailSharedPref), "undefined")!!
    fun userPassword(): String = sharedPref.getString(getString(R.string.userPasswordSharedPref), "undefined")!!

    private fun init() {
        // init shared preference
        sharedPref = getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE)

        // init retrofit helper
        // TODO use user defined ip address ?
        accountService = DyrAccountService.create()
        environmentService = DyrEnvironmentService.create()
        animalService = DyrAnimalService.create()
        animalTypeService = DyrAnimalTypeService.create()
    }

    @SuppressLint("ApplySharedPref")
    fun updateSharedPref(user: User) {
        with(sharedPref.edit()) {
            putInt(getString(R.string.userId), user.id ?: -1)
            putString(getString(R.string.tokenId), user.token ?: "undefined")
            putString(getString(R.string.userEmailSharedPref), user.email)
            putString(getString(R.string.userPasswordSharedPref), user.password)
            commit()
        }
        assert(sharedPref.getInt(getString(R.string.userId), -1) ?: -1 != -1)
    }
}