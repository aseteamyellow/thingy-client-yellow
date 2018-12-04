package ch.snipy.thingyClientYellow.environment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.snipy.thingyClientYellow.*
import ch.snipy.thingyClientYellow.animal.AnimalAdapter
import ch.snipy.thingyClientYellow.routes.DyrAnimalService
import ch.snipy.thingyClientYellow.routes.DyrEnvironmentService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EnvironmentFragment : Fragment() {

    // For logging
    private val loggingTag = "ENVIRONMENT_FRAGMENT"

    // For the recycler view
    private lateinit var recyclerView: RecyclerView

    // Listener for animal's items
    private lateinit var listener: AnimalsItemViewListener

    // UI field
    private lateinit var name: TextView
    private lateinit var type: TextView

    // button
    private lateinit var addAnimalButton: Button
    private lateinit var updateEnvironmentButton: ImageButton
    private lateinit var viewVideoButton: Button

    // Api call
    private val environmentService by lazy { DyrEnvironmentService.create() }
    private val animalService by lazy { DyrAnimalService.create() }
    private var disposable: Disposable? = null


    // The current environment
    private lateinit var environment: Environment

    // data container for the animalsList
    private val animalsList: MutableList<Animal> = mutableListOf()

    companion object {
        fun newInstance(
            environment: Environment,
            animalsItemViewListener: AnimalsItemViewListener
        ): EnvironmentFragment {
            val fragment = EnvironmentFragment()
            fragment.environment = environment // TODO do we need to call the api service ?
            fragment.listener = animalsItemViewListener
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        disposable = animalService.getAllAnimalsForAnEnvironment(environment.id ?: -1) // quite ugly...
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { animals ->
                    animalsList.clear()
                    animalsList.addAll(animals)
                    recyclerView.adapter?.notifyDataSetChanged()
                },
                { error ->
                    Log.e(loggingTag, error.toString())
                }
            )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val rootView = inflater.inflate(R.layout.fragment_environment, container, false)

        recyclerView = rootView.findViewById<RecyclerView>(R.id.environment_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = AnimalAdapter(
                dataset = animalsList,
                context = context!!,
                listener = listener
            )
        }

        name = rootView.findViewById(R.id.fragment_environment_name)
        name.text = environment.name

        type = rootView.findViewById(R.id.fragment_environment_type)
        type.text = environment.envType

        addAnimalButton = rootView.findViewById(R.id.environment_add_animal_button)
        addAnimalButton.setOnClickListener(::onClickCreateAnimal)

        viewVideoButton = rootView.findViewById(R.id.environment_fragment_view_video_button)
        viewVideoButton.setOnClickListener(::onClickViewVideo)

        updateEnvironmentButton = rootView.findViewById(R.id.environment_fragment_update_button)
        updateEnvironmentButton.setOnClickListener(::onClickUpdateEnvironment)



        return rootView
    }

    private fun onClickViewVideo(view: View) {
        (activity as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_frame_layout, EnvironmentVideoFragment.newInstance(environment))
            .addToBackStack(null)
            .commit()
    }

    private fun onClickCreateAnimal(view: View) {
        Log.d(loggingTag, "create animal button callback, id : ${view.id}")
        (activity as MainActivity).onClickCreateAnimalNavigation(environment)
    }

    private fun onClickUpdateEnvironment(view: View) {
        Log.d(loggingTag, "On click update environment from view : ${view.id}")
        (activity as MainActivity).supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_activity_frame_layout,
                EnvironmentUpdateFragment.newInstance(environment)
            )
            .addToBackStack(null)
            .commit()
    }
}