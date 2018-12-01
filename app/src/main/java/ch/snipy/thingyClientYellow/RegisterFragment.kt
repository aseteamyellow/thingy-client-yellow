package ch.snipy.thingyClientYellow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class RegisterFragment : Fragment() {

    // For logging
    private val loggingTag = "REGISTER_FRAGMENT"

    companion object {
        fun newInstance(): RegisterFragment {
            val fragment = RegisterFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        return view
    }
}
