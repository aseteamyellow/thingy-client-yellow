package ch.snipy.thingyClientYellow.environment

import android.os.Bundle
import android.text.Editable
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

class EnvironmentUpdateFragment : Fragment() {

    // For logging
    private val loggingTag = "ENVIRONMENT_UPDATE_FRAGMENT"

    // UI field
    private lateinit var radioGroup: RadioGroup

    private lateinit var aquaterrarium: RadioButton
    private lateinit var aquariumRadioButton: RadioButton
    private lateinit var terrariumRadioButton: RadioButton

    private lateinit var name: EditText

    private lateinit var environmentTypeImage: ImageView

    // Button
    private lateinit var cancelButton: Button
    private lateinit var updateButton: Button

    // The current environment
    private lateinit var environment: Environment

    // For API call
    private val environmentService by lazy { ((activity) as MainActivity).environmentService }
    private var disposable: Disposable? = null

    companion object {
        fun newInstance(environment: Environment): EnvironmentUpdateFragment {
            val fragment = EnvironmentUpdateFragment()
            fragment.environment = environment
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_environment_update, container, false)

        radioGroup = rootView.findViewById(R.id.environment_update_radio_group_type)

        aquaterrarium = rootView.findViewById(R.id.environment_update_aquaterrarium_button)
        aquariumRadioButton = rootView.findViewById(R.id.environment_update_aquarium_button)
        terrariumRadioButton = rootView.findViewById(R.id.environment_update_terrarium_button)

        name = rootView.findViewById(R.id.environment_update_name)

        environmentTypeImage = rootView.findViewById(R.id.environment_update_image)

        // update from current environment
        when (environment.envType) {
            "terrarium" -> radioGroup.check(R.id.environment_update_terrarium_button)
            "aquarium" -> radioGroup.check(R.id.environment_update_aquarium_button)
            "aquaterrarium" -> radioGroup.check(R.id.environment_update_aquaterrarium_button)
        }

        name.text = Editable.Factory.getInstance().newEditable(environment.name)

        cancelButton = rootView.findViewById(R.id.environment_update_cancel_button)
        cancelButton.setOnClickListener { _ -> fragmentManager?.popBackStack() }

        updateButton = rootView.findViewById(R.id.environment_update_update_button)
        updateButton.setOnClickListener(::onClickUpdateEnvironment)

        return rootView
    }

    private fun onClickUpdateEnvironment(view: View) {
        Log.d(loggingTag, "update button callback, id : ${view.id}")

        disposable = environmentService.updateEnvironment(
            environment.id ?: -1,
            environment.copy(
                name = name.text.toString(),
                envType = when (radioGroup.checkedRadioButtonId) {
                    R.id.environment_update_aquaterrarium_button -> "aquaterrarium"
                    R.id.environment_update_aquarium_button -> "aquarium"
                    R.id.environment_update_terrarium_button -> "terrarium"
                    else -> "no-type"
                },
                animals = null
            )
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { environment ->
                    Log.d(loggingTag, "environment update success")
                    Toast.makeText(activity, "environment successfully updated", Toast.LENGTH_SHORT).show()
                    fragmentManager?.popBackStack()
                    fragmentManager?.popBackStack() // quite ugly
                },
                { error ->
                    Log.e(loggingTag, error.toString())
                    Toast.makeText(activity, "Can't update environment...", Toast.LENGTH_SHORT).show()
                }
            )
    }
}