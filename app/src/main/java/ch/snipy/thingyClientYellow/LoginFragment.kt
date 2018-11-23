package ch.snipy.thingyClientYellow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class LoginFragment : Fragment() {

    companion object {
        fun newInstance(): LoginFragment {
            val fragment = LoginFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        super.onCreateView(inflater, container, savedInstanceState)
        return view
    }
}
