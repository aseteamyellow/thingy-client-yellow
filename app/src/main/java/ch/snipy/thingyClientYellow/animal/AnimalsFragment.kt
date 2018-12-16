package ch.snipy.thingyClientYellow.animal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.snipy.thingyClientYellow.Animal
import ch.snipy.thingyClientYellow.AnimalsItemViewListener
import ch.snipy.thingyClientYellow.MainActivity
import ch.snipy.thingyClientYellow.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AnimalsFragment : Fragment() {

    // For logging
    private val loggingTag = "ANIMALS_FRAGMENT"

    // data container
    private val animals: MutableList<Animal> = mutableListOf()

    // For the recycler view
    private lateinit var recyclerView: RecyclerView

    // Listener
    private lateinit var listener: AnimalsItemViewListener

    // Api call
    private val animalService by lazy { ((activity) as MainActivity).animalService }
    private val animalTypesService by lazy { ((activity) as MainActivity).animalTypeService }
    private var disposable: Disposable? = null

    companion object {
        fun newInstance(animalsItemViewListener: AnimalsItemViewListener): AnimalsFragment {
            val fragment = AnimalsFragment()
            fragment.listener = animalsItemViewListener
            return fragment
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        disposable = animalService.getAllAnimals(
            token = (activity as MainActivity).userToken(),
            userId = (activity as MainActivity).userId()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    animals.clear()
                    animals.addAll(result)
                    recyclerView.adapter?.notifyDataSetChanged()
                },
                { error ->
                    Log.e(loggingTag, error.toString())
                }
            )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val rootView = inflater.inflate(R.layout.fragment_environment_animals, container, false)

        disposable = animalTypesService.getAnimalTypes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    recyclerView = rootView.findViewById<RecyclerView>(R.id.animals_recycler_view).apply {
                        setHasFixedSize(true)
                        //layoutManager = LinearLayoutManager(activity)
                        layoutManager = GridLayoutManager(context!!,3)
                        adapter = AnimalAdapter(
                            dataset = animals,
                            context = context!!,
                            listener = listener,
                            animalTypes = result
                        )
                    }
                },
                { error ->
                    Log.e(loggingTag, error.toString())
                }
            )


        /*recyclerView = rootView.findViewById<RecyclerView>(R.id.animals_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = AnimalAdapter(
                dataset = animals,
                context = context!!,
                listener = listener,
                animalTypes =
            )
        }*/
        return rootView
    }
}