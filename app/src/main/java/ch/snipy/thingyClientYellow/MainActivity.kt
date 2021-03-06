package ch.snipy.thingyClientYellow

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ch.snipy.thingyClientYellow.animal.AnimalCreationFragment
import ch.snipy.thingyClientYellow.animal.AnimalFragment
import ch.snipy.thingyClientYellow.animal.AnimalsFragment
import ch.snipy.thingyClientYellow.environment.EnvironmentCreationFragment
import ch.snipy.thingyClientYellow.environment.EnvironmentFragment
import ch.snipy.thingyClientYellow.environment.EnvironmentsFragment
import com.google.android.material.navigation.NavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

class MainActivity : UserAbstractFragmentActivity(),
                     EnvironmentsItemViewListener,
                     AnimalsItemViewListener {

    private val loggingTag = "MAIN_ACTIVITY"

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Drawer
        drawerLayout = findViewById(R.id.drawer_layout)

        navigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(::onSelectDrawerItem)

        // filter the broadcast
        LocalBroadcastManager.getInstance(this).registerReceiver(
            mainActivityBroadcastReceiver,
            IntentFilter(getString(R.string.firebaseData))
        )

        // Start the notification service
        startService(Intent(this, DyrNotificationService::class.java))

        // setup the fragment, the first one is to view all the environments
        supportFragmentManager.beginTransaction()
            .add(R.id.main_activity_frame_layout, EnvironmentsFragment.newInstance(this))
            .addToBackStack(null)
            .commit()
    }

    private fun onSelectDrawerItem(menuItem: MenuItem): Boolean {
        menuItem.isChecked = true
        drawerLayout.closeDrawers()

        when (menuItem.itemId) {
            R.id.nav_menu_item_my_environments ->
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_activity_frame_layout, EnvironmentsFragment.newInstance(
                            this
                        )
                    )
                    .addToBackStack(null)
                    .commit()
            R.id.nav_menu_item_my_animals ->
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_activity_frame_layout, AnimalsFragment.newInstance(
                            this
                        )
                    )
                    .addToBackStack(null)
                    .commit()
            R.id.nav_menu_item_settings ->
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_activity_frame_layout, SettingsFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            R.id.nav_menu_item_disconnection ->
                {val intent = Intent(this, RegisterLoginActivity::class.java)
                startActivity(intent)}
        }

        return true
    }

    override fun onEnvironmentItemClick(view: View, environment: Environment) {
        Log.d(loggingTag, "onEnvironmentItemClick : ${environment.id ?: -1}")
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_frame_layout, EnvironmentFragment.newInstance(environment, this))
            .addToBackStack(null)
            .commit()
    }

    override fun onAnimalItemClick(view: View, animal: Animal) {
        Log.d(loggingTag, "onAnimalItemClick : ${animal.id ?: -1}")
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_frame_layout, AnimalFragment.newInstance(animal))
            .addToBackStack(null)
            .commit()
    }

    fun onClickCreateEnvironment(view: View) {
        Log.d(loggingTag, "on click create environment navigation, view id : ${view.id}")
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_frame_layout, EnvironmentCreationFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    fun onClickCreateAnimalNavigation(environment: Environment) {
        Log.d(loggingTag, "on click create animal navigation")
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_frame_layout, AnimalCreationFragment.newInstance(environment))
            .addToBackStack(null)
            .commit()
    }

    /*override fun onAnimalItemDeleteClick(
        view: View,
        animal: Animal,
        onSuccess: (ResponseBody) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        disposable = animalService.deleteAnimal(token = userToken(), animalId = animal.id ?: -1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onSuccess(it) }, { onError(it) })
    }*/

    /*override fun onEnvironmentItemDeleteClick(
        view: View,
        environment: Environment,
        onSuccess: (ResponseBody) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        disposable = environmentService.deleteEnvironment(token = userToken(), envId = environment.id ?: -1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onSuccess(it) }, { onError(it) })
    }*/

    @SuppressLint("SetTextI18n")
    fun setUserMenu() {
        //findViewById<TextView>(R.id.header_title).text = "Connected as " + userId().toString()
        navigationView.getHeaderView(0).findViewById<TextView>(R.id.header_title).text = "Connected as " + userEmail()
    }

    fun onNewToken(token: String?) {
        disposable = accountService.update(
            token = userToken(),
            userId = userId(),
            body = User(
                email = userEmail(),
                password = userPassword(),
                firebaseToken = token
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    updateSharedPref(user)
                    Log.d(loggingTag, "update the user : " + user.toString())
                },
                { error ->
                    Log.e(loggingTag, error.toString())
                }
            )
    }

    private val mainActivityBroadcastReceiver = object : BroadcastReceiver() {
        // From broadcastReceiver
        override fun onReceive(context: Context?, intent: Intent?) {
            onNewToken(
                intent?.extras?.getString("token")
            )
        }
    }
}