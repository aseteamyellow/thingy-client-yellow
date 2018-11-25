package ch.snipy.thingyClientYellow

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import ch.snipy.thingyClientYellow.animal.AnimalCreationFragment
import ch.snipy.thingyClientYellow.animal.AnimalFragment
import ch.snipy.thingyClientYellow.environment.EnvironmentCreationFragment
import ch.snipy.thingyClientYellow.environment.EnvironmentFragment
import ch.snipy.thingyClientYellow.environment.EnvironmentsFragment

class MainActivity : UserAbstractFragmentActivity(),
                     EnvironmentsItemViewListener,
                     AnimalsItemViewListener {

    private val loggingTag = "MAIN_ACTIVITY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE)

        // setup the fragment, the first one is to view all the environments
        supportFragmentManager.beginTransaction()
            .add(R.id.main_activity_frame_layout, EnvironmentsFragment.newInstance(this))
            .addToBackStack(null)
            .commit()
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
        Log.d(loggingTag, "on click create environment navigation")
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

}