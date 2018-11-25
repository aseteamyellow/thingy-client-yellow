package ch.snipy.thingyClientYellow

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.FragmentActivity
import ch.snipy.thingyClientYellow.routes.DyrAccountService
import ch.snipy.thingyClientYellow.routes.DyrAnimalService
import ch.snipy.thingyClientYellow.routes.DyrEnvironmentService
import io.reactivex.disposables.Disposable

abstract class UserAbstractFragmentActivity : FragmentActivity() {

    // Current user
    lateinit var sharedPref: SharedPreferences

    // API call
    val accountService by lazy { DyrAccountService.create() }
    val environmentService by lazy { DyrEnvironmentService.create() }
    val animalService by lazy { DyrAnimalService.create() }

    // utility to terminate correctly the call to the services
    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        // init shared preference
        sharedPref = getPreferences(Context.MODE_PRIVATE)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // init shared preference
        sharedPref = getPreferences(Context.MODE_PRIVATE)!!
    }

    fun userId(): Int = sharedPref.getInt(getString(R.string.userId), -1)
    fun userToken(): Token = sharedPref.getString(getString(R.string.tokenId), "undefined")!!

    @SuppressLint("ApplySharedPref")
    fun updateSharedPref(user: User) {
        with(sharedPref.edit()) {
            putInt(getString(R.string.userId), user.id ?: -1)
            putString(getString(R.string.tokenId), user.token ?: "undefined")
            commit()
        }
    }
}