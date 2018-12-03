package ch.snipy.thingyClientYellow

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    // For logging
    private val loggingTag = "SETTINGS_FRAGMENT"

    // Current user
    private lateinit var crtUser: User


    companion object {
        fun newInstance(user: User): SettingsFragment {
            val fragment = SettingsFragment()
            fragment.crtUser = user
            return fragment
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
    }
}