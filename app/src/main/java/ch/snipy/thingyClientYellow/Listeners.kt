package ch.snipy.thingyClientYellow

import android.view.View

interface EnvironmentsItemViewListener {
    fun onEnvironmentItemClick(view: View, environment: Environment)
}

interface AnimalsItemViewListener {
    fun onAnimalItemClick(view: View, animal: Animal)
}
