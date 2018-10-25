package ch.snipy.thingyClientYellow

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class RegisterLoginActivity : FragmentActivity(), LoginSignal, RegisterSignal {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_login)
    }

    override fun onRegisterSignal() {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val registerFragment = RegisterActivityFragment.newInstance()
        registerFragment.loginSignal = this
        transaction.replace(R.id.container, registerFragment, null)
        transaction.commit()
    }

    override fun onLoginSignal() {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val loginFragment = LoginActivityFragment.newInstance()
        loginFragment.registerSignal = this
        transaction.replace(R.id.container, loginFragment, null)
        transaction.commit()
    }

}
