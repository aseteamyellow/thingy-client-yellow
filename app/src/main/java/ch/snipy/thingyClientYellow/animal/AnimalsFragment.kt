package ch.snipy.thingyClientYellow.animal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.snipy.thingyClientYellow.AnimalsItemViewListener
import ch.snipy.thingyClientYellow.Id
import ch.snipy.thingyClientYellow.R
import ch.snipy.thingyClientYellow.routes.DyrAnimalService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AnimalsFragment : Fragment() {

    // For logging
    private val loggingTag = "ANIMALS_FRAGMENT"

    // For the recycler view
    private lateinit var recyclerView: RecyclerView

    // Listener
    private lateinit var listener: AnimalsItemViewListener

    // Api call
    private val animalService by lazy { DyrAnimalService.create() }
    private var disposable: Disposable? = null

    // current state
    private var environmentId: Id = -1

    companion object {
        fun newInstance(animalsItemViewListener: AnimalsItemViewListener, environmentId: Id): AnimalsFragment {
            val fragment = AnimalsFragment()
            fragment.listener = animalsItemViewListener
            fragment.environmentId = environmentId
            return fragment
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        disposable = animalService.getAllAnimals(environmentId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Log.d(loggingTag, result.toString())
                },
                { error ->
                    Log.e(loggingTag, error.toString())
                }
            )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val rootView = inflater.inflate(R.layout.fragment_environment_animals, container, false)


        recyclerView = rootView.findViewById<RecyclerView>(R.id.animals_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = AnimalAdapter(
                dataset = mutableListOf(),
                context = context!!,
                listener = listener

            )
        }
        return rootView
    }
}