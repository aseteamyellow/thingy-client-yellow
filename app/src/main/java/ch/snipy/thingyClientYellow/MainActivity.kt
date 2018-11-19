package ch.snipy.thingyClientYellow

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import ch.snipy.thingyClientYellow.environment.EnvironmentFragment
import ch.snipy.thingyClientYellow.environment.EnvironmentsFragment

class MainActivity : FragmentActivity(),
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
        Toast.makeText(applicationContext, "animal item click", Toast.LENGTH_SHORT).show()
    }

    fun onClickCreateEnvironment(view: View) {
        Toast.makeText(applicationContext, "Create environment", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    // add a new environment
    fun addEnvironment(view: View): Unit = TODO()

    // delete an existing environment
    fun deleteEnvironment(view: View): Unit = TODO()

    // edit an existing environment
    fun editEnvironment(view: View): Unit = TODO()

    // watch a specific environment
    fun watchEnvironment(view: View): Unit = TODO()
}