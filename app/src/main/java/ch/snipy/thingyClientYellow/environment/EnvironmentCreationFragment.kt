package ch.snipy.thingyClientYellow.environment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import ch.snipy.thingyClientYellow.R

class EnvironmentCreationFragment : Fragment() {

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
}