package ch.snipy.thingyClientYellow.animal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import ch.snipy.thingyClientYellow.Animal
import ch.snipy.thingyClientYellow.MainActivity
import ch.snipy.thingyClientYellow.R
import ch.snipy.thingyClientYellow.routes.DyrAnimalService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AnimalUpdateFragment : Fragment() {

    // For logging
    private val loggingTag = "ANIMAL_UPDATE_FRAGMENT"

    // UI field
    private lateinit var name: EditText

    // Button
    private lateinit var cancelButton: Button
    private lateinit var createButton: Button

    // The current animal
    private lateinit var animal: Animal

    // For API call
    private val animalService by lazy { DyrAnimalService.create() }
    private var disposable: Disposable? = null

    companion object {
        fun newInstance(animal: Animal): AnimalUpdateFragment {
            val fragment = AnimalUpdateFragment()
            fragment.animal = animal
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_animal_creation, container, false)

        name = rootView.findViewById(R.id.animal_update_name)

        cancelButton = rootView.findViewById(R.id.animal_update_cancel_button)
        cancelButton.setOnClickListener { _ -> fragmentManager?.popBackStack() }

        createButton = rootView.findViewById(R.id.animal_update_update_button)
        createButton.setOnClickListener(::onClickUpdateAnimal)

        return rootView
    }

    private fun onClickUpdateAnimal(view: View) {
        Log.d(loggingTag, "update button callback, id : ${view.id}")

        disposable = animalService.updateAnimal(animal.id ?: -1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { animal ->
                    Log.d(loggingTag, "animal update success")
                    Toast.makeText(activity, "Animal successfully updated", Toast.LENGTH_SHORT).show()
                    (activity as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_activity_frame_layout, AnimalFragment.newInstance(animal))
                        .commit()
                },
                { error ->
                    Log.e(loggingTag, error.toString())
                    Toast.makeText(activity, "Can't update animal...", Toast.LENGTH_SHORT).show()
                }
            )
    }
}