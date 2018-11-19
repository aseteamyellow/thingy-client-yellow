package ch.snipy.thingyClientYellow.environment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.snipy.thingyClientYellow.Animal
import ch.snipy.thingyClientYellow.AnimalsItemViewListener
import ch.snipy.thingyClientYellow.Environment
import ch.snipy.thingyClientYellow.R
import ch.snipy.thingyClientYellow.animal.AnimalAdapter

class EnvironmentFragment : Fragment() {

    // For the recycler view
    private lateinit var recyclerView: RecyclerView

    // Listener for animal's items
    private lateinit var listener: AnimalsItemViewListener

    // The current environment
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
        super.onCreateView(inflater, container, savedInstanceState)

        val rootView = inflater.inflate(R.layout.fragment_environment, container, false)

        recyclerView = rootView.findViewById<RecyclerView>(R.id.animals_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = AnimalAdapter(
                dataset = listOf(
                    Animal(null, "Cat"),
                    Animal(null, "Dog"),
                    Animal(null, "Bird")
                ),
                context = context!!,
                listener = listener
            )
        }

        name = rootView.findViewById(R.id.fragment_environment_name)
        type = rootView.findViewById(R.id.fragment_environment_type)

        name.text = environment.name
        type.text = environment.envType

        return rootView
    }
}