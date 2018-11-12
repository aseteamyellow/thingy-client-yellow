package ch.snipy.thingyClientYellow.environment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.snipy.thingyClientYellow.Environment
import ch.snipy.thingyClientYellow.R
import ch.snipy.thingyClientYellow.routes.DyrEnvironmentService
import io.reactivex.disposables.Disposable

// Fragment which show all the environments for a specific user
class EnvironmentsFragment : Fragment() {

    // For the recycler view
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    // For API call
    val environmentService by lazy { DyrEnvironmentService.create() }
    var disposable: Disposable? = null

    companion object {
        fun newInstance() = EnvironmentsFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        viewManager = LinearLayoutManager(activity)
        viewAdapter = EnvironmentAdapter(
            dataset = listOf(
                Environment(null, "A", "terra", listOf(), 20.0, 15.0, 180.0, false, false, false),
                Environment(null, "B", "viva", listOf(), 20.0, 15.0, 180.0, false, false, false)
            ),
            context = context!!
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.environments_fragment, container, false)

        recyclerView = rootView.findViewById<RecyclerView>(R.id.environments_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        return rootView
    }
}
