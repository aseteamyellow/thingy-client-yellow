package ch.snipy.thingyClientYellow

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    // For logging
    private val loggingTag = "SETTINGS_FRAGMENT"

    companion object {
        fun newInstance(): SettingsFragment {
            val fragment = SettingsFragment()
            return fragment
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
    }
}