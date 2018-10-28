package ch.snipy.thingyClientYellow

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction

class RegisterLoginActivity : FragmentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_login)
        supportFragmentManager.beginTransaction()
            .add(R.id.register_login_activity_frame_layout, RegisterActivityFragment.newInstance())
            .commit()
    }

    fun onSelectFragment(view: View): Unit {
        when (view.id) {
            R.id.sign_up_nav -> {
                Log.d("Sign up nav", "NAV")
                val transaction = supportFragmentManager.beginTransaction()
                val fragment = RegisterActivityFragment.newInstance()
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                transaction.replace(R.id.register_login_activity_frame_layout, fragment)
                transaction.commit()
            }
            R.id.sign_in_nav -> {
                Log.d("Sign in nav", "NAV")
                val transaction = supportFragmentManager.beginTransaction()
                val fragment = LoginActivityFragment.newInstance()
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                transaction.replace(R.id.register_login_activity_frame_layout, fragment)
                transaction.commit()
            }
        }
    }

    fun onClickCreateAccount(view: View) {
        Log.d("on click create account", "CALLBACK")
        TODO()
    }

    fun onClickSignIn(view: View) {
        Log.d("on click sign in", "CALLBACK")
        TODO()
    }
}
