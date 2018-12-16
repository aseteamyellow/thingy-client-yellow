package ch.snipy.thingyClientYellow.animal

import android.graphics.BitmapFactory
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
import android.widget.ArrayAdapter

class AnimalUpdateFragment : Fragment(), AdapterView.OnItemSelectedListener {

    // For logging
    private val loggingTag = "ANIMAL_UPDATE_FRAGMENT"

    // UI field
    private lateinit var name: EditText
    private lateinit var image: ImageView
    private lateinit var spinner: Spinner

    // Button
    private lateinit var cancelButton: Button
    private lateinit var updateButton: Button

    // The current animal
    private lateinit var animal: Animal

    // types for an animal
    private lateinit var animalsTypes: List<AnimalType>

    // For API call
    private val animalService by lazy { ((activity) as MainActivity).animalService }
    private val animalTypeService by lazy { ((activity) as MainActivity).animalTypeService }
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
        val rootView = inflater.inflate(R.layout.fragment_animal_update, container, false)

        name = rootView.findViewById(R.id.animal_update_name)
        image = rootView.findViewById(R.id.animal_update_image)
        spinner = rootView.findViewById(R.id.spinner_update_animal_type)

        name.text = Editable.Factory.getInstance().newEditable(animal.name)

        cancelButton = rootView.findViewById(R.id.animal_update_cancel_button)
        cancelButton.setOnClickListener { _ -> fragmentManager?.popBackStack() }

        updateButton = rootView.findViewById(R.id.animal_update_update_button)
        updateButton.setOnClickListener(::onClickUpdateAnimal)

        name.onChange()

        disposable = animalTypeService.getAnimalTypes()
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
                    spinner.setSelection(animal.animalTypeId, true)
                    Log.d(loggingTag, result.map { Tuple(it.id, it.type) }.toString())
                },
                { error ->
                    Log.e(loggingTag, error.toString())
                }
            )

        spinner.onItemSelectedListener = this

        return rootView
    }

    private fun EditText.onChange() {
        this.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateButton.isEnabled = name.text.isNotEmpty()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun onClickUpdateAnimal(view: View) {
        Log.d(loggingTag, "update button callback, id : ${view.id}")

        disposable = animalService.updateAnimal(
            token = (activity as MainActivity).userToken(),
            animalId = animal.id ?: -1,
            body = Animal(
                id = animal.id,
                name = name.text.toString()
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { animal ->
                    Log.d(loggingTag, "animal update success")
                    Toast.makeText(activity, "Animal successfully updated", Toast.LENGTH_SHORT).show()
                    fragmentManager?.popBackStack()
                    fragmentManager?.popBackStack() // quite ugly
                },
                { error ->
                    Log.e(loggingTag, error.toString())
                    Toast.makeText(activity, "Can't update animal...", Toast.LENGTH_SHORT).show()
                }
            )
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val type = animalsTypes[position]
        Log.d(loggingTag, type.icon.substring("data:image/png;base64,".length))
        val bytes = Base64.decode(type.icon.substring("data:image/png;base64,".length), Base64.DEFAULT)
        image.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
    }
}