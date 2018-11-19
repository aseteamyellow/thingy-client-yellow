package ch.snipy.thingyClientYellow.environment

import android.view.View
import ch.snipy.thingyClientYellow.Environment

interface EnvironmentsItemViewListener {
    fun onItemClick(view: View, environment: Environment)
}
