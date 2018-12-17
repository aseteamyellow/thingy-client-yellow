package ch.snipy.thingyClientYellow.environment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import ch.snipy.thingyClientYellow.Environment
import ch.snipy.thingyClientYellow.MainActivity
import ch.snipy.thingyClientYellow.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*
import android.provider.MediaStore
import java.io.ByteArrayOutputStream


class EnvironmentCreationFragment : Fragment() {

    // For logging
    private val loggingTag = "ENVIRONMENT_CREATION"

    // For API call
    private val environmentService by lazy { ((activity) as MainActivity).environmentService }
    private var disposable: Disposable? = null

    // UI field
    private lateinit var radioGroup: RadioGroup

    private lateinit var aquaterrarium: RadioButton
    private lateinit var aquariumRadioButton: RadioButton
    private lateinit var terrariumRadioButton: RadioButton

    private lateinit var name: EditText
    private lateinit var thingy: EditText
    private lateinit var camera: EditText

    private lateinit var environmentIcon: ImageView
    private lateinit var base64EnvironmentIcon: String
    private lateinit var environmentTypeImage: ImageView

    private lateinit var notifTemperature: Switch
    private lateinit var notifAirQuality: Switch
    private lateinit var notifHumidity: Switch
    private lateinit var notifAirPressure: Switch
    private lateinit var notifLight: Switch

    private lateinit var minTemperature: EditText
    private lateinit var maxTemperature: EditText
    private lateinit var minAirQuality: EditText
    private lateinit var maxAirQuality: EditText
    private lateinit var minHumidity: EditText
    private lateinit var maxHumidity: EditText
    private lateinit var minAirPressure: EditText
    private lateinit var maxAirPressure: EditText
    private lateinit var minLight: EditText
    private lateinit var maxLight: EditText

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
        name.onChange()
        thingy = rootView.findViewById(R.id.environment_creation_thingy)
        thingy.onChange()

        camera = rootView.findViewById(R.id.environment_creation_camera)

        environmentIcon = rootView.findViewById(R.id.environment_creation_icon)
        environmentIcon.setOnClickListener(::onClickEnvironmentIcon)

        val base64URI = Uri.parse("android.resource://" + context!!.packageName + "/" + R.mipmap.icon_environment)
        val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, base64URI)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        base64EnvironmentIcon = Base64.getEncoder().encodeToString(byteArray)
        base64EnvironmentIcon = "data:image/png;base64,$base64EnvironmentIcon"

        environmentTypeImage = rootView.findViewById(R.id.environment_creation_image)

        notifTemperature = rootView.findViewById(R.id.notif_creation_temperature)
        notifAirQuality = rootView.findViewById(R.id.notif_creation_air_quality)
        notifHumidity = rootView.findViewById(R.id.notif_creation_humidity)
        notifAirPressure = rootView.findViewById(R.id.notif_creation_air_pressure)
        notifLight = rootView.findViewById(R.id.notif_creation_light)

        checkListener(notifTemperature, rootView)
        checkListener(notifAirQuality, rootView)
        checkListener(notifHumidity, rootView)
        checkListener(notifAirPressure, rootView)
        checkListener(notifLight, rootView)

        minTemperature = rootView.findViewById(R.id.min_creation_temperature)
        maxTemperature = rootView.findViewById(R.id.max_creation_temperature)
        minAirQuality = rootView.findViewById(R.id.min_creation_air_quality)
        maxAirQuality = rootView.findViewById(R.id.max_creation_air_quality)
        minHumidity = rootView.findViewById(R.id.min_creation_humidity)
        maxHumidity = rootView.findViewById(R.id.max_creation_humidity)
        minAirPressure = rootView.findViewById(R.id.min_creation_air_pressure)
        maxAirPressure = rootView.findViewById(R.id.max_creation_air_pressure)
        minLight = rootView.findViewById(R.id.min_creation_light)
        maxLight = rootView.findViewById(R.id.max_creation_light)

        cancelButton = rootView.findViewById(R.id.environment_creation_cancel)
        cancelButton.setOnClickListener { _ -> fragmentManager?.popBackStack() }

        createButton = rootView.findViewById(R.id.environment_creation_create)
        createButton.setOnClickListener(::onClickCreateEnvironmentConfirm)

        // UI sugar, change the image when the radio button change
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.environment_creation_aquaterrarium_button ->
                    environmentTypeImage.setImageResource(R.mipmap.env_aquaterrarium)
                R.id.environment_creation_aquarium_button ->
                    environmentTypeImage.setImageResource(R.mipmap.env_aquarium)
                R.id.environment_creation_terrarium_button ->
                    environmentTypeImage.setImageResource(R.mipmap.env_terrarium)
            }
        }

        return rootView
    }

    private fun EditText.onChange() {
        this.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                createButton.isEnabled = name.text.isNotEmpty() && thingy.text.isNotEmpty()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun checkListener(switch:Switch, view: View) {
        switch.setOnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
            val minId = resources.getIdentifier("min_creation_" + switch.text.toString().replace(' ','_').toLowerCase(), "id", context!!.packageName)
            val maxId = resources.getIdentifier("max_creation_" + switch.text.toString().replace(' ','_').toLowerCase(), "id", context!!.packageName)
            view.findViewById<EditText>(minId).isEnabled = isChecked
            view.findViewById<EditText>(maxId).isEnabled = isChecked
        }
    }

    // TODO add a progress bar

    private fun onClickEnvironmentIcon(view: View) {
        val intent = Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT)   // TODO Change to png
        startActivityForResult(Intent.createChooser(intent,""),888)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 888 && resultCode == RESULT_OK) {
            val fileURI = data?.data!!
            environmentIcon.setImageURI(fileURI)

            val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, fileURI)

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val base64 = Base64.getEncoder().encodeToString(byteArray)
            base64EnvironmentIcon = "data:image/png;base64,$base64"

        }
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
                },
                icon = base64EnvironmentIcon,
                temperatureNotification = if (notifTemperature.isChecked) 1 else 0,
                air_qualityNotification = if (notifAirQuality.isChecked) 1 else 0,
                humidityNotification = if (notifHumidity.isChecked) 1 else 0,
                air_pressureNotification = if (notifAirPressure.isChecked) 1 else 0,
                lightNotification = if (notifLight.isChecked) 1 else 0,
                temperature_min = if (!minTemperature.text.isEmpty()) minTemperature.text.toString().toDouble() else null,
                temperature_max = if (!maxTemperature.text.isEmpty()) maxTemperature.text.toString().toDouble() else null,
                air_quality_min = if (!minAirQuality.text.isEmpty()) minAirQuality.text.toString().toDouble() else null,
                air_quality_max = if (!maxAirQuality.text.isEmpty()) maxAirQuality.text.toString().toDouble() else null,
                humidity_min = if (!minHumidity.text.isEmpty()) minHumidity.text.toString().toDouble() else null,
                humidity_max = if (!maxHumidity.text.isEmpty()) maxHumidity.text.toString().toDouble() else null,
                air_pressure_min = if (!minAirPressure.text.isEmpty()) minAirPressure.text.toString().toDouble() else null,
                air_pressure_max = if (!maxAirPressure.text.isEmpty()) maxAirPressure.text.toString().toDouble() else null,
                light_min = if (!minLight.text.isEmpty()) minLight.text.toString().toDouble() else null,
                light_max = if (!maxLight.text.isEmpty()) maxLight.text.toString().toDouble() else null
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