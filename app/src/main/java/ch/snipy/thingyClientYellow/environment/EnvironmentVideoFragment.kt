package ch.snipy.thingyClientYellow.environment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import ch.snipy.thingyClientYellow.Environment
import ch.snipy.thingyClientYellow.MainActivity
import ch.snipy.thingyClientYellow.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.net.URL
import kotlin.concurrent.thread

class EnvironmentVideoFragment : Fragment() {

    // For logging
    private val loggingTag = "ENVIRONMENT_VIDEO_FRAGMENT"

    private lateinit var videoImageView: ImageView

    private lateinit var environment: Environment

    private val environmentService by lazy { ((activity) as MainActivity).environmentService }
    private var disposable: Disposable? = null

    private lateinit var videoThread: Thread

    private var isOn = false

    companion object {
        fun newInstance(environment: Environment): EnvironmentVideoFragment {
            val fragment = EnvironmentVideoFragment()
            fragment.environment = environment
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val rootView = inflater.inflate(R.layout.fragment_environment_video, container, false)

        videoImageView = rootView.findViewById(R.id.video)


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isOn = true
        disposable = environmentService.getEnvironment(
            token = (activity as MainActivity).userToken(),
            envId = environment.id ?: -1
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { environment ->
                    videoThread = thread(
                        start = true,
                        isDaemon = true
                    ) {
                        while (isOn) {
                            val result = URL("http://192.168.43.128:8080").readText()
                            Log.d(loggingTag, result)
                            val bytes = Base64.decode(result, Base64.DEFAULT)

                            (activity as? MainActivity)?.runOnUiThread {
                                videoImageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
                            }

                        }
                    }
                },
                { error ->
                    Log.e(loggingTag, error.message)
                }
            )
    }

    override fun onDetach() {
        isOn = false
        super.onDetach()
    }
}