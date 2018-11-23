package ch.snipy.thingyClientYellow.environment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import ch.snipy.thingyClientYellow.Environment
import ch.snipy.thingyClientYellow.MainActivity
import ch.snipy.thingyClientYellow.R
import ch.snipy.thingyClientYellow.routes.DyrEnvironmentService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EnvironmentCreationFragment : Fragment() {

    // For logging
    private val TAG = "ENVIRONMENT_CREATION"

    // For API call
    private val environmentService by lazy { DyrEnvironmentService.create() }
    private var disposable: Disposable? = null

    // UI field
    private lateinit var radioGroup: RadioGroup

    private lateinit var vivariumRadioButton: RadioButton
    private lateinit var aquariumRadioButton: RadioButton
    private lateinit var terrariumRadioButton: RadioButton

    private lateinit var name: EditText

    private lateinit var environementTypeImage: ImageView

    // Button
    private lateinit var cancelButton: Button
    private lateinit var createButton: Button


    companion object {
        fun newInstance(): EnvironmentCreationFragment {
            val fragment = EnvironmentCreationFragment()
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_environment_creation, container, false)

        radioGroup = rootView.findViewById(R.id.environment_creation_radio_group_type)

        vivariumRadioButton = rootView.findViewById(R.id.environment_creation_vivarium_button)
        aquariumRadioButton = rootView.findViewById(R.id.environment_creation_aquarium_button)
        terrariumRadioButton = rootView.findViewById(R.id.environment_creation_terrarium_button)

        name = rootView.findViewById(R.id.environment_creation_name)

        environementTypeImage = rootView.findViewById(R.id.environment_creation_image)

        cancelButton = rootView.findViewById(R.id.environment_creation_cancel)

        createButton = rootView.findViewById(R.id.environment_creation_create)
        createButton.setOnClickListener(::onClickCreateEnvironmentConfirm)

        // UI sugar, change the image when the radio button change
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.environment_creation_vivarium_button ->
                    environementTypeImage.setImageResource(R.drawable.ic_android_red_64dp)
                R.id.environment_creation_aquarium_button ->
                    environementTypeImage.setImageResource(R.drawable.ic_android_green_64dp)
                R.id.environment_creation_terrarium_button ->
                    environementTypeImage.setImageResource(R.drawable.ic_android_blue_64dp)
            }
        }

        return rootView
    }

    fun onClickCreateEnvironmentConfirm(view: View) {
        Log.d(TAG, "create button callback, id : ${view.id}")
        disposable = environmentService.createEnvironment(
            (activity as MainActivity).userId(),
            Environment(
                id = null,
                name = name.text.toString(),
                envType = when (radioGroup.checkedRadioButtonId) {
                    R.id.environment_creation_vivarium_button -> "Vivarium"
                    R.id.environment_creation_aquarium_button -> "Aquarium"
                    R.id.environment_creation_terrarium_button -> "Terrarium"
                    else -> "no-type"
                },
                animals = listOf(),
                humidity = null, temperature = null, luminosity = null,
                humidityNotification = false,
                temperatureNotification = false,
                luminosityNotification = false
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { environment ->
                    Toast.makeText(activity, "Environment has been successfully created", Toast.LENGTH_SHORT).show()
                    fragmentManager?.popBackStack()
                },
                { error ->
                    Log.e(TAG, error.toString())
                }
            )
    }
}