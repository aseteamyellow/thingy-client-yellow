package ch.snipy.thingyClientYellow.environment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.snipy.thingyClientYellow.Environment
import ch.snipy.thingyClientYellow.EnvironmentsItemViewListener
import ch.snipy.thingyClientYellow.MainActivity
import ch.snipy.thingyClientYellow.R
import ch.snipy.thingyClientYellow.routes.DyrEnvironmentService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

// Fragment which show all the environments for a specific user
class EnvironmentsFragment : Fragment() {

    // data container
    private val environments: MutableList<Environment> = mutableListOf()

    // For the recycler view
    private lateinit var recyclerView: RecyclerView

    // Listener for environment's items
    private lateinit var listener: EnvironmentsItemViewListener

    // For API call
    private val environmentService by lazy { DyrEnvironmentService.create() }
    private var disposable: Disposable? = null

    companion object {
        fun newInstance(
            recyclerViewListener: EnvironmentsItemViewListener
        ): EnvironmentsFragment {
            val fragment = EnvironmentsFragment()
            fragment.listener = recyclerViewListener
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        disposable = environmentService.getEnvironments((activity as MainActivity).userId())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    environments.addAll(result)
                },
                { error ->
                    Log.e("ENVIRONMENTS", error.toString())
                }
            )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val rootView = inflater.inflate(R.layout.fragment_environments, container, false)

        recyclerView = rootView.findViewById<RecyclerView>(R.id.environments_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = EnvironmentAdapter(
                dataset = environments,
                context = context!!,
                listener = listener
            )
        }
        return rootView
    }
}
