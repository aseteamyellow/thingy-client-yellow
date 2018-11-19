package ch.snipy.thingyClientYellow.environment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ch.snipy.thingyClientYellow.R

class EnvironmentFragment : Fragment() {
    companion object {
        fun newInstance(): EnvironmentFragment {
            val fragment = EnvironmentFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_environment, container,false)
        super.onCreateView(inflater, container, savedInstanceState)
        return view
    }
}