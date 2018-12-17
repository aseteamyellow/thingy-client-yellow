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
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
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

    // graph
    private lateinit var graph: GraphView

    // Api call
    private val environmentService by lazy { ((activity) as MainActivity).environmentService }
    private val animalService by lazy { ((activity) as MainActivity).animalService }
    private val animalTypesService by lazy { ((activity) as MainActivity).animalTypeService }
    private var disposable: Disposable? = null


    // The current crtEnvironment
    private lateinit var crtEnvironment: Environment

    // data container for the animalsList
    private val animalsList: MutableList<Animal> = mutableListOf()

    companion object {
        fun newInstance(
            environment: Environment,
            animalsItemViewListener: AnimalsItemViewListener
        ): EnvironmentFragment {
            val fragment = EnvironmentFragment()
            fragment.crtEnvironment = environment // TODO do we need to call the api service ?
            fragment.listener = animalsItemViewListener
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        disposable = animalService.getAllAnimalsForAnEnvironment(
            token = (activity as MainActivity).userToken(),
            environmentId = crtEnvironment.id ?: -1
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

        recyclerView = rootView.findViewById(R.id.environment_recycler_view)

        disposable = animalTypesService.getAnimalTypes(token = (activity as MainActivity).userToken())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { results ->
                    recyclerView.apply {
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
        maxLight = rootView.findViewById(R.id.max_environment_light)

        if (!crtEnvironment.name.isEmpty()) name.text = crtEnvironment.name
        if (!crtEnvironment.thingy.isEmpty()) thingy.text = crtEnvironment.thingy
        if (!crtEnvironment.piCamera.isEmpty()) camera.text = crtEnvironment.piCamera

        val iconWithoutHeader = crtEnvironment.icon!!.substring(22)
        val imageBytes = Base64.decode(iconWithoutHeader, 0)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        environmentIcon.setImageBitmap(decodedImage)

        when (crtEnvironment.envType) {
            "terrarium" -> environmentTypeImage.setImageResource(R.mipmap.env_terrarium)
            "aquarium" -> environmentTypeImage.setImageResource(R.mipmap.env_aquarium)
            "aquaterrarium" -> environmentTypeImage.setImageResource(R.mipmap.env_aquaterrarium)
        }
        environmentTypeLabel.text =
                crtEnvironment.envType.replaceFirst(
                    crtEnvironment.envType[0],
                    crtEnvironment.envType.get(0).toUpperCase()
                )

        notifTemperature.isChecked = crtEnvironment.temperatureNotification == 1
        notifAirQuality.isChecked = crtEnvironment.air_qualityNotification == 1
        notifHumidity.isChecked = crtEnvironment.humidityNotification == 1
        notifAirPressure.isChecked = crtEnvironment.air_pressureNotification == 1
        notifLight.isChecked = crtEnvironment.lightNotification == 1

        if (crtEnvironment.temperature_min !== null) minTemperature.text = crtEnvironment.temperature_min.toString()
        if (crtEnvironment.temperature_max !== null) maxTemperature.text = crtEnvironment.temperature_max.toString()
        if (crtEnvironment.air_quality_min !== null) minAirQuality.text = crtEnvironment.air_quality_min.toString()
        if (crtEnvironment.air_quality_max !== null) maxAirQuality.text = crtEnvironment.air_quality_max.toString()
        if (crtEnvironment.humidity_min !== null) minHumidity.text = crtEnvironment.humidity_min.toString()
        if (crtEnvironment.humidity_max !== null) maxHumidity.text = crtEnvironment.humidity_max.toString()
        if (crtEnvironment.air_pressure_min !== null) minAirPressure.text = crtEnvironment.air_pressure_min.toString()
        if (crtEnvironment.air_pressure_max !== null) maxAirPressure.text = crtEnvironment.air_pressure_max.toString()
        if (crtEnvironment.light_min !== null) minLight.text = crtEnvironment.light_min.toString()
        if (crtEnvironment.light_max !== null) maxLight.text = crtEnvironment.light_max.toString()

        addAnimalButton = rootView.findViewById(R.id.environment_add_animal_button)
        addAnimalButton.setOnClickListener(::onClickCreateAnimal)

        viewVideoButton = rootView.findViewById(R.id.environment_view_video_button)
        viewVideoButton.setOnClickListener(::onClickViewVideo)

        updateEnvironmentButton = rootView.findViewById(R.id.environment_update_button)
        updateEnvironmentButton.setOnClickListener(::onClickUpdateEnvironment)

        deleteEnvironmentButton = rootView.findViewById(R.id.environment_delete_button)
        deleteEnvironmentButton.setOnClickListener(::onClickDeleteEnvironment)

        graph = rootView.findViewById(R.id.graph)

        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
            arrayOf(
                DataPoint(1.0, 2.0),
                DataPoint(2.0, 3.0),
                DataPoint(3.0, 4.0),
                DataPoint(4.0, 5.0)
            )
        )
        graph.addSeries(series)

        /*
        disposable = environmentService.getSensorsData(
            token = (activity as MainActivity).userToken(),
            thingyId = crtEnvironment.thingy ?: "yellow"
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Log.d(loggingTag, "get sensor data : $result")


                },
                { error ->
                    Log.e(loggingTag, "get sensor data : ${error.message}")
                }
            )
            */

        return rootView
    }

    private fun onClickViewVideo(view: View) {
        Log.d(loggingTag, "onClick view video from the view ${view.id}")
        (activity as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_frame_layout, EnvironmentVideoFragment.newInstance(crtEnvironment))
            .addToBackStack(null)
            .commit()
    }

    private fun onClickCreateAnimal(view: View) {
        Log.d(loggingTag, "create animal button callback, id : ${view.id}")
        (activity as MainActivity).onClickCreateAnimalNavigation(crtEnvironment)
    }

    private fun onClickUpdateEnvironment(view: View) {
        Log.d(loggingTag, "On click update crtEnvironment from view : ${view.id}")
        (activity as MainActivity).supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_activity_frame_layout,
                EnvironmentUpdateFragment.newInstance(crtEnvironment)
            )
            .addToBackStack(null)
            .commit()
    }

    private fun onClickDeleteEnvironment(view: View) {
        Log.d(loggingTag, "On click delete crtEnvironment from view : ${view.id}")
        disposable = environmentService.deleteEnvironment(
            token = (activity as MainActivity).userToken(),
            envId = crtEnvironment.id ?: -1
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ it!!; Unit; fragmentManager?.popBackStack() }, { it!!; Unit })
    }
}