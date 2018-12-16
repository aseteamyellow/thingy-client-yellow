package ch.snipy.thingyClientYellow.environment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.snipy.thingyClientYellow.*
import ch.snipy.thingyClientYellow.animal.AnimalAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EnvironmentFragment : Fragment() {

    // For logging
    private val loggingTag = "ENVIRONMENT_FRAGMENT"

    // For the recycler view
    private lateinit var recyclerView: RecyclerView

    // Listener for animal's items
    private lateinit var listener: AnimalsItemViewListener

    // UI field
    private lateinit var name: TextView
    private lateinit var thingy: TextView
    private lateinit var camera: TextView

    private lateinit var environmentIcon: ImageView
    private lateinit var environmentTypeImage: ImageView
    private lateinit var environmentTypeLabel: TextView

    private lateinit var notifTemperature: Switch
    private lateinit var notifAirQuality: Switch
    private lateinit var notifHumidity: Switch
    private lateinit var notifAirPressure: Switch
    private lateinit var notifLight: Switch

    private lateinit var minTemperature: TextView
    private lateinit var maxTemperature: TextView
    private lateinit var minAirQuality: TextView
    private lateinit var maxAirQuality: TextView
    private lateinit var minHumidity: TextView
    private lateinit var maxHumidity: TextView
    private lateinit var minAirPressure: TextView
    private lateinit var maxAirPressure: TextView
    private lateinit var minLight: TextView
    private lateinit var maxLight: TextView

    // button
    private lateinit var addAnimalButton: Button
    private lateinit var updateEnvironmentButton: ImageButton
    private lateinit var deleteEnvironmentButton: ImageButton
    private lateinit var viewVideoButton: Button

    // Api call
    private val environmentService by lazy { ((activity) as MainActivity).environmentService }
    private val animalService by lazy { ((activity) as MainActivity).animalService }
    private val animalTypesService by lazy { ((activity) as MainActivity).animalTypeService }
    private var disposable: Disposable? = null


    // The current environment
    private lateinit var environment: Environment

    // data container for the animalsList
    private val animalsList: MutableList<Animal> = mutableListOf()

    companion object {
        fun newInstance(
            environment: Environment,
            animalsItemViewListener: AnimalsItemViewListener
        ): EnvironmentFragment {
            val fragment = EnvironmentFragment()
            fragment.environment = environment // TODO do we need to call the api service ?
            fragment.listener = animalsItemViewListener
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        disposable = animalService.getAllAnimalsForAnEnvironment(
            token = (activity as MainActivity).userToken(),
            environmentId = environment.id ?: -1
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { animals ->
                    animalsList.clear()
                    animalsList.addAll(animals)
                    recyclerView.adapter?.notifyDataSetChanged()
                },
                { error ->
                    Log.e(loggingTag, error.toString())
                }
            )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val rootView = inflater.inflate(R.layout.fragment_environment, container, false)

        disposable = animalTypesService.getAnimalTypes(token = (activity as MainActivity).userToken())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { results ->
                    recyclerView = rootView.findViewById<RecyclerView>(R.id.environment_recycler_view).apply {
                        setHasFixedSize(true)
                        //layoutManager = LinearLayoutManager(activity)
                        layoutManager = GridLayoutManager(context!!, 2)
                        adapter = AnimalAdapter(
                            dataset = animalsList,
                            context = context!!,
                            listener = listener,
                            animalTypes = results
                        )
                    }
                },
                { error ->
                    Log.e(loggingTag, error.toString())
                }
            )

        /*recyclerView = rootView.findViewById<RecyclerView>(R.id.environment_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = AnimalAdapter(
                dataset = animalsList,
                context = context!!,
                listener = listener
            )
        }*/

        name = rootView.findViewById(R.id.environment_name)
        thingy = rootView.findViewById(R.id.environment_thingy)
        camera = rootView.findViewById(R.id.environment_camera)

        environmentIcon = rootView.findViewById(R.id.environment_icon)
        environmentTypeImage = rootView.findViewById(R.id.environment_image)
        environmentTypeLabel = rootView.findViewById(R.id.environment_type_name)

        notifTemperature = rootView.findViewById(R.id.notif_environment_temperature)
        notifAirQuality = rootView.findViewById(R.id.notif_environment_air_quality)
        notifHumidity = rootView.findViewById(R.id.notif_environment_humidity)
        notifAirPressure = rootView.findViewById(R.id.notif_environment_air_pressure)
        notifLight = rootView.findViewById(R.id.notif_environment_light)

        minTemperature = rootView.findViewById(R.id.min_environment_temperature)
        maxTemperature = rootView.findViewById(R.id.max_environment_temperature)
        minAirQuality = rootView.findViewById(R.id.min_environment_air_quality)
        maxAirQuality = rootView.findViewById(R.id.max_environment_air_quality)
        minHumidity = rootView.findViewById(R.id.min_environment_humidity)
        maxHumidity = rootView.findViewById(R.id.max_environment_humidity)
        minAirPressure = rootView.findViewById(R.id.min_environment_air_pressure)
        maxAirPressure = rootView.findViewById(R.id.max_environment_air_pressure)
        minLight = rootView.findViewById(R.id.min_environment_light)
        maxLight = rootView.findViewById(R.id.max_environment_temperature)

        if (!environment.name.isEmpty()) name.text = environment.name
        if (!environment.thingy.isEmpty()) thingy.text = environment.thingy
        if (!environment.piCamera.isEmpty()) camera.text = environment.piCamera

        val iconWithoutHeader = environment.icon!!.substring(22)
        val imageBytes = Base64.decode(iconWithoutHeader, 0)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        environmentIcon.setImageBitmap(decodedImage)

        when (environment.envType) {
            "terrarium" -> environmentTypeImage.setImageResource(R.mipmap.env_terrarium)
            "aquarium" -> environmentTypeImage.setImageResource(R.mipmap.env_aquarium)
            "aquaterrarium" -> environmentTypeImage.setImageResource(R.mipmap.env_aquaterrarium)
        }
        environmentTypeLabel.text =
                environment.envType.replaceFirst(environment.envType[0], environment.envType.get(0).toUpperCase())

        notifTemperature.isChecked = environment.temperatureNotification == 1
        notifAirQuality.isChecked = environment.air_qualityNotification == 1
        notifHumidity.isChecked = environment.humidityNotification == 1
        notifAirPressure.isChecked = environment.air_pressureNotification == 1
        notifLight.isChecked = environment.lightNotification == 1

        if (environment.temperature_min !== null) minTemperature.text = environment.temperature_min.toString()
        if (environment.temperature_max !== null) maxTemperature.text = environment.temperature_max.toString()
        if (environment.air_quality_min !== null) minAirQuality.text = environment.air_quality_min.toString()
        if (environment.air_quality_max !== null) maxAirQuality.text = environment.air_quality_max.toString()
        if (environment.humidity_min !== null) minHumidity.text = environment.humidity_min.toString()
        if (environment.humidity_max !== null) maxHumidity.text = environment.humidity_max.toString()
        if (environment.air_pressure_min !== null) minAirPressure.text = environment.air_pressure_min.toString()
        if (environment.air_pressure_max !== null) maxAirPressure.text = environment.air_pressure_max.toString()
        if (environment.light_min !== null) minLight.text = environment.light_min.toString()
        if (environment.light_max !== null) maxLight.text = environment.light_max.toString()

        addAnimalButton = rootView.findViewById(R.id.environment_add_animal_button)
        addAnimalButton.setOnClickListener(::onClickCreateAnimal)

        viewVideoButton = rootView.findViewById(R.id.environment_view_video_button)
        viewVideoButton.setOnClickListener(::onClickViewVideo)

        updateEnvironmentButton = rootView.findViewById(R.id.environment_update_button)
        updateEnvironmentButton.setOnClickListener(::onClickUpdateEnvironment)

        deleteEnvironmentButton = rootView.findViewById(R.id.environment_delete_button)
        deleteEnvironmentButton.setOnClickListener(::onClickDeleteEnvironment)

        return rootView
    }

    private fun onClickViewVideo(view: View) {
        Log.d(loggingTag, "onClick view video from the view ${view.id}")
        (activity as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_frame_layout, EnvironmentVideoFragment.newInstance(environment))
            .addToBackStack(null)
            .commit()
    }

    private fun onClickCreateAnimal(view: View) {
        Log.d(loggingTag, "create animal button callback, id : ${view.id}")
        (activity as MainActivity).onClickCreateAnimalNavigation(environment)
    }

    private fun onClickUpdateEnvironment(view: View) {
        Log.d(loggingTag, "On click update environment from view : ${view.id}")
        (activity as MainActivity).supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_activity_frame_layout,
                EnvironmentUpdateFragment.newInstance(environment)
            )
            .addToBackStack(null)
            .commit()
    }

    private fun onClickDeleteEnvironment(view: View) {
        Log.d(loggingTag, "On click delete environment from view : ${view.id}")
        disposable = environmentService.deleteEnvironment(
            token = (activity as MainActivity).userToken(),
            envId = environment.id ?: -1
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ it!!; Unit; fragmentManager?.popBackStack() }, { it!!; Unit })
    }
}