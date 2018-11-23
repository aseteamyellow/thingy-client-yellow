package ch.snipy.thingyClientYellow

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import ch.snipy.thingyClientYellow.animal.AnimalFragment
import ch.snipy.thingyClientYellow.environment.EnvironmentFragment
import ch.snipy.thingyClientYellow.environment.EnvironmentsFragment

class MainActivity : UserAbstractFragmentActivity(),
                     EnvironmentsItemViewListener,
                     AnimalsItemViewListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup the fragment, the first one is to view all the environments
        supportFragmentManager.beginTransaction()
            .add(R.id.main_activity_frame_layout, EnvironmentsFragment.newInstance(this))
            .addToBackStack(null)
            .commit()
    }

    override fun onEnvironmentItemClick(view: View, environment: Environment) {
        Log.d("MAIN_ACTIVITY", "onEnvironmentItemClick : ${environment.id ?: -1}")
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_frame_layout, EnvironmentFragment.newInstance(environment, this))
            .addToBackStack(null)
            .commit()
    }

    override fun onAnimalItemClick(view: View, animal: Animal) {
        Log.d("MAIN_ACTIVITY", "onAnimalItemClick : ${animal.id ?: -1}")
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_frame_layout, AnimalFragment.newInstance(animal))
            .addToBackStack(null)
            .commit()
    }

    // TODO
    fun onClickCreateEnvironment(view: View) {
        Toast.makeText(applicationContext, "Create environment", Toast.LENGTH_SHORT).show()
    }

}