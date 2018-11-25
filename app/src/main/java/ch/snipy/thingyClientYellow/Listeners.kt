package ch.snipy.thingyClientYellow

import android.view.View
import okhttp3.ResponseBody

interface EnvironmentsItemViewListener {
    fun onEnvironmentItemClick(view: View, environment: Environment)
    fun onEnvironmentItemDeleteClick(
        view: View,
        environment: Environment,
        onSuccess: (ResponseBody) -> Unit,
        onError: (Throwable) -> Unit
    )
}

interface AnimalsItemViewListener {
    fun onAnimalItemClick(view: View, animal: Animal)
    fun onAnimalItemDeleteClick(
        view: View,
        animal: Animal,
        onSuccess: (ResponseBody) -> Unit,
        onError: (Throwable) -> Unit
    )
}
