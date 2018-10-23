package ch.snipy.thingyClientYellow

import android.app.Activity
import android.os.Bundle

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : Activity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}
