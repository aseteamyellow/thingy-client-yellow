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
import ch.snipy.thingyClientYellow.Environment
import ch.snipy.thingyClientYellow.MainActivity
import ch.snipy.thingyClientYellow.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AnimalCreationFragment : Fragment() {

    // For logging
    private val loggingTag = "ENVIRONMENT_CREATION"

    // UI field
    private lateinit var name: EditText
    private lateinit var type: EditText

    // Button
    private lateinit var cancelButton: Button
    private lateinit var createButton: Button

    // The current environment
    private lateinit var environment: Environment

    // For API call
    private val animalService by lazy { ((activity) as MainActivity).animalService }
    private var disposable: Disposable? = null

    companion object {
        fun newInstance(environment: Environment): AnimalCreationFragment {
            val fragment = AnimalCreationFragment()
            fragment.environment = environment
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val rootView = inflater.inflate(R.layout.fragment_animal_creation, container, false)

        name = rootView.findViewById(R.id.animal_creation_name)
        type = rootView.findViewById(R.id.animal_creation_type)

        cancelButton = rootView.findViewById(R.id.animal_creation_cancel_button)
        cancelButton.setOnClickListener { _ -> fragmentManager?.popBackStack() }

        createButton = rootView.findViewById(R.id.animal_creation_create_button)
        createButton.setOnClickListener(::onClickCreateAnimal)

        return rootView
    }

    // TODO add a progress bar

    private fun onClickCreateAnimal(view: View) {
        Log.d(loggingTag, "create button callback, id : ${view.id}")

        disposable = animalService.createAnimal(
            environment.id ?: -1, Animal(
                name = name.text.toString()
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { animal ->
                    Log.d(loggingTag, animal.toString())
                    Toast.makeText(activity, "Environment has been successfully created", Toast.LENGTH_SHORT).show()
                    fragmentManager?.popBackStack()
                },
                { error ->
                    Log.e(loggingTag, error.toString())
                }
            )
    }
}