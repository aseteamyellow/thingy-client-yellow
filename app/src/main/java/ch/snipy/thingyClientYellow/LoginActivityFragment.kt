package ch.snipy.thingyClientYellow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class LoginActivityFragment : Fragment() {

    lateinit var registerSignal: RegisterSignal

    companion object {
        fun newInstance(): LoginActivityFragment {
            return LoginActivityFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val signUpButton = view.findViewById<Button>(R.id.sign_in_nav)
        signUpButton.setOnClickListener { _ -> registerSignal.registerSignal() }
        return view
    }
}
