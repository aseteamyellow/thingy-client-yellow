package ch.snipy.thingyClientYellow.environment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ch.snipy.thingyClientYellow.Environment
import ch.snipy.thingyClientYellow.R

class EnvironmentFragment : Fragment() {

    private lateinit var environment: Environment

    // UI field

    private lateinit var name: TextView
    private lateinit var type: TextView

    companion object {
        fun newInstance(environment: Environment): EnvironmentFragment {
            val fragment = EnvironmentFragment()
            fragment.environment = environment
            // TODO call service

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_environment, container, false)
        super.onCreateView(inflater, container, savedInstanceState)

        name = view.findViewById(R.id.fragment_environment_name)
        type = view.findViewById(R.id.fragment_environment_type)

        name.text = environment.name
        type.text = environment.envType

        return view
    }
}