package ch.snipy.thingyClientYellow.animal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.snipy.thingyClientYellow.Animal
import ch.snipy.thingyClientYellow.AnimalsItemViewListener
import ch.snipy.thingyClientYellow.R

class AnimalsFragment : Fragment() {

    // For the recycler view
    private lateinit var recyclerView: RecyclerView

    // Listener
    private lateinit var listener: AnimalsItemViewListener

    // For API call TODO
    // val environmentService by lazy { DyrEnvironmentService.create() }
    // var disposable: Disposable? = null

    companion object {
        fun newInstance(animalsItemViewListener: AnimalsItemViewListener): AnimalsFragment {
            val fragment = AnimalsFragment()
            fragment.listener = animalsItemViewListener
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val rootView = inflater.inflate(R.layout.fragment_environment_animals, container, false)


        recyclerView = rootView.findViewById<RecyclerView>(R.id.animals_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = AnimalAdapter(
                dataset = arrayOf(
                    Animal(null, "Cat"),
                    Animal(null, "Dog")
                ),
                context = context!!,
                listener = listener

            )
        }
        return rootView
    }
}