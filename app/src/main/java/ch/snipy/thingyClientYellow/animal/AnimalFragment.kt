package ch.snipy.thingyClientYellow.animal

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import ch.snipy.thingyClientYellow.Animal
import ch.snipy.thingyClientYellow.MainActivity
import ch.snipy.thingyClientYellow.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AnimalFragment : Fragment() {

    // For logging
    private val loggingTag = "ANIMAL_FRAGMENT"

    // Data
    lateinit var animal: Animal

    // UI
    private lateinit var icon: ImageView
    private lateinit var name: TextView
    private lateinit var type: TextView

    // Button
    private lateinit var updateButton: ImageButton
    private lateinit var deleteButton: ImageButton

    // For API call TODO
    // val environmentService by lazy { DyrEnvironmentService.create() }
    // var disposable: Disposable? = null
    private val animalService by lazy { ((activity) as MainActivity).animalService }
    private val animalTypeService by lazy { ((activity) as MainActivity).animalTypeService }
    private var disposable: Disposable? = null

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

        name = rootView.findViewById(R.id.animal_name)
        type = rootView.findViewById(R.id.animal_type)
        icon = rootView.findViewById(R.id.animal_image)

        updateButton = rootView.findViewById(R.id.animal_update_button)
        updateButton.setOnClickListener(::onClickUpdateAnimal)
        deleteButton = rootView.findViewById(R.id.animal_delete_button)
        deleteButton.setOnClickListener(::onClickDeleteAnimal)

        name.text = animal.name

        disposable = animalTypeService.getAnimalTypes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { animalTypes ->
                    type.text = animalTypes[animal.animalTypeId].type
                    val iconWithoutHeader = animalTypes[animal.animalTypeId].icon.substring(22)
                    val imageBytes = Base64.decode(iconWithoutHeader, 0)
                    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    icon.setImageBitmap(decodedImage)
                },
                { error ->
                    Log.e(loggingTag, error.toString())
                }
            )

        return rootView
    }

    private fun onClickUpdateAnimal(view: View) {
        Log.d(loggingTag, "On click update animal from view : ${view.id}")
        (activity as MainActivity).supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_activity_frame_layout,
                AnimalUpdateFragment.newInstance(animal)
            )
            .addToBackStack(null)
            .commit()
    }

    private fun onClickDeleteAnimal(view: View) {
        Log.d(loggingTag, "On click delete animal from view : ${view.id}")
        disposable = animalService.deleteAnimal(animalId = animal.id ?: -1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({it!! ; Unit; fragmentManager?.popBackStack()}, {it!! ; Unit})
    }
}