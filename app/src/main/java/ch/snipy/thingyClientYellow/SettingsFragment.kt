package ch.snipy.thingyClientYellow

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    // For logging
    private val loggingTag = "SETTINGS_FRAGMENT"

    private lateinit var userMail: EditTextPreference
    private lateinit var userPassword: EditTextPreference

    companion object {
        fun newInstance(): SettingsFragment {
            val fragment = SettingsFragment()
            return fragment
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)

        userMail = findPreference("pref_email") as EditTextPreference
        userPassword = findPreference("pref_password") as EditTextPreference
    }
}