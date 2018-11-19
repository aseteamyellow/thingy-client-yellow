package ch.snipy.thingyClientYellow.environment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.snipy.thingyClientYellow.Environment
import ch.snipy.thingyClientYellow.EnvironmentsItemViewListener
import ch.snipy.thingyClientYellow.R
import ch.snipy.thingyClientYellow.routes.DyrEnvironmentService
import io.reactivex.disposables.Disposable

// Fragment which show all the environments for a specific user
class EnvironmentsFragment : Fragment() {

    // For the recycler view
    private lateinit var recyclerView: RecyclerView

    // Listener
    private lateinit var listener: EnvironmentsItemViewListener

    // For API call TODO
    val environmentService by lazy { DyrEnvironmentService.create() }
    var disposable: Disposable? = null

    companion object {
        fun newInstance(recyclerViewListener: EnvironmentsItemViewListener): EnvironmentsFragment {
            val fragment = EnvironmentsFragment()
            fragment.listener = recyclerViewListener
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val rootView = inflater.inflate(R.layout.fragment_environments, container, false)

        recyclerView = rootView.findViewById<RecyclerView>(R.id.environments_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = EnvironmentAdapter(
                dataset = listOf(
                    Environment(null, "A", "terra", listOf(), 20.0, 15.0, 180.0, false, false, false),
                    Environment(null, "B", "viva", listOf(), 20.0, 15.0, 180.0, false, false, false)
                ),
                context = context!!,
                listener = listener
            )
        }
        return rootView
    }
}
