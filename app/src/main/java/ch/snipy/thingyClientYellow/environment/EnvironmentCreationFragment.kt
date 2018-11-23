package ch.snipy.thingyClientYellow.environment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import ch.snipy.thingyClientYellow.R

class EnvironmentCreationFragment : Fragment() {

    // UI field
    private lateinit var radioGroup: RadioGroup

    // TODO : onChecked -> change image logo
    private lateinit var vivariumRadioButton: RadioButton
    private lateinit var aquariumRadioButton: RadioButton
    private lateinit var terrariumRadioButton: RadioButton

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

        return rootView
    }
}