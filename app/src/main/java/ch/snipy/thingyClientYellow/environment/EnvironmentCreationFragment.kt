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
    private val loggingTag = "ENVIRONMENT_CREATION"

    // For API call
    private val environmentService by lazy { DyrEnvironmentService.create() }
    private var disposable: Disposable? = null

    // UI field
    private lateinit var radioGroup: RadioGroup

    private lateinit var aquaterrarium: RadioButton
    private lateinit var aquariumRadioButton: RadioButton
    private lateinit var terrariumRadioButton: RadioButton

    private lateinit var name: EditText

    private lateinit var environmentTypeImage: ImageView

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

        aquaterrarium = rootView.findViewById(R.id.environment_creation_aquaterrarium_button)
        aquariumRadioButton = rootView.findViewById(R.id.environment_creation_aquarium_button)
        terrariumRadioButton = rootView.findViewById(R.id.environment_creation_terrarium_button)

        name = rootView.findViewById(R.id.environment_creation_name)

        environmentTypeImage = rootView.findViewById(R.id.environment_creation_image)

        cancelButton = rootView.findViewById(R.id.environment_creation_cancel)
        cancelButton.setOnClickListener { _ -> fragmentManager?.popBackStack() }

        createButton = rootView.findViewById(R.id.environment_creation_create)
        createButton.setOnClickListener(::onClickCreateEnvironmentConfirm)

        // UI sugar, change the image when the radio button change
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.environment_creation_aquaterrarium_button ->
                    environmentTypeImage.setImageResource(R.drawable.ic_android_red_64dp)
                R.id.environment_creation_aquarium_button ->
                    environmentTypeImage.setImageResource(R.drawable.ic_android_green_64dp)
                R.id.environment_creation_terrarium_button ->
                    environmentTypeImage.setImageResource(R.drawable.ic_android_blue_64dp)
            }
        }

        return rootView
    }

    private fun onClickCreateEnvironmentConfirm(view: View) {
        Log.d(loggingTag, "create button callback, id : ${view.id}")


        disposable = environmentService.createEnvironment(
            (activity as MainActivity).userId(),
            Environment(
                name = name.text.toString(),
                envType = when (radioGroup.checkedRadioButtonId) {
                    R.id.environment_creation_aquaterrarium_button -> "aquaterrarium"
                    R.id.environment_creation_aquarium_button -> "aquarium"
                    R.id.environment_creation_terrarium_button -> "terrarium"
                    else -> "no-type"
                }
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { environment ->
                    Log.d(loggingTag, environment.toString())
                    Toast.makeText(activity, "Environment has been successfully created", Toast.LENGTH_SHORT).show()
                    fragmentManager?.popBackStack()
                },
                { error ->
                    Log.e(loggingTag, error.toString())
                }
            )
    }
}