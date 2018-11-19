package ch.snipy.thingyClientYellow

import android.view.View

interface EnvironmentsItemViewListener {
    fun onItemClick(view: View, environment: Environment)
}

interface AnimalsItemViewListener {
    fun onItemClick(view: View, animal: Animal)
}
