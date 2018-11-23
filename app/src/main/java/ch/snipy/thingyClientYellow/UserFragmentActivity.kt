package ch.snipy.thingyClientYellow

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.FragmentActivity

abstract class UserFragmentActivity : FragmentActivity() {

    // Current user
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        // init shared preference
        sharedPref = getPreferences(Context.MODE_PRIVATE)!!
    }

    fun userId(): Int = sharedPref.getInt(getString(R.string.userId), -1)
    fun userToken(): Token = sharedPref.getString(getString(R.string.tokenId), "undefined")!!

    fun updateSharedPref(user: User?) {
        with(sharedPref.edit()) {
            putInt(getString(R.string.userId), user?.id ?: -1)
            putString(getString(R.string.tokenId), user?.token ?: "undefined")
            commit()
        }
    }
}