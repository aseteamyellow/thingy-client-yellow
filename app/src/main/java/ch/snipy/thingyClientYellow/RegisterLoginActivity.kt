package ch.snipy.thingyClientYellow

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RegisterLoginActivity : UserAbstractFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // For network access
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        setContentView(R.layout.activity_register_login)
        supportFragmentManager.beginTransaction()
            .add(R.id.register_login_activity_frame_layout, LoginFragment.newInstance())
            .commit()
    }

    fun onSelectFragment(view: View) {
        when (view.id) {
            R.id.sign_up_nav -> {
                Log.d("Sign up nav", "NAV")
                val transaction = supportFragmentManager.beginTransaction()
                val fragment = RegisterFragment.newInstance()
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                transaction.replace(R.id.register_login_activity_frame_layout, fragment)
                transaction.commit()
            }
            R.id.sign_in_nav -> {
                Log.d("Sign in nav", "NAV")
                val transaction = supportFragmentManager.beginTransaction()
                val fragment = LoginFragment.newInstance()
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                transaction.replace(R.id.register_login_activity_frame_layout, fragment)
                transaction.commit()
            }
        }
    }

    fun onClickCreateAccount(view: View) {
        Log.d("CALLBACK", "on click create account : ${view.id}")

        val email = findViewById<EditText>(R.id.create_account_email)
        val password1 = findViewById<EditText>(R.id.create_account_password_1).text.toString()
        val password2 = findViewById<EditText>(R.id.create_account_password_2).text.toString()

        register(password1, password2, email.text.toString())
    }

    fun onClickSignIn(view: View) {
        Log.d("CALLBACK", "on click create account : ${view.id}")
        val email = findViewById<AutoCompleteTextView>(R.id.login_email).text.toString()
        val password = findViewById<EditText>(R.id.login_password).text.toString()

        connect(email, password)
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    @SuppressLint("SetTextI18n")
    private fun connect(email: String, password: String) {
        if (!isNetworkAvailable()) {
            Toast.makeText(applicationContext, getString(R.string.internet_error), Toast.LENGTH_SHORT).show()
            return
        }

        val bar = findViewById<ProgressBar>(R.id.login_progressBar)
        bar.visibility = VISIBLE

        disposable =
                accountService.connect(User(null, null, email, password))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { result: User ->
                            updateSharedPref(result)
                            Toast.makeText(applicationContext, "Connection done !", Toast.LENGTH_SHORT).show()
                            Log.d("CONNECT", result.toString())
                            bar.visibility = INVISIBLE
                            navigateToMainActivity()
                        },
                        { error ->
                            Toast.makeText(applicationContext, R.string.login_fail, Toast.LENGTH_SHORT).show()
                            Log.e("CONNECT", error.message)
                            bar.visibility = INVISIBLE
                        }
                    )
    }

    private fun register(password1: String, password2: String, email: String) {
        if (!isNetworkAvailable()) {
            Toast.makeText(applicationContext, getString(R.string.internet_error), Toast.LENGTH_SHORT).show()
            return
        }

        val bar = findViewById<ProgressBar>(R.id.register_progressBar)
        bar.visibility = VISIBLE

        if (password1 != password2) {
            Toast.makeText(applicationContext, getString(R.string.passwords_not_same), Toast.LENGTH_SHORT).show()
        }
        assert(password1 == password2)

        disposable =
                accountService.register(User(null, null, email, password1))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { result ->
                            updateSharedPref(result)
                            Toast.makeText(applicationContext, "Registration done !", Toast.LENGTH_SHORT).show()
                            Log.d("REGISTER", result.toString())
                            bar.visibility = INVISIBLE
                            navigateToMainActivity()
                        },
                        { error ->
                            Toast.makeText(applicationContext, R.string.register_fail, Toast.LENGTH_SHORT).show()
                            Log.e("REGISTER", error.message)
                            bar.visibility = INVISIBLE
                        }
                    )
    }

    private fun navigateToMainActivity() {
        Log.d("REGISTER_LOGIN", "navigate to main activity")
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
