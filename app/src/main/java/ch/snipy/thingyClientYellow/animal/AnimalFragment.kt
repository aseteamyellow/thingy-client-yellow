package ch.snipy.thingyClientYellow.animal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import ch.snipy.thingyClientYellow.Animal
import ch.snipy.thingyClientYellow.R

class AnimalFragment : Fragment() {

    // Data
    lateinit var animal: Animal

    // UI
    private lateinit var photo: ImageView
    private lateinit var name: TextView

    // For API call TODO
    // val environmentService by lazy { DyrEnvironmentService.create() }
    // var disposable: Disposable? = null

    companion object {
        fun newInstance(animal: Animal): AnimalFragment {
            val fragment = AnimalFragment()
            fragment.animal = animal
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_animal, container, false)

        photo = rootView.findViewById(R.id.animal_photo)
        name = rootView.findViewById(R.id.animal_name)

        name.text = animal.name

        return rootView
    }
}