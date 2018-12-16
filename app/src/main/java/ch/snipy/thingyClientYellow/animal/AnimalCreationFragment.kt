package ch.snipy.thingyClientYellow.animal

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import ch.snipy.thingyClientYellow.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AnimalCreationFragment : Fragment(), AdapterView.OnItemSelectedListener {


    // For logging
    private val loggingTag = "ANIMAL_CREATION_FRAGMENT"

    // UI field
    private lateinit var name: EditText
    private lateinit var image: ImageView
    private lateinit var spinner: Spinner

    // Button
    private lateinit var cancelButton: Button
    private lateinit var createButton: Button

    // The current environment
    private lateinit var environment: Environment

    // types for an animal
    private lateinit var animalsTypes: List<AnimalType>

    // For API call
    private val animalService by lazy { ((activity) as MainActivity).animalService }
    private val animalTypeService by lazy { ((activity) as MainActivity).animalTypeService }
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
        image = rootView.findViewById(R.id.animal_creation_image)
        spinner = rootView.findViewById(R.id.spinner_creation_animal_type)

        name.onChange()

        cancelButton = rootView.findViewById(R.id.animal_creation_cancel_button)
        cancelButton.setOnClickListener { _ -> fragmentManager?.popBackStack() }

        createButton = rootView.findViewById(R.id.animal_creation_create_button)
        createButton.setOnClickListener(::onClickCreateAnimal)


        disposable = animalTypeService.getAnimalTypes(token = (activity as MainActivity).userToken())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    animalsTypes = result
                    spinner.adapter = ArrayAdapter<String>(
                        activity!!,
                        R.layout.support_simple_spinner_dropdown_item,
                        animalsTypes.map { it.type }
                    )
                    Log.d(loggingTag, result.map { Tuple(it.id, it.type) }.toString())
                },
                { error ->
                    Log.e(loggingTag, error.toString())
                }
            )

        spinner.onItemSelectedListener = this

        return rootView
    }

    // TODO add a progress bar

    private fun EditText.onChange() {
        this.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                createButton.isEnabled = name.text.isNotEmpty()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun onClickCreateAnimal(view: View) {
        Log.d(loggingTag, "create button callback, id : ${view.id}")

        disposable = animalService.createAnimal(
            token = (activity as MainActivity).userToken(),
            environmentId = environment.id ?: -1,
            body = Animal(
                name = name.text.toString(),
                animalTypeId = spinner.selectedItemPosition
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

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val type = animalsTypes[position]
        Log.d(loggingTag, type.icon.substring("data:image/png;base64,".length))
        val bytes = Base64.decode(type.icon.substring("data:image/png;base64,".length), Base64.DEFAULT)
        image.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
    }
}