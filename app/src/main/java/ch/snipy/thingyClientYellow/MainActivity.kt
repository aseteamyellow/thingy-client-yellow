package ch.snipy.thingyClientYellow

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import ch.snipy.thingyClientYellow.environment.EnvironmentsFragment
import ch.snipy.thingyClientYellow.environment.RecyclerViewListener

class MainActivity : FragmentActivity(), RecyclerViewListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // setup the fragment, the first one is to view all the environments
        supportFragmentManager.beginTransaction()
            .add(R.id.main_activity_frame_layout, EnvironmentsFragment.newInstance(this))
            .commit()
    }

    override fun onItemClick(view: View, position: Int) {
        Log.d("MAIN_ACTIVITY", "onItemClick : $position")
        Toast.makeText(applicationContext, "View one environment", Toast.LENGTH_SHORT).show()
        /*
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_frame_layout, EnvironmentFragment.newInstance())
            .commit()
         */
    }

    fun onClickCreateEnvironment(view: View) {
        Toast.makeText(applicationContext, "Create environment", Toast.LENGTH_SHORT).show()
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
