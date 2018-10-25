package ch.snipy.thingyClientYellow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class RegisterActivityFragment : Fragment() {

    lateinit var loginSignal: LoginSignal

    companion object {
        fun newInstance(): RegisterActivityFragment {
            return RegisterActivityFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        val loginButton = view.findViewById<Button>(R.id.sign_in_nav)
        loginButton.setOnClickListener { _ -> loginSignal.loginSignal() }
        return view
    }
}
