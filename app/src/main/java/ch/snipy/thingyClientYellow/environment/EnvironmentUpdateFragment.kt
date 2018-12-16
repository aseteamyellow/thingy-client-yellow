package ch.snipy.thingyClientYellow.environment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
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
import java.io.ByteArrayOutputStream

class EnvironmentUpdateFragment : Fragment() {

    // For logging
    private val loggingTag = "ENVIRONMENT_UPDATE_FRAGMENT"

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
        name.onChange()
        thingy = rootView.findViewById(R.id.environment_update_thingy)
        thingy.onChange()

        camera = rootView.findViewById(R.id.environment_update_camera)

        base64EnvironmentIcon = ""

        environmentIcon = rootView.findViewById(R.id.environment_update_icon)
        environmentIcon.setOnClickListener(::onClickEnvironmentIcon)

        environmentTypeImage = rootView.findViewById(R.id.environment_update_image)

        notifTemperature = rootView.findViewById(R.id.notif_update_temperature)
        notifAirQuality = rootView.findViewById(R.id.notif_update_air_quality)
        notifHumidity = rootView.findViewById(R.id.notif_update_humidity)
        notifAirPressure = rootView.findViewById(R.id.notif_update_air_pressure)
        notifLight = rootView.findViewById(R.id.notif_update_light)

        checkListener(notifTemperature, rootView)
        checkListener(notifAirQuality, rootView)
        checkListener(notifHumidity, rootView)
        checkListener(notifAirPressure, rootView)
        checkListener(notifLight, rootView)

        cancelButton = rootView.findViewById(R.id.environment_update_cancel_button)
        cancelButton.setOnClickListener { _ -> fragmentManager?.popBackStack() }

        updateButton = rootView.findViewById(R.id.environment_update_update_button)
        updateButton.setOnClickListener(::onClickUpdateEnvironment)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.environment_update_aquaterrarium_button ->
                    environmentTypeImage.setImageResource(R.mipmap.env_aquaterrarium)
                R.id.environment_update_aquarium_button ->
                    environmentTypeImage.setImageResource(R.mipmap.env_aquarium)
                R.id.environment_update_terrarium_button ->
                    environmentTypeImage.setImageResource(R.mipmap.env_terrarium)
            }
        }

        // update from current environment
        when (environment.envType) {
            "terrarium" -> radioGroup.check(R.id.environment_update_terrarium_button)
            "aquarium" -> radioGroup.check(R.id.environment_update_aquarium_button)
            "aquaterrarium" -> radioGroup.check(R.id.environment_update_aquaterrarium_button)
        }

        if (!environment.name.isEmpty()) name.text = Editable.Factory.getInstance().newEditable(environment.name)
        if (!environment.thingy.isEmpty()) thingy.text = Editable.Factory.getInstance().newEditable(environment.thingy)
        if (!environment.piCamera.isEmpty()) camera.text =
                Editable.Factory.getInstance().newEditable(environment.piCamera)

        val iconWithoutHeader = environment.icon!!.substring(22)
        val imageBytes = Base64.decode(iconWithoutHeader, 0)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        environmentIcon.setImageBitmap(decodedImage)

        minTemperature = rootView.findViewById(R.id.min_update_temperature)
        maxTemperature = rootView.findViewById(R.id.max_update_temperature)
        minAirQuality = rootView.findViewById(R.id.min_update_air_quality)
        maxAirQuality = rootView.findViewById(R.id.max_update_air_quality)
        minHumidity = rootView.findViewById(R.id.min_update_humidity)
        maxHumidity = rootView.findViewById(R.id.max_update_humidity)
        minAirPressure = rootView.findViewById(R.id.min_update_air_pressure)
        maxAirPressure = rootView.findViewById(R.id.max_update_air_pressure)
        minLight = rootView.findViewById(R.id.min_update_light)
        maxLight = rootView.findViewById(R.id.max_update_light)

        if (environment.temperature_min !== null) minTemperature.text =
                Editable.Factory.getInstance().newEditable("" + environment.temperature_min)
        if (environment.temperature_max !== null) maxTemperature.text =
                Editable.Factory.getInstance().newEditable("" + environment.temperature_max)
        if (environment.air_quality_min !== null) minAirQuality.text =
                Editable.Factory.getInstance().newEditable("" + environment.air_quality_min)
        if (environment.air_quality_max !== null) maxAirQuality.text =
                Editable.Factory.getInstance().newEditable("" + environment.air_quality_max)
        if (environment.humidity_min !== null) minHumidity.text =
                Editable.Factory.getInstance().newEditable("" + environment.humidity_min)
        if (environment.humidity_max !== null) maxHumidity.text =
                Editable.Factory.getInstance().newEditable("" + environment.humidity_max)
        if (environment.air_pressure_min !== null) minAirPressure.text =
                Editable.Factory.getInstance().newEditable("" + environment.air_pressure_min)
        if (environment.air_pressure_max !== null) maxAirPressure.text =
                Editable.Factory.getInstance().newEditable("" + environment.air_pressure_max)
        if (environment.light_min !== null) minLight.text =
                Editable.Factory.getInstance().newEditable("" + environment.light_min)
        if (environment.light_max !== null) maxLight.text =
                Editable.Factory.getInstance().newEditable("" + environment.light_max)

        notifTemperature.isChecked = environment.temperatureNotification == 1
        notifAirQuality.isChecked = environment.air_qualityNotification == 1
        notifHumidity.isChecked = environment.humidityNotification == 1
        notifAirPressure.isChecked = environment.air_pressureNotification == 1
        notifLight.isChecked = environment.lightNotification == 1


        return rootView
    }

    private fun EditText.onChange() {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateButton.isEnabled = name.text.isNotEmpty() && thingy.text.isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun checkListener(switch: Switch, view: View) {
        switch.setOnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
            val minId = resources.getIdentifier(
                "min_update_" + switch.text.toString().replace(' ', '_').toLowerCase(),
                "id",
                context!!.packageName
            )
            val maxId = resources.getIdentifier(
                "max_update_" + switch.text.toString().replace(' ', '_').toLowerCase(),
                "id",
                context!!.packageName
            )
            view.findViewById<EditText>(minId).isEnabled = isChecked
            view.findViewById<EditText>(maxId).isEnabled = isChecked
        }
    }

    private fun onClickEnvironmentIcon(view: View) {
        val intent = Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT)   // TODO Change to png
        startActivityForResult(Intent.createChooser(intent, ""), 888)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 888 && resultCode == Activity.RESULT_OK) {
            val fileURI = data?.data!!
            environmentIcon.setImageURI(fileURI)

            val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, fileURI)

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val base64 = java.util.Base64.getEncoder().encodeToString(byteArray)
            base64EnvironmentIcon = "data:image/png;base64,$base64"
        }
    }

    private fun onClickUpdateEnvironment(view: View) {
        Log.d(loggingTag, "update button callback, id : ${view.id}")

        disposable = environmentService.updateEnvironment(
            token = (activity as MainActivity).userToken(),
            envId = environment.id ?: -1,
            body = environment.copy(
                name = name.text.toString(),
                envType = when (radioGroup.checkedRadioButtonId) {
                    R.id.environment_update_aquaterrarium_button -> "aquaterrarium"
                    R.id.environment_update_aquarium_button -> "aquarium"
                    R.id.environment_update_terrarium_button -> "terrarium"
                    else -> "no-type"
                },
                animals = null,
                icon = if (!base64EnvironmentIcon.isEmpty()) base64EnvironmentIcon else null,
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